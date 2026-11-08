package com.example.youtubeoverlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class YoutubeScreen extends Screen {
    private final MinecraftClient client;
    private TextFieldWidget inputField;

    public YoutubeScreen(MinecraftClient client) {
        super(Text.translatable("screen.youtubeoverlay.input"));
        this.client = client;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.inputField = new TextFieldWidget(
                this.textRenderer, centerX - 125, centerY - 30,
                250, 20, Text.literal("URL")
        );
        this.inputField.setMaxLength(500);
        this.inputField.setText("https://www.youtube.com/watch?v=");
        this.addDrawableChild(this.inputField);

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("button.youtubeoverlay.load"),
                (btn) -> {
                    String url = inputField.getText();
                    if (url != null && !url.isEmpty()) {
                        YoutubeOverlayMod.startVideo(url);
                        client.setScreen(new VideoOverlayGui(client));
                    }
                }
        ).dimensions(centerX - 125, centerY, 120, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("button.youtubeoverlay.close"),
                (btn) -> client.setScreen(null)
        ).dimensions(centerX + 5, centerY, 120, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 60, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
