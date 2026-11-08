package com.example.youtubeoverlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class YoutubeOverlayMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        YoutubeKeyBinding.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (YoutubeKeyBinding.youtubeKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new YoutubeScreen());
            }
        });
    }
}

    public static void startVideo(String url) {
        currentUrl = url;
    }
}
