package com.example.youtubeoverlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.dimaskama.mcef.api.MCEFApi;
import net.dimaskama.mcef.api.MCEFBrowser;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import org.cef.browser.CefBrowser;
import org.joml.Matrix3x2f;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class YoutubeBrowser {
    private static MCEFBrowser browser;
    private static CefBrowser cef;
    private static boolean cefSearched = false;

    public static int browserW = 960;
    public static int browserH = 540;

    private static final String AD_JS = """
        (function(){
          if(!document.getElementById('yo-style')){
            var s=document.createElement('style'); s.id='yo-style';
            s.textContent='#player-ads,#masthead-ad,ytd-ad-slot-renderer,ytd-display-ad-renderer,ytd-promoted-sparkles-web-renderer,ytd-in-feed-ad-layout-renderer,.ytp-ad-overlay-container,.ytp-ad-image-overlay{display:none!important}';
            document.head.appendChild(s);
          }
          var p=document.querySelector('.html5-video-player');
          var v=document.querySelector('video.html5-main-video')||document.querySelector('video');
          if(p&&v&&p.classList.contains('ad-showing')){
            if(isFinite(v.duration)&&v.duration>0){v.currentTime=v.duration;}
          }
          var b=document.querySelector('.ytp-skip-ad-button,.ytp-ad-skip-button,.ytp-ad-skip-button-modern,.ytp-ad-skip-button-container button');
          if(b){b.click();}
        })();
        """;

    private YoutubeBrowser() {}

    public static MCEFBrowser get() {
        if (browser == null) {
            browser = MCEFApi.getInstance().createBrowser("https://www.youtube.com", false);
            browser.resize(browserW, browserH);
            cef = null;
            cefSearched = false;
        }
        return browser;
    }

    public static MCEFBrowser peek() {
        return browser;
    }

    public static void resize(int w, int h) {
        browserW = w;
        browserH = h;
        if (browser != null) {
            browser.resize(w, h);
        }
    }

    public static void close() {
        if (browser != null) {
            browser.close();
            browser = null;
            cef = null;
            cefSearched = false;
        }
    }

    // Ищем внутри MCEFBrowser объект JCEF (CefBrowser), не зная названия метода.
    private static CefBrowser findCef() {
        if (cefSearched || browser == null) {
            return cef;
        }
        cefSearched = true;
        Object o = browser;
        if (o instanceof CefBrowser direct) {
            cef = direct;
            return cef;
        }
        for (Method m : o.getClass().getMethods()) {
            if (m.getParameterCount() == 0 && CefBrowser.class.isAssignableFrom(m.getReturnType())) {
                try {
                    Object r = m.invoke(o);
                    if (r instanceof CefBrowser c) {
                        cef = c;
                        return cef;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        Class<?> k = o.getClass();
        while (k != null && k != Object.class) {
            for (Field f : k.getDeclaredFields()) {
                if (CefBrowser.class.isAssignableFrom(f.getType())) {
                    try {
                        f.setAccessible(true);
                        Object r = f.get(o);
                        if (r instanceof CefBrowser c) {
                            cef = c;
                            return cef;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            k = k.getSuperclass();
        }
        System.err.println("[youtube-overlay] CefBrowser not found inside MCEFBrowser; ad skipper disabled");
        return null;
    }

    public static void runAdSkipper() {
        CefBrowser c = findCef();
        if (c != null) {
            try {
                c.executeJavaScript(AD_JS, "https://www.youtube.com", 0);
            } catch (Throwable t) {
                System.err.println("[youtube-overlay] executeJavaScript failed: " + t);
            }
        }
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        if (browser == null) {
            return;
        }
        GpuTextureView view = browser.getTextureView();
        if (view == null) {
            return;
        }
        g.guiRenderState.addGuiElement(new BlitRenderState(
                RenderPipelines.GUI_TEXTURED,
                TextureSetup.singleTexture(view, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)),
                new Matrix3x2f(g.pose()),
                x, y, x + w, y + h,
                0.0F, 1.0F, 0.0F, 1.0F,
                0xFFFFFFFF,
                g.scissorStack.peek()
        ));
    }
}
