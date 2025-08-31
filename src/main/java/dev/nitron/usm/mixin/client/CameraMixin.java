package dev.nitron.usm.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.nitron.usm.content.cca.SpacePlayerComponent;
import dev.nitron.usm.content.cca.USMEntityComponents;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private Entity focusedEntity;

    @Shadow private float lastTickDelta;

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void usm$changeRotation(Camera instance, float yaw, float pitch, Operation<Void> original){
        /*if (yaw == this.focusedEntity.getYaw(this.lastTickDelta) && yaw == this.focusedEntity.getPitch(this.lastTickDelta)){
            if (this.focusedEntity instanceof PlayerEntity player){
                SpacePlayerComponent component = USMEntityComponents.PLAYER.get(player);
                original.call(instance, -component.getPlayerYaw(), component.getPlayerPitch());
                return;
            }
        }*/
        original.call(instance, yaw, pitch);
    }
}
