package dev.nitron.usm.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.nitron.usm.USM;
import dev.nitron.usm.content.cca.SpacePlayerComponent;
import dev.nitron.usm.content.cca.USMEntityComponents;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow @Final private Quaternionf rotation;

    @Inject(method = "update", at = @At("TAIL"))
    private void applyRoll(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (focusedEntity instanceof PlayerEntity player){
            if (!USM.shouldBeInZeroG(player)) return;
            SpacePlayerComponent component = USMEntityComponents.PLAYER.get(player);
            float rollDegrees = -component.getPlayerRoll();
            float rollRadians = (float) Math.toRadians(rollDegrees);

            Quaternionf rollQuat = new Quaternionf().rotationZ(rollRadians);
            this.rotation.mul(rollQuat);
        }
    }

}
