#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D PlanetTexture;

uniform vec3 PlanetPosition;
uniform vec3 PlanetRotation;
uniform vec3  PlanetSize;
uniform vec3 PlanetHaloColor;

uniform vec3 SunDirection;
uniform float Time;

in vec2 texCoord;
out vec4 fragColor;

const float e = 0.001;

// --- helpers ---
float depthSampleToWorldDepth(float depthSample){
    float f = depthSample*2.0-1.0;
    return 2.0*VeilCamera.NearPlane*VeilCamera.FarPlane /
           (VeilCamera.FarPlane+VeilCamera.NearPlane - f*(VeilCamera.FarPlane - VeilCamera.NearPlane));
}

vec3 rotateX(vec3 p,float a){float c=cos(a);float s=sin(a);return vec3(p.x,c*p.y-s*p.z,s*p.y+c*p.z);}
vec3 rotateY(vec3 p,float a){float c=cos(a);float s=sin(a);return vec3(c*p.x+s*p.z,p.y,-s*p.x+c*p.z);}
vec3 rotateZ(vec3 p,float a){float c=cos(a);float s=sin(a);return vec3(c*p.x-s*p.y,s*p.x+c*p.y,p.z);}
vec3 rotatePlanet(vec3 p,vec3 r){p=rotateX(p,r.x);p=rotateY(p,r.y);p=rotateZ(p,r.z);return p;}

float computeLighting(vec3 normal, vec3 lightDir){
    return clamp(dot(normal, normalize(lightDir))*0.8 + 0.3,0.0,1.0);
}

// Pseudo-random function from position
float hash33(vec3 p){
    p = fract(p * 0.3183099 + vec3(0.1,0.2,0.3));
    p *= 17.0;
    return fract(p.x * p.y * p.z * (p.x + p.y + p.z));
}

// --- ray-box ---
bool iBox(in vec3 ro,in vec3 rd,in vec3 boxSize,out float tN,out float tF,out vec2 uv,out vec3 normal,out vec3 hitPos){
    vec3 m = 1.0/rd;
    vec3 n = m*ro;
    vec3 k = abs(m)*boxSize;
    vec3 t1 = -n-k;
    vec3 t2 = -n+k;
    tN = max(max(t1.x,t1.y),t1.z);
    tF = min(min(t2.x,t2.y),t2.z);
    if(tN>tF||tF<0.0) return false;
    if(!(tN>0.0)) tN=0.0;
    hitPos = ro + rd*tN;
    uv = vec2(0.0);
    normal = normalize(hitPos/boxSize);
    if((hitPos.z-e)<=-boxSize.z) uv = vec2(-hitPos.x/boxSize.x+3.0, -hitPos.y/boxSize.y+3.0)/8.0;
    else if((hitPos.z+e)>=boxSize.z) uv = vec2(hitPos.x/boxSize.x+7.0, -hitPos.y/boxSize.y+3.0)/8.0;
    else if((hitPos.x-e)<=-boxSize.x) uv = vec2(hitPos.z/boxSize.z+5.0, -hitPos.y/boxSize.y+3.0)/8.0;
    else if((hitPos.x+e)>=boxSize.x) uv = vec2(-hitPos.z/boxSize.z+1.0, -hitPos.y/boxSize.y+3.0)/8.0;
    else if((hitPos.y-e)<=-boxSize.y) uv = vec2(-hitPos.x/boxSize.x+3.0, hitPos.z/boxSize.z+5.0)/8.0;
    else if((hitPos.y+e)>=boxSize.y) uv = vec2(-hitPos.x/boxSize.x+3.0, -hitPos.z/boxSize.z+1.0)/8.0;
    return true;
}

// --- 3D noise (classic Perlin / value noise approximation) ---
float noise3D(vec3 p){
    vec3 i = floor(p);
    vec3 f = fract(p);

    // Smoothstep interpolation
    vec3 u = f*f*(3.0-2.0*f);

    // Hash corners
    float n000 = hash33(i + vec3(0.0,0.0,0.0));
    float n001 = hash33(i + vec3(0.0,0.0,1.0));
    float n010 = hash33(i + vec3(0.0,1.0,0.0));
    float n011 = hash33(i + vec3(0.0,1.0,1.0));
    float n100 = hash33(i + vec3(1.0,0.0,0.0));
    float n101 = hash33(i + vec3(1.0,0.0,1.0));
    float n110 = hash33(i + vec3(1.0,1.0,0.0));
    float n111 = hash33(i + vec3(1.0,1.0,1.0));

    // Trilinear interpolation
    float n00 = mix(n000, n001, u.z);
    float n01 = mix(n010, n011, u.z);
    float n10 = mix(n100, n101, u.z);
    float n11 = mix(n110, n111, u.z);

    float n0 = mix(n00, n01, u.y);
    float n1 = mix(n10, n11, u.y);

    return mix(n0, n1, u.x);
}

