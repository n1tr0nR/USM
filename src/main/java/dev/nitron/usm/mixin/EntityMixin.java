package dev.nitron.usm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.nitron.usm.content.cca.SpacePlayerComponent;
import dev.nitron.usm.content.cca.USMEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyReturnValue(method = "getYaw()F", at = @At("RETURN"))
    private float getYaw(float original){
        Entity current = (Entity) (Object) this;
        if (current instanceof PlayerEntity player) {
            SpacePlayerComponent component = USMEntityComponents.PLAYER.get(player);
            return  component.getPlayerYaw();
        }
        return original;
    }

    @ModifyReturnValue(method = "getPitch()F", at = @At("RETURN"))
    private float getPitch(float original){
        Entity current = (Entity) (Object) this;
        if (current instanceof PlayerEntity player) {
            SpacePlayerComponent component = USMEntityComponents.PLAYER.get(player);
            return  component.getPlayerPitch();
        }
        return original;
    }
}
