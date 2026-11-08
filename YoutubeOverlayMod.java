package com.example.youtubeoverlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class YoutubeOverlayMod implements ClientModInitializer {
    public static String currentUrl = "";

    @Override
    public void onInitializeClient() {
        YoutubeKeyBinding.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (YoutubeKeyBinding.toggleKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new YoutubeScreen(client));
                } else if (client.currentScreen instanceof VideoOverlayGui) {
                    client.setScreen(null);
                }
            }
        });
    }

    public static void startVideo(String url) {
        currentUrl = url;
    }
}
