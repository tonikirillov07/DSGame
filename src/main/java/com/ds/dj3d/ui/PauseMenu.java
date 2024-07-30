package com.ds.dj3d.ui;

import com.ds.Main;
import com.ds.Constants;
import com.ds.dj3d.Game;
import com.ds.dj3d.platforms.platformsManaging.PlatformsSpawner;
import com.ds.engine.Screen;
import com.ds.engine.ui.button.Button;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseMenu {
    private boolean isOpen = false;
    private final FrameBuffer frameBuffer;
    private final Screen screen;
    private Texture backgroundTexture;
    private final GLFont titleFont, pausedFont;
    private final Button buttonResume, buttonExit, buttonRestart;
    private final KeyMapper keyMapper;
    private final Game game;

    public PauseMenu(@NotNull Screen screen, Game game) {
        this.frameBuffer = screen.getFrameBuffer();
        this.screen = screen;
        this.game = game;

        keyMapper = new KeyMapper();

        titleFont = new GLFont(Utils.getFont(Font.BOLD,65f, Constants.ARCADE_CLASSIC_FONT_PATH), GLFont.ENGLISH);
        pausedFont = new GLFont(Utils.getFont(Font.ITALIC, 25f, Constants.ROBOTO_BOLD_FONT_PATH), GLFont.ENGLISH);

        GLFont glFont = new GLFont(Utils.getFont(Font.BOLD,24f, Constants.JOYSTIX_MONOSPACE_FONT_PATH));

        buttonResume = new Button(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, glFont, "Resume", Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer);
        buttonResume.setX(buttonResume.calculateScreenCenterX());
        buttonResume.setY(frameBuffer.getHeight() / 3);
        buttonResume.setOnAction(this::close);

        buttonRestart = new Button(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, glFont, "Restart", Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer);
        buttonRestart.setX(buttonRestart.calculateScreenCenterX());
        buttonRestart.setY(buttonResume.getY() + 70);
        buttonRestart.setOnAction(this::onRestart);

        buttonExit = new Button(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, glFont, "Goto menu", Constants.BUTTON_RED_BACKGROUND_TEXTURE, frameBuffer);
        buttonExit.setX(buttonExit.calculateScreenCenterX());
        buttonExit.setY(buttonRestart.getY() + 70);
        buttonExit.setOnAction(this::onGotoMenu);

        prepareTextures();
    }

    private void onGotoMenu() {
        game.getGameWorld().removeAll();
        game.setGameStarted(false);
        game.getMainMenu().init();
        close();

    }

    private void onRestart() {
        game.getPlatformsManager().getPlatformsSpawner().recreate(10);
        game.getScreen().setTimeScale(1f);

        close();
    }

    private void close(){
        isOpen = false;
    }

    private void prepareTextures() {
        backgroundTexture = new Texture(Main.class.getResourceAsStream("/textures/ui/PauseBackground.png"));
    }

    public void update() {
        KeyState keyState;
        while ((keyState = keyMapper.poll()) != KeyState.NONE) {
            if (keyState.getKeyCode() == KeyEvent.VK_ESCAPE & keyState.getState()){
                isOpen = !isOpen;
                Mouse.setGrabbed(!isOpen);
            }
        }

        changeMenuState();
        screen.setTimeScale(isOpen ? 0f : 1f);
    }

    private void changeMenuState() {
        if(isOpen)
            drawMenu();
    }

    private void drawMenu(){
        frameBuffer.blit(backgroundTexture, 0,0, 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight(), frameBuffer.getWidth(), frameBuffer.getHeight(), 0, true);
        titleFont.blitString(frameBuffer, Constants.TITLE.toUpperCase(), Utils.calculateCenterXForLabel(frameBuffer, titleFont, Constants.TITLE), frameBuffer.getHeight() / 4, 1, Color.WHITE);
        pausedFont.blitString(frameBuffer, "Paused", 50, frameBuffer.getHeight() - 50, 1, new Color(176, 176, 176));

        Mouse.setGrabbed(false);

        buttonResume.update();
        buttonRestart.update();
        buttonExit.update();
    }
}
