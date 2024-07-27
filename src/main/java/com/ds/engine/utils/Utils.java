package com.ds.engine.utils;

import com.ds.Main;
import com.ds.engine.ui.text.GLFont;
import com.threed.jpct.FrameBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.newdawn.slick.Sound;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public final class Utils {
    public static @Nullable Font getFont(int style, float size, String fontPath){
        try {
            Font font = Font.createFont(Font.PLAIN, Objects.requireNonNull(Main.class.getResourceAsStream(fontPath)));
            return font.deriveFont(style, size);
        }catch (Exception e){
            ErrorHandler.doError(e);
        }

        return null;
    }

    public static void playSound(String path){
        try {
            Sound sound = new Sound(path);
            sound.play();
        }catch (Exception e){
            ErrorHandler.doError(e);
        }
    }

    public static int calculateCenterXForLabel(@NotNull FrameBuffer frameBuffer, @NotNull GLFont glFont, String text){
        return (frameBuffer.getWidth() / 2) - (glFont.getStringBounds(text).width / 2);
    }

    public static boolean getChance(float chance){
        float result = new Random().nextFloat(0f, 1f);
        return result <= chance;
    }
}
