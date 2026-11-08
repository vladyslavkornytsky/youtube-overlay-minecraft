package com.example.youtubeoverlay;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YoutubeScreen extends Screen {
    public YoutubeScreen() {
        super(Component.literal("YouTube Overlay"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.fill(50, 50, this.width - 50, this.height - 50, 0xAA000000);
    }
}
