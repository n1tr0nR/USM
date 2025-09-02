package dev.nitron.usm;

import dev.nitron.usm.content.registries.AllEntities;
import dev.nitron.usm.saves.USMAllSystems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class USM implements ModInitializer {
	public static final String MOD_ID = "usm";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Do not go gentle into that good night.");
		USMAllSystems.init();
		AllEntities.init();
	}

	public static boolean shouldBeInZeroG(PlayerEntity player){
		return true;
	}
}