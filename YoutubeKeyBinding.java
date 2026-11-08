package com.example.youtubeoverlay;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class YoutubeKeyBinding {
    public static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.youtubeoverlay.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "category.youtubeoverlay"
        ));
    }
}
