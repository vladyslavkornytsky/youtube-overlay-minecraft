package com.example.youtubeoverlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class YoutubeOverlayMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        YoutubeKeyBinding.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (YoutubeKeyBinding.youtubeKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreenAndShow(new YoutubeScreen());
                }
            }
        });
    }
}
