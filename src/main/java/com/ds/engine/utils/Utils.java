package com.ds.engine.utils;

import com.ds.Main;
import com.ds.dj3d.settings.SettingsConstants;
import com.ds.dj3d.settings.SettingsReader;
import com.ds.engine.ui.text.GLFont;
import com.threed.jpct.FrameBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.newdawn.slick.Sound;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Random;

public final class Utils {
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

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
            if(Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_SOUNDS_KEY))){
                Sound sound = new Sound(path);
                sound.play();
            }
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

    public static float roundNumber(float number){
        return Float.parseFloat(decimalFormat.format(number));
    }

    public static boolean convertIndexToBooleanForSwitchButton(int index){
        return index == 1;
    }

    public static int convertBooleanToIndexForSwitchButton(boolean value){
        return value ? 0 : 1;
    }
}