// --- raytrace planet with solid color ---
bool raytracePlanet(in vec3 ro, in vec3 rd, vec3 pos, vec3 rot, vec3 size, float maxDepth,
                    out float dist, out vec4 color, out vec3 hitWorld){
    vec3 boxSize = PlanetSize;
    vec3 localRo = rotatePlanet(ro-pos,rot);
    vec3 localRd = rotatePlanet(rd,rot);

    float near, far;
    vec2 uv;
    vec3 normal, hitPos;

    bool hit = iBox(localRo, localRd, boxSize, near, far, uv, normal, hitPos);
    if(!hit||near>=maxDepth) return false;

    dist = near;
    float distFactor = 1.0 - clamp(dist / maxDepth, 0.0, 1.0);
    hitWorld = rotatePlanet(hitPos,-rot)+pos;

    // Determine chunk ID for glitching
    vec3 chunkPos = floor(hitPos*1000.0);
    float chunkRand = hash33(chunkPos + floor(Time*2.0));

    vec3 glitchOffset = vec3(0.0);
    vec3 glitchRot = vec3(0.0);

    vec3 localRoGlitch = rotatePlanet(localRo - glitchOffset, glitchRot);
    vec3 localRdGlitch = rotatePlanet(localRd, glitchRot);

    hit = iBox(localRoGlitch, localRdGlitch, boxSize, near, far, uv, normal, hitPos);
    hitWorld = rotatePlanet(hitPos, -rot) + pos + glitchOffset;

    // Compute fade based on Y height (top is boxSize.y)
    //float fade = smoothstep(boxSize.y - 1.8, boxSize.y, hitPos.y);
    // fade = 0 near the middle, fade = 1 at top

    float light = computeLighting(normal, SunDirection);

    // --- gradient from bottom to top ---
    float yNormalized = (hitPos.y + boxSize.y) / (2.0 * boxSize.y);
    // yNormalized = 0 at bottom, 1 at top

    vec3 bottomColor = vec3(0.8, 0.9, 1.0); // white
    vec3 middleColor = vec3(0.2, 0.5, 0.8); // blue
    vec3 topColor = vec3(0.5, 0.2, 0.7);    // purple

    vec3 planetColorGradient;
    if(yNormalized < 0.4){
        float t = yNormalized / 0.4;
        planetColorGradient = mix(bottomColor, middleColor, t);
    } else {
        float t = (yNormalized - 0.4) / 0.4;
        planetColorGradient = mix(middleColor, topColor, t);
    }
    vec3 planetColorSolid = vec3(0.2, 0.5, 0.8); // original color
    // Mix with scene if desired (optional fade)
    float fade = smoothstep(boxSize.y - 1.8, boxSize.y, hitPos.y);
    vec3 sceneColor = texture(DiffuseSampler0, texCoord).rgb;
    planetColorSolid = mix(planetColorGradient, sceneColor, fade);

   // Inside raytracePlanet, after computing fade and light:

   // --- noise for flowing fire effect ---
   float noiseScale = 3.0;   // detail of noise
   float noiseSpeed = -5;   // speed of upward flow
   float n = noise3D(hitPos * noiseScale + vec3(0.0, Time * noiseSpeed, 0.0));

   // Make noise affect mostly the "outer" or "end" parts
   // Use fade to reduce noise effect in well-lit or central areas
   float noiseStrength =  smoothstep(0.0, 0.7, fade); // noise mostly at top/edges
   float noiseAlpha = smoothstep(0.1, 1.7, n) * noiseStrength * distFactor;

   // Final color with noise affecting alpha
   color = vec4(planetColorSolid, 6 * noiseAlpha + 0.4 * (4.0 - noiseStrength));



    return true;
}

// --- main ---
void main(){
    fragColor = texture(DiffuseSampler0, texCoord);
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).r;
    vec3 scenePosVS = screenToLocalSpace(texCoord, sceneDepthSample).xyz;
    float maxRayDist = length(scenePosVS);

    vec3 camera = VeilCamera.CameraPosition + VeilCamera.CameraBobOffset;
    vec3 rd = normalize(viewDirFromUv(texCoord));

    float closestDist = maxRayDist;
    vec4 finalColor = fragColor;
    float finalDepth = sceneDepthSample;

    vec4 planetColor;
    float planetDist;
    vec3 planetHitWorld;

    if(raytracePlanet(camera, rd, PlanetPosition, PlanetRotation, PlanetSize, maxRayDist, planetDist, planetColor, planetHitWorld)){
        if(planetDist<closestDist){
            closestDist = planetDist;
            finalColor.rgb = finalColor.rgb * (1.0 - planetColor.a) + planetColor.rgb * planetColor.a;
            vec4 clipPos = VeilCamera.ProjMat * VeilCamera.ViewMat * vec4(planetHitWorld,1.0);
            finalDepth = clipPos.z/clipPos.w*0.5+0.5;
        }
    }

    vec4 haloColor;
    float haloDist;
    vec3 haloHitWorld;

    fragColor = finalColor;



    gl_FragDepth = finalDepth;
}
