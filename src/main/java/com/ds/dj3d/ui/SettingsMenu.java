package com.ds.dj3d.ui;

import com.ds.Constants;
import com.ds.engine.ui.button.Button;
import com.ds.engine.ui.button.SwitchButton;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.FrameBuffer;

import java.awt.*;
import java.util.List;

public class SettingsMenu {
    private final FrameBuffer frameBuffer;
    private GLFont settingsTitle;
    private SwitchButton switchButtonVSync, switchButtonFog, switchButtonLimitFPS, switchButtonRender, switchButtonSounds;
    private com.ds.engine.ui.button.Button buttonBack;
    private String settingsTitleText;
    private boolean isOpen;

    public SettingsMenu(FrameBuffer frameBuffer) {
        this.frameBuffer = frameBuffer;
    }

    public void init(){
        isOpen = true;

        settingsTitleText = "Settings";
        GLFont buttonsGLFont = new GLFont(Utils.getFont(Font.BOLD, 24f, Constants.ARCADE_CLASSIC_FONT_PATH), GLFont.ENGLISH);

        settingsTitle = new GLFont(Utils.getFont(Font.BOLD, 35f, Constants.ARCADE_CLASSIC_FONT_PATH), GLFont.ENGLISH);

        initSwitchButtonsFirstRow(buttonsGLFont);
        initSwitchButtonsSecondRow(buttonsGLFont);
    }

    private void initSwitchButtonsSecondRow(GLFont buttonsGLFont) {
        List<String> renderValues = List.of("Render: Open GL", "Render: Software");
        List<String> soundsValues = List.of("Sounds: On", "Sounds: Off");

        switchButtonRender = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, renderValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, renderValues);
        switchButtonRender.setX(switchButtonRender.calculateScreenCenterX() + 200);
        switchButtonRender.setY((frameBuffer.getHeight() / 2) - 200);

        switchButtonSounds = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, soundsValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, soundsValues);
        switchButtonSounds.setX(switchButtonSounds.calculateScreenCenterX() + 200);
        switchButtonSounds.setY((frameBuffer.getHeight() / 2) - 100);

        buttonBack = new Button(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, "Back",
                Constants.BUTTON_RED_BACKGROUND_TEXTURE, frameBuffer);
        buttonBack.setX(buttonBack.calculateScreenCenterX() + 200);
        buttonBack.setY((frameBuffer.getHeight() / 2));
        buttonBack.setOnAction(() -> isOpen = false);
    }

    private void initSwitchButtonsFirstRow(GLFont buttonsGLFont){
        List<String> vSyncValues = List.of("Vsync: On", "Vsync: Off");
        List<String> fogValues = List.of("Fog: On", "Fog: Off");
        List<String> limitFpsValues = List.of("Limit FPS: On", "Limit FPS: Off");

        switchButtonVSync = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, vSyncValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, vSyncValues);
        switchButtonVSync.setX(switchButtonVSync.calculateScreenCenterX() - 200);
        switchButtonVSync.setY((frameBuffer.getHeight() / 2) - 200);

        switchButtonFog = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, fogValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, fogValues);
        switchButtonFog.setX(switchButtonFog.calculateScreenCenterX() - 200);
        switchButtonFog.setY((frameBuffer.getHeight() / 2) - 100);

        switchButtonLimitFPS = new SwitchButton(SwitchButton.DEFAULT_WIDTH, SwitchButton.DEFAULT_HEIGHT, buttonsGLFont, limitFpsValues.get(0),
                Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer, limitFpsValues);
        switchButtonLimitFPS.setX(switchButtonLimitFPS.calculateScreenCenterX() - 200);
        switchButtonLimitFPS.setY((frameBuffer.getHeight() / 2));
    }

    public void update(){
        settingsTitle.blitString(frameBuffer, settingsTitleText, Utils.calculateCenterXForLabel(frameBuffer, settingsTitle, settingsTitleText), 90, 1, Color.WHITE);

        switchButtonVSync.update();
        switchButtonFog.update();
        switchButtonLimitFPS.update();
        switchButtonRender.update();
        switchButtonSounds.update();
        buttonBack.update();
    }

    public boolean isOpen() {
        return isOpen;
    }
}
