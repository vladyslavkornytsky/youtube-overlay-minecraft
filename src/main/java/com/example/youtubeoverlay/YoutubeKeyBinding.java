package com.example.youtubeoverlay;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class YoutubeKeyBinding {
    public static KeyMapping youtubeKey;
    public static KeyMapping closeKey;

    public static void register() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("youtube-overlay", "main"));

        youtubeKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.youtube-overlay.open",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                category));

        closeKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.youtube-overlay.close",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                category));
    }
}
