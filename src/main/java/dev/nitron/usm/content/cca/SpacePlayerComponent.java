package dev.nitron.usm.content.cca;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class SpacePlayerComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final PlayerEntity player;

    private float playerYaw = 0;
    private float playerPitch = 0;
    private float playerRoll = 0;

    public SpacePlayerComponent(PlayerEntity player) {
        this.player = player;
    }
    public void sync(){
        USMEntityComponents.PLAYER.sync(this.player);
    }

    @Override
    public void tick() {
        if (this.player.getWorld().isClient){
            this.player.sendMessage(Text.literal("Roll: " + this.getPlayerRoll()), true);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.playerYaw = nbtCompound.getFloat("playerYaw");
        this.playerPitch = nbtCompound.getFloat("playerPitch");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putFloat("playerYaw", this.playerYaw);
        nbtCompound.putFloat("playerPitch", this.playerPitch);
    }

    public float getPlayerYaw() {
        return playerYaw;
    }

    public void setPlayerYaw(float playerYaw) {
        this.playerYaw = playerYaw;
        this.sync();
    }

    public float getPlayerPitch() {
        return playerPitch;
    }

    public void setPlayerPitch(float playerPitch) {
        this.playerPitch = playerPitch;
        this.sync();
    }

    public float getPlayerRoll() {
        return (playerRoll % 360 + 360) % 360;
    }

    public void setPlayerRoll(float playerRoll) {
        this.playerRoll = playerRoll;
        this.sync();
    }
}
