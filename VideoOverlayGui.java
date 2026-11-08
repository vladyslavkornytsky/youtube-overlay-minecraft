package com.example.youtubeoverlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class VideoOverlayGui extends Screen {
    private final MinecraftClient client;
    private boolean isMinimized = false;

    private static final int FULL_WIDTH = 400;
    private static final int FULL_HEIGHT = 225;
    private static final int MINIMIZED_WIDTH = 160;
    private static final int MINIMIZED_HEIGHT = 90;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public VideoOverlayGui(MinecraftClient client) {
        super(Text.translatable("screen.youtubeoverlay.overlay"));
        this.client = client;
    }

    @Override
    protected void init() {
        super.init();
        rebuildWidgets();
    }

    private void rebuildWidgets() {
        this.clearChildren();

        int startX = this.width - (isMinimized ? MINIMIZED_WIDTH : FULL_WIDTH) - 10;
        int startY = 10;

        if (!isMinimized) {
            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("_"),
                    (btn) -> {
                        isMinimized = true;
                        rebuildWidgets();
                    }
            ).dimensions(startX + FULL_WIDTH - 40, startY, 40, 20).build());

            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("X"),
                    (btn) -> {
                        client.setScreen(null);
                        executor.shutdown();
                    }
            ).dimensions(startX + FULL_WIDTH - 85, startY, 40, 20).build());
        } else {
            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("□"),
                    (btn) -> {
                        isMinimized = false;
                        rebuildWidgets();
                    }
            ).dimensions(startX + MINIMIZED_WIDTH - 25, startY, 20, 20).build());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isMinimized) {
            this.renderBackground(context, mouseX, mouseY, delta);
        }

        int w = isMinimized ? MINIMIZED_WIDTH : FULL_WIDTH;
        int h = isMinimized ? MINIMIZED_HEIGHT : FULL_HEIGHT;
        int x = this.width - w - 10;
        int y = 10;

        context.fill(x, y, x + w, y + h, 0xDD101010);
        context.drawBorder(x, y, w, h, 0xFF555555);

        if (!isMinimized) {
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    Text.literal("YouTube Player"),
                    x + w / 2, y + 40,
                    0xFFFFFFFF
            );
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    Text.literal("Ссылка: " + YoutubeOverlayMod.currentUrl),
                    x + w / 2, y + 70,
                    0xFFAAAAAA
            );
        } else {
            context.drawText(
                    this.textRenderer,
                    Text.literal("▶ YouTube (Mini)"),
                    x + 10, y + 15,
                    0xFFFFFFFF,
                    true
            );
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
