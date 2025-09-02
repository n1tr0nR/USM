package dev.nitron.usm.content.entity;

import dev.nitron.usm.USM;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class FighterShipEntity extends Entity {
    private float yaw = 0;
    private float pitch = 0;

    public FighterShipEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.yaw = nbt.getFloat("yaw");
        this.pitch = nbt.getFloat("pitch");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("yaw", this.yaw);
        nbt.putFloat("pitch", this.pitch);
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ActionResult actionResult = super.interact(player, hand);
        USM.LOGGER.info("its being called!");
        if (actionResult != ActionResult.PASS) {
            return actionResult;
        } else if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else {
            if (!this.getWorld().isClient) {
                return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
            } else {
                return ActionResult.SUCCESS;
            }
        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return passenger instanceof PlayerEntity;
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        Vec3d offset = getRotatedPassengerOffset();
        passenger.setPosition(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public Vec3d getPassengerRidingPos(Entity passenger) {
        Vec3d offset = getRotatedPassengerOffset();
        return new Vec3d(this.getX(), this.getY(), this.getZ());
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        Vec3d offset = getRotatedPassengerOffset();
        return new Vec3d(this.getX(), this.getY(), this.getZ());
    }

    /**
     * Computes the offset of the passenger based on entity rotation.
     */
    private Vec3d getRotatedPassengerOffset() {
        double yOffset = 1.0;      // height above the entity
        double zOffset = 0.0;      // forward/backward
        double xOffset = 0.0;      // side offset

        // Convert yaw/pitch to radians
        float yawRad = (float) Math.toRadians(this.getYaw());
        float pitchRad = (float) Math.toRadians(this.getPitch());

        // First rotate forward/backward by pitch (around X axis)
        double cosPitch = Math.cos(pitchRad);
        double sinPitch = Math.sin(pitchRad);
        double y1 = yOffset * cosPitch - zOffset * sinPitch;
        double z1 = yOffset * sinPitch + zOffset * cosPitch;

        // Then rotate around Y axis by yaw
        double cosYaw = Math.cos(yawRad);
        double sinYaw = Math.sin(yawRad);
        double x1 = xOffset * cosYaw - z1 * sinYaw;
        double z2 = xOffset * sinYaw + z1 * cosYaw;

        return new Vec3d(x1, y1, z2);
    }



    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
