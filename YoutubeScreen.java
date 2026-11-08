package com.example.youtubeoverlay;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class YoutubeScreen extends Screen {
    public YoutubeScreen() {
        super(Text.literal("YouTube Overlay"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.fill(50, 50, this.width - 50, this.height - 50, 0xAA000000);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 70, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Оверлей YouTube активен"), this.width / 2, this.height / 2, 0xFF0000);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
