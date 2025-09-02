package dev.nitron.usm.client.shader;

import dev.nitron.usm.USM;
import dev.nitron.usm.content.entity.FighterShipEntity;
import dev.nitron.usm.data.SSystem;
import dev.nitron.usm.saves.USMAllSystems;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import foundry.veil.api.client.render.shader.ShaderManager;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.platform.VeilEventPlatform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GlobalShaderManager {
    public static void initShaders(){
        VeilEventPlatform.INSTANCE.onVeilRenderLevelStage((stage, worldRenderer, immediate, matrixStack, matrix4fc, matrix4fc1, i, renderTickCounter, camera, frustum) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientWorld world = client.world;
            float time = MathHelper.lerp(renderTickCounter.getTickDelta(false), (world.getTimeOfDay() - 1) % 24000, world.getTimeOfDay() % 24000); //Lerp for smoothed time
            PostProcessingManager manager = VeilRenderSystem.renderer().getPostProcessingManager();
            if (stage == VeilRenderLevelStageEvent.Stage.AFTER_SKY){
                renderSpaceSkybox(manager, time, camera);
            }
            if (stage == VeilRenderLevelStageEvent.Stage.AFTER_LEVEL){
                renderThrusters(manager, time, client);
            }
        });
    }

    private static void renderSpaceSkybox(PostProcessingManager manager, float time, Camera camera){
        PostPipeline space = manager.getPipeline(Identifier.of(USM.MOD_ID, "space"));
        if (space != null){
            space.getOrCreateUniform("Position").setVector(camera.getPos().toVector3f());
            space.getOrCreateUniform("Radius").setFloat(100);
            space.getOrCreateUniform("Time").setFloat(time);

            //SpaceGasSettings
            //TODO: Make a system swapping system, currently hardcoded to Astrya
            SSystem currentSystem = USMAllSystems.ASTRYA;
            space.getOrCreateUniform("Density").setFloat(currentSystem.getStarDensity());
            space.getOrCreateUniform("Power").setFloat(currentSystem.getSpaceGasPower());
            space.getOrCreateUniform("Color").setVector(new Vector3f((float) currentSystem.getSpaceGasColor().getRed() / 255, (float) currentSystem.getSpaceGasColor().getGreen() / 255, (float) currentSystem.getSpaceGasColor().getBlue() / 255));

            manager.runPipeline(space);
        } else {
            USM.LOGGER.error("PostPipeline: 'space' is null.");
        }
    }

    private static void renderThrusters(PostProcessingManager manager, float time, MinecraftClient client){
        PostPipeline thruster = manager.getPipeline(Identifier.of(USM.MOD_ID, "thruster"));
        if (thruster != null){
            ClientWorld world = client.world;
            for (Entity entity : world.getEntities()){
                if (entity instanceof FighterShipEntity fighterShipEntity){
                    //Main thruster

                    float power = 1;

                    thruster.getOrCreateUniform("PlanetPosition").setVector(fighterShipEntity.getPos().toVector3f().add(0, 1.125F, -3.1F));
                    thruster.getOrCreateUniform("PlanetRotation").setVector((float) Math.toRadians(90F), 0, 0);
                    thruster.getOrCreateUniform("PlanetSize").setVector(0.3F, 0.3F, 0.3F);
                    thruster.getOrCreateUniform("Time").setFloat(time);

                    manager.runPipeline(thruster);

                    thruster.getOrCreateUniform("PlanetPosition").setVector(fighterShipEntity.getPos().toVector3f().add(0, 1.125F, -3.6F));
                    thruster.getOrCreateUniform("PlanetRotation").setVector((float) Math.toRadians(90F), 0, 0);
                    thruster.getOrCreateUniform("PlanetSize").setVector(0.2F, 0.8F, 0.2F);
                    thruster.getOrCreateUniform("Time").setFloat(time);

                    manager.runPipeline(thruster);

                    //Left thruster

                    thruster.getOrCreateUniform("PlanetPosition").setVector(fighterShipEntity.getPos().toVector3f().add(0.925F, 0.8F, -2.1F));
                    thruster.getOrCreateUniform("PlanetRotation").setVector((float) Math.toRadians(90F), 0, 0);
                    thruster.getOrCreateUniform("PlanetSize").setVector(0.15F, 0.3F, 0.3F);
                    thruster.getOrCreateUniform("Time").setFloat(time + 60);

                    manager.runPipeline(thruster);

                    thruster.getOrCreateUniform("PlanetPosition").setVector(fighterShipEntity.getPos().toVector3f().add(0.925F, 0.8F, -2.5F));
                    thruster.getOrCreateUniform("PlanetRotation").setVector((float) Math.toRadians(90F), 0, 0);
                    thruster.getOrCreateUniform("PlanetSize").setVector(0.1F, 0.8F, 0.2F);
                    thruster.getOrCreateUniform("Time").setFloat(time + 60 );

                    manager.runPipeline(thruster);

                    //Right thruster

                    thruster.getOrCreateUniform("PlanetPosition").setVector(fighterShipEntity.getPos().toVector3f().add(-0.925F, 0.8F, -2.1F));
                    thruster.getOrCreateUniform("PlanetRotation").setVector((float) Math.toRadians(90F), 0, 0);
                    thruster.getOrCreateUniform("PlanetSize").setVector(0.15F, 0.3F, 0.3F);
                    thruster.getOrCreateUniform("Time").setFloat(time + 20);

                    manager.runPipeline(thruster);

                    thruster.getOrCreateUniform("PlanetPosition").setVector(fighterShipEntity.getPos().toVector3f().add(-0.925F, 0.8F, -2.5F));
                    thruster.getOrCreateUniform("PlanetRotation").setVector((float) Math.toRadians(90F), 0, 0);
                    thruster.getOrCreateUniform("PlanetSize").setVector(0.1F, 0.8F, 0.2F);
                    thruster.getOrCreateUniform("Time").setFloat(time + 20);

                    manager.runPipeline(thruster);
                }
            }
        } else {
            USM.LOGGER.error("PostPipeline: 'thruster' is null.");
        }
    }

    public static Vec3d lerp(Vec3d a, Vec3d b, double t) {
        return a.add(b.subtract(a).multiply(t));
    }
}
