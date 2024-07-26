package com.ds.engine.utils;

import com.ds.Main;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;

public final class Utils {
    public static @Nullable Font getGameFont(int style, float size, String fontPath){
        try {
            Font font = Font.createFont(Font.PLAIN, Objects.requireNonNull(Main.class.getResourceAsStream(fontPath)));
            return font.deriveFont(style, size);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
