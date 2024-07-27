package com.ds.dj3d.ui;

import com.ds.Main;
import com.ds.Constants;
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
    private final Button buttonResume, buttonExit;
    private final KeyMapper keyMapper;

    public PauseMenu(@NotNull Screen screen) {
        this.frameBuffer = screen.getFrameBuffer();
        this.screen = screen;

        keyMapper = new KeyMapper();

        titleFont = new GLFont(Utils.getFont(Font.BOLD,80f, Constants.ARCADE_CLASSIC_FONT_PATH), GLFont.ENGLISH);
        pausedFont = new GLFont(Utils.getFont(Font.ITALIC, 40f, Constants.ROBOTO_BOLD_FONT_PATH), GLFont.ENGLISH);

        GLFont glFont = new GLFont(Utils.getFont(Font.BOLD,24f, Constants.ARCADE_CLASSIC_FONT_PATH));

        int buttonsX = frameBuffer.getWidth() - 400;
        buttonResume = new Button(buttonsX, frameBuffer.getHeight() / 3, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, glFont, "Resume", Constants.BUTTON_DEFAULT_BACKGROUND_TEXTURE, frameBuffer,
                Color.WHITE, Color.RED);
        buttonResume.setOnAction(() -> isOpen = false);

        buttonExit = new Button(buttonsX, (frameBuffer.getHeight() / 3) + 60, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, glFont, "Exit", Constants.BUTTON_RED_BACKGROUND_TEXTURE, frameBuffer,
                Color.WHITE, Color.RED);
        buttonExit.setOnAction(screen::dispose);

        prepareTextures();
    }

    private void prepareTextures() {
        backgroundTexture = new Texture(Main.class.getResourceAsStream("/textures/ui/PauseBackground.png"));
    }

    public void update(){
        KeyState keyState;
        while ((keyState = keyMapper.poll()) != KeyState.NONE){
            if(keyState.getKeyCode() == KeyEvent.VK_ESCAPE & keyState.getState())
                isOpen = !isOpen;
        }

        changeMenuState();
        screen.setTimeScale(isOpen ? 0f: 1f);
    }

    private void changeMenuState() {
        if(isOpen)
            drawMenu();

        Mouse.setGrabbed(!isOpen);
    }

    private void drawMenu(){
        frameBuffer.blit(backgroundTexture, 0,0, 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight(), frameBuffer.getWidth(), frameBuffer.getHeight(), 0, true);
        titleFont.blitString(frameBuffer, Constants.TITLE.toUpperCase(), frameBuffer.getWidth() / 9, frameBuffer.getHeight() / 3, 1, Color.WHITE);
        pausedFont.blitString(frameBuffer, "Paused", 50, frameBuffer.getHeight() - 50, 1, new Color(176, 176, 176));

        Mouse.setGrabbed(false);

        buttonResume.update();
        buttonExit.update();
    }
}
