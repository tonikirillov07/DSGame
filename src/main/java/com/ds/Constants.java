package com.ds;

import com.threed.jpct.Texture;

public final class Constants {
    public static final int START_WIDTH = 800;
    public static final int START_HEIGHT = 600;
    public static final int PREFER_WIDTH = 1200;
    public static final int PREFER_HEIGHT = 800;
    public static final int TARGET_FPS = 60;
    public static final float PLATFORM_DELETE_DISTANCE = 90;
    public static final String TITLE = "Doodle Jump 3D";
    public static final String DEVELOPER = "Darkness";
    public static final String ARCADE_CLASSIC_FONT_PATH = "/fonts/ARCADECLASSIC.TTF";
    public static final String ROBOTO_BOLD_FONT_PATH = "/fonts/Roboto-Bold.ttf";
    public static final float GAME_SPACE_START_X = -20f;
    public static final float GAME_SPACE_END_X = 40f;

    public static final Texture BUTTON_DEFAULT_BACKGROUND_TEXTURE = new Texture(Main.class.getResourceAsStream("/textures/ui/ButtonBackground.png"));
    public static final Texture BUTTON_RED_BACKGROUND_TEXTURE = new Texture(Main.class.getResourceAsStream("/textures/ui/ButtonBackground2.png"));
}
