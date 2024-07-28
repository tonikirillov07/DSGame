package com.ds.dj3d.ui;

import com.ds.Constants;
import com.ds.dj3d.SettingsManager;
import com.ds.dj3d.settings.SettingsConstants;
import com.ds.dj3d.settings.SettingsReader;
import com.ds.dj3d.settings.SettingsWriter;
import com.ds.engine.GameWorld;
import com.ds.engine.Screen;
import com.ds.engine.ui.button.Button;
import com.ds.engine.ui.button.SwitchButton;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.FrameBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

public class SettingsMenu {
    private static final Logger log = LoggerFactory.getLogger(SettingsMenu.class);
    private final FrameBuffer frameBuffer;
    private GLFont settingsTitle;
    private SwitchButton switchButtonVSync, switchButtonFog, switchButtonLimitFPS, switchButtonSounds;
    private com.ds.engine.ui.button.Button buttonBack;
    private String settingsTitleText;
    private final List<String> soundsValues, vSyncValues, fogValues, limitFpsValues;
    private boolean isOpen;
    private final GameWorld gameWorld;
    private final Screen screen;

    public SettingsMenu(FrameBuffer frameBuffer, GameWorld gameWorld, Screen screen) {
        this.frameBuffer = frameBuffer;
        this.gameWorld = gameWorld;
        this.screen = screen;

        soundsValues = List.of("Sounds On", "Sounds Off");
        vSyncValues = List.of("Vsync On", "Vsync Off");
        fogValues = List.of("Fog On", "Fog Off");
        limitFpsValues = List.of("Limit FPS On", "Limit FPS Off");
    }

    public void init(){
        log.info("Initializing settings...");

        isOpen = true;

        settingsTitleText = "Settings";
        GLFont buttonsGLFont = new GLFont(Utils.getFont(Font.BOLD, 18f, Constants.JOYSTIX_MONOSPACE_FONT_PATH), GLFont.ENGLISH);

        settingsTitle = new GLFont(Utils.getFont(Font.BOLD, 27f, Constants.JOYSTIX_MONOSPACE_FONT_PATH), GLFont.ENGLISH);

        initSwitchButtonsFirstRow(buttonsGLFont);
        initSwitchButtonsSecondRow(buttonsGLFont);
        setInitialValues();
    }

    private void onBack(){
        isOpen = false;
    }

    private void setInitialValues() {
        log.info("Initializing initial values for buttons...");

        boolean vsyncValue = Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_VSYNC_KEY));
        boolean fogValue = Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_FOG_KEY));
        boolean limitFpsValue = Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.LIMIT_FPS_KEY));
        boolean soundsValue = Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_SOUNDS_KEY));

        switchButtonVSync.setValue(Utils.convertBooleanToIndexForSwitchButton(vsyncValue));
        switchButtonFog.setValue(Utils.convertBooleanToIndexForSwitchButton(fogValue));
        switchButtonLimitFPS.setValue(Utils.convertBooleanToIndexForSwitchButton(limitFpsValue));
        switchButtonSounds.setValue(Utils.convertBooleanToIndexForSwitchButton(soundsValue));
    }

    private void updateSettings(){
        SettingsManager.updateGameSettings(gameWorld, screen);
    }

    private void initSwitchButtonsSecondRow(GLFont buttonsGLFont) {
        switchButtonSounds = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, soundsValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, soundsValues);
        switchButtonSounds.setX(switchButtonSounds.calculateScreenCenterX() + 200);
        switchButtonSounds.setY((frameBuffer.getHeight() / 2) - 200);
        switchButtonSounds.setOnAction(() -> {
            SettingsWriter.writeValue(SettingsConstants.USE_SOUNDS_KEY, String.valueOf(Utils.convertIndexToBooleanForSwitchButton(switchButtonSounds.getCurrentValueIndex())));
            updateSettings();
        });

        buttonBack = new Button(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, "Back",
                Constants.BUTTON_RED_BACKGROUND_TEXTURE, frameBuffer);
        buttonBack.setX(buttonBack.calculateScreenCenterX() + 200);
        buttonBack.setY((frameBuffer.getHeight() / 2) - 100);
        buttonBack.setOnAction(this::onBack);
    }

    private void initSwitchButtonsFirstRow(GLFont buttonsGLFont){
        switchButtonVSync = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, vSyncValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, vSyncValues);
        switchButtonVSync.setX(switchButtonVSync.calculateScreenCenterX() - 200);
        switchButtonVSync.setY((frameBuffer.getHeight() / 2) - 200);
        switchButtonVSync.setOnAction(() -> {
            SettingsWriter.writeValue(SettingsConstants.USE_VSYNC_KEY, String.valueOf(Utils.convertIndexToBooleanForSwitchButton(switchButtonVSync.getCurrentValueIndex())));
            updateSettings();
        });

        switchButtonFog = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, fogValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, fogValues);
        switchButtonFog.setX(switchButtonFog.calculateScreenCenterX() - 200);
        switchButtonFog.setY((frameBuffer.getHeight() / 2) - 100);
        switchButtonFog.setOnAction(() -> {
            SettingsWriter.writeValue(SettingsConstants.USE_FOG_KEY, String.valueOf(Utils.convertIndexToBooleanForSwitchButton(switchButtonFog.getCurrentValueIndex())));
            updateSettings();
        });

        switchButtonLimitFPS = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, limitFpsValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, limitFpsValues);
        switchButtonLimitFPS.setX(switchButtonLimitFPS.calculateScreenCenterX() - 200);
        switchButtonLimitFPS.setY((frameBuffer.getHeight() / 2));
        switchButtonLimitFPS.setOnAction(() -> {
            SettingsWriter.writeValue(SettingsConstants.LIMIT_FPS_KEY, String.valueOf(Utils.convertIndexToBooleanForSwitchButton(switchButtonLimitFPS.getCurrentValueIndex())));
            updateSettings();
        });
    }

    public void update(){
        settingsTitle.blitString(frameBuffer, settingsTitleText, Utils.calculateCenterXForLabel(frameBuffer, settingsTitle, settingsTitleText), 90, 1, Color.WHITE);

        switchButtonVSync.update();
        switchButtonFog.update();
        switchButtonLimitFPS.update();
        switchButtonSounds.update();
        buttonBack.update();
    }

    public boolean isOpen() {
        return isOpen;
    }
}
