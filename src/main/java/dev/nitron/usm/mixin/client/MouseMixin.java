package dev.nitron.usm.mixin.client;

import dev.nitron.usm.content.cca.SpacePlayerComponent;
import dev.nitron.usm.content.cca.USMEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow private double cursorDeltaX;
    @Shadow private double cursorDeltaY;

    // store "velocity" so rotation eases into changes
    @Unique
    private float smoothYawVelocity = 0f;
    @Unique
    private float smoothPitchVelocity = 0f;

    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
    private void usm$updateMouse(double timeDelta, CallbackInfo ci){
        if (this.client.player == null) return;
        SpacePlayerComponent component = USMEntityComponents.PLAYER.get(this.client.player);

        double sens = this.client.options.getMouseSensitivity().getValue() * 0.6 + 0.2;
        double mult = sens * sens * sens * 2.0;
        float deltaYaw = (float)(this.cursorDeltaX * mult);
        float deltaPitch = (float)(this.cursorDeltaY * mult);

        smoothYawVelocity += deltaYaw;
        smoothPitchVelocity += deltaPitch;

        float damping = 0.95f;
        smoothYawVelocity *= damping;
        smoothPitchVelocity *= damping;

        float newYaw = component.getPlayerYaw() + smoothYawVelocity * (float)timeDelta;
        float newPitch = component.getPlayerPitch() + smoothPitchVelocity * (float)timeDelta;

        /*newYaw = (newYaw % 360 + 360) % 360;
        newPitch = (newPitch % 360 + 360) % 360;*/

        component.setPlayerYaw(newYaw);
        component.setPlayerPitch(newPitch);

        this.cursorDeltaX = 0;
        this.cursorDeltaY = 0;

        ci.cancel();
    }
}

