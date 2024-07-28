package com.ds.dj3d;

import com.ds.dj3d.settings.SettingsConstants;
import com.ds.dj3d.settings.SettingsReader;
import com.ds.engine.GameWorld;
import com.ds.engine.Screen;
import com.threed.jpct.World;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;

public final class SettingsManager {
    public static void updateGameSettings(@NotNull GameWorld gameWorld, @NotNull Screen screen){
        boolean vsyncValue = Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_VSYNC_KEY));
        boolean fogValue = Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_FOG_KEY));

        Display.setVSyncEnabled(vsyncValue);
        gameWorld.setFogging(fogValue ? World.FOGGING_ENABLED : World.FOGGING_DISABLED);
        screen.setLimitFps(Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.LIMIT_FPS_KEY)));
    }
}
