package com.ds.dj3d;

import com.ds.Main;
import com.ds.engine.Constants;
import com.ds.engine.Screen;
import com.ds.engine.ui.button.Button;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class PauseMenu {
    private boolean isOpen = false;
    private final FrameBuffer frameBuffer;
    private final Screen screen;
    private Texture backgroundTexture;
    private final GLFont titleFont, pausedFont;
    private final Button buttonResume, buttonExit;

    public PauseMenu(@NotNull Screen screen) {
        this.frameBuffer = screen.getFrameBuffer();
        this.screen = screen;

        titleFont = new GLFont(Utils.getGameFont(Font.BOLD,100f, Constants.ARCADE_CLASSIC_FONT_PATH), GLFont.ENGLISH);
        pausedFont = new GLFont(Utils.getGameFont(Font.ITALIC, 40f, Constants.ROBOTO_BOLD_FONT_PATH), GLFont.ENGLISH);

        GLFont glFont = new GLFont(Utils.getGameFont(Font.BOLD,24f, Constants.ARCADE_CLASSIC_FONT_PATH));

        buttonResume = new Button(frameBuffer.getWidth() / 2, frameBuffer.getHeight() / 2, 215, 49, glFont, "Resume", new Texture(Main.class.getResourceAsStream("/textures/ui/ButtonBackground.png")), frameBuffer,
                Color.WHITE, Color.RED);
        buttonResume.setOnAction(() -> isOpen = false);

        buttonExit = new Button(frameBuffer.getWidth() / 9, (frameBuffer.getHeight() / 2) + 60, 215, 49, glFont, "Exit", new Texture(Main.class.getResourceAsStream("/textures/ui/ButtonBackground2.png")), frameBuffer,
                Color.WHITE, Color.RED);
        buttonExit.setOnAction(screen::dispose);

        prepareTextures();
    }

    private void prepareTextures() {
        backgroundTexture = new Texture(Main.class.getResourceAsStream("/textures/ui/PauseBackground.png"));
    }

    public void update(){
        if(Keyboard.isKeyDown(Keyboard.KEY_R))
            isOpen = !isOpen;

        changeMenuState();
        screen.setTimeScale(isOpen ? 0f: 1f);
    }

    private void changeMenuState() {
        if(isOpen){
            drawMenu();
        }
    }

    private void drawMenu(){
        frameBuffer.blit(backgroundTexture, 0,0, 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight(), frameBuffer.getWidth(), frameBuffer.getHeight(), 0, true);
        titleFont.blitString(frameBuffer, "DOODLE JUMP 3D", frameBuffer.getWidth() / 9, frameBuffer.getHeight() / 3, 1, Color.WHITE);
        pausedFont.blitString(frameBuffer, "Paused", 50, frameBuffer.getHeight() - 50, 1, new Color(176, 176, 176));

        Mouse.setGrabbed(false);

        buttonResume.update();
        buttonExit.update();
    }
}
