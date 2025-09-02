package dev.nitron.usm.content.registries;

import dev.nitron.usm.USM;
import dev.nitron.usm.content.entity.FighterShipEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AllEntities {
    public static EntityType<FighterShipEntity> FIGHTER_SHIP = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(USM.MOD_ID, "fighter_ship"),
            FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, FighterShipEntity::new).dimensions(EntityDimensions.fixed(1.0F, 1.0F)).trackRangeChunks(128).build()
    );

    public static void init(){
    }
}
