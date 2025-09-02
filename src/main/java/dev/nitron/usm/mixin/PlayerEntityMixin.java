package dev.nitron.usm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.nitron.usm.USM;
import dev.nitron.usm.content.cca.SpacePlayerComponent;
import dev.nitron.usm.content.cca.USMEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void usm$travel(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        SpacePlayerComponent component = USMEntityComponents.PLAYER.get(player);
        if(!USM.shouldBeInZeroG(player)) return;
        float yawDeg   = player.getYaw();
        float pitchDeg = player.getPitch();

        Vector3f axis = new Vector3f((float) movementInput.x, (float) movementInput.y, (float) movementInput.z);

        if (jumping)      axis.y += 1.0f;
        if (isSneaking()) axis.y -= 1.0f;

        if (axis.lengthSquared() > 1.0e-4f) {
            axis.normalize();

            float yawRad   = (float) Math.toRadians(-yawDeg);
            float pitchRad = (float) Math.toRadians(pitchDeg);

            Quaternionf q = new Quaternionf()
                    .rotateY(yawRad)
                    .rotateX(pitchRad);

            axis.rotate(q);
            Vec3d rotatedMovement = new Vec3d(axis.x, axis.y, axis.z).multiply(player.getMovementSpeed()).multiply(0.1F);
            player.addVelocity(rotatedMovement.x, rotatedMovement.y, rotatedMovement.z);
        }

        player.move(MovementType.SELF, player.getVelocity());
        //player.setVelocity(player.getVelocity().multiply(0.975));
        player.velocityDirty = true;
        ci.cancel();
    }

    @ModifyExpressionValue(method = "updatePose", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSwimming()Z"))
    private boolean crawl(boolean original){
        PlayerEntity player = (PlayerEntity) (Object) this;
        SpacePlayerComponent component = USMEntityComponents.PLAYER.get(player);
        if(!USM.shouldBeInZeroG(player)) return original;
        return true;
    }
}

