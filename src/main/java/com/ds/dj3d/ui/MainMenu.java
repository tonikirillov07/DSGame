package com.ds.dj3d.ui;

import com.ds.Constants;
import com.ds.dj3d.Game;
import com.ds.engine.ui.button.Button;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.FrameBuffer;
import org.lwjgl.input.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class MainMenu {
    private static final Logger log = LoggerFactory.getLogger(MainMenu.class);
    private final FrameBuffer frameBuffer;
    private GLFont titleFont, developerFont;
    private Button buttonPlay, buttonSettings, buttonExit;
    private final Game game;
    private final String titleText, developerText;
    private boolean isOpen;
    private SettingsMenu settingsMenu;

    public MainMenu(FrameBuffer frameBuffer, Game game) {
        this.frameBuffer = frameBuffer;
        this.game = game;

        developerText = Constants.DEVELOPER;
        titleText = Constants.TITLE.toUpperCase();
    }

    public void init(){
        isOpen = true;

        titleFont = new GLFont(Utils.getFont(Font.BOLD, 100f, Constants.ARCADE_CLASSIC_FONT_PATH));
        developerFont = new GLFont(Utils.getFont(Font.BOLD, 30f, Constants.ROBOTO_BOLD_FONT_PATH));
        settingsMenu = new SettingsMenu(frameBuffer, game.getGameWorld(), game.getScreen());

        Mouse.setGrabbed(false);

        createButtons();
    }

    private void createButtons() {
        log.info("Creating main menu buttons...");

        GLFont buttonGLFont = new GLFont(Utils.getFont(Font.BOLD, 25f, Constants.ARCADE_CLASSIC_FONT_PATH), GLFont.ENGLISH);

        buttonPlay = new Button(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, buttonGLFont, "Play", Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer);
        buttonPlay.setX(buttonPlay.calculateScreenCenterX());
        buttonPlay.setY((frameBuffer.getHeight() / 2) - buttonPlay.getHeight() * 3);
        buttonPlay.setOnAction(this::onPlay);

        buttonSettings = new Button(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, buttonGLFont, "Settings", Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer);
        buttonSettings.setX(buttonSettings.calculateScreenCenterX());
        buttonSettings.setY(buttonPlay.getY() + buttonSettings.getHeight() + 10);
        buttonSettings.setOnAction(this::onSettings);

        buttonExit = new Button(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, buttonGLFont, "Exit", Constants.BUTTON_RED_BACKGROUND_TEXTURE, frameBuffer);
        buttonExit.setX(buttonExit.calculateScreenCenterX());
        buttonExit.setY(buttonSettings.getY() + buttonExit.getHeight() + 10);
        buttonExit.setOnAction(() -> game.getScreen().dispose());
    }

    private void onPlay() {
        isOpen = false;
        game.startGame();
    }

    private void onSettings(){
        settingsMenu.init();
    }

    public void update(float deltaTime){
        if(settingsMenu.isOpen())
            settingsMenu.update();
        else
            updateMenu();
    }

    private void updateMenu(){
        titleFont.blitString(frameBuffer, titleText, Utils.calculateCenterXForLabel(frameBuffer, titleFont, titleText), 100, 1, Color.WHITE);
        developerFont.blitString(frameBuffer, developerText, Utils.calculateCenterXForLabel(frameBuffer, developerFont, developerText), frameBuffer.getHeight() - 20, 1, Color.GRAY);

        buttonPlay.update();
        buttonSettings.update();
        buttonExit.update();
    }

    public boolean isOpen() {
        return isOpen;
    }
}
