package dev.nitron.usm.client;

import dev.nitron.usm.client.shader.GlobalShaderManager;
import net.fabricmc.api.ClientModInitializer;

public class USMClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GlobalShaderManager.initShaders();
    }
}
