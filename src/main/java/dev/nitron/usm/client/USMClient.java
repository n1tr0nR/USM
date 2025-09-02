package dev.nitron.usm.client;

import dev.nitron.usm.USM;
import dev.nitron.usm.client.models.FighterShipModel;
import dev.nitron.usm.client.render.entity.FighterShipEntityRenderer;
import dev.nitron.usm.client.shader.GlobalShaderManager;
import dev.nitron.usm.content.registries.AllEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class USMClient implements ClientModInitializer {
    public static final EntityModelLayer FIGHTER_SHIP = new EntityModelLayer(Identifier.of(USM.MOD_ID, "fighter"), "main");

    public static KeyBinding SLOW_DOWN = new KeyBinding(
            "key.usm.slow_down",
            GLFW.GLFW_KEY_R,
            "category.usm"
    );

    @Override
    public void onInitializeClient() {
        GlobalShaderManager.initShaders();

        EntityModelLayerRegistry.registerModelLayer(FIGHTER_SHIP, FighterShipModel::getTexturedModelData);

        EntityRendererRegistry.register(AllEntities.FIGHTER_SHIP, FighterShipEntityRenderer::new);
    }
}
