package dev.nitron.usm.content.cca;

import dev.nitron.usm.USM;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class USMEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<SpacePlayerComponent> PLAYER = org.ladysnake.cca.api.v3.component.ComponentRegistry.getOrCreate(Identifier.of(USM.MOD_ID, "player"),
            SpacePlayerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(PLAYER, SpacePlayerComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
