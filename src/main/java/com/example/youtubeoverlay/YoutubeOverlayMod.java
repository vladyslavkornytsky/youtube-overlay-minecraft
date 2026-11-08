package com.example.youtubeoverlay;

import net.dimaskama.mcef.api.MCEFApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

public class YoutubeOverlayMod implements ClientModInitializer {
    private static int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        MCEFApi.initialize();
        YoutubeKeyBinding.register();

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath("youtube-overlay", "corner"),
                (graphics, deltaTracker) -> {
                    int w = 320;
                    int h = w * YoutubeBrowser.browserH / Math.max(1, YoutubeBrowser.browserW);
                    YoutubeBrowser.draw(graphics, 6, 6, w, h);
                });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (YoutubeKeyBinding.youtubeKey.consumeClick()) {
                client.setScreenAndShow(new YoutubeScreen());
            }
            while (YoutubeKeyBinding.closeKey.consumeClick()) {
                YoutubeBrowser.close();
            }
            if (++tickCounter >= 10) {
                tickCounter = 0;
                YoutubeBrowser.runAdSkipper();
            }
        });
    }
}
