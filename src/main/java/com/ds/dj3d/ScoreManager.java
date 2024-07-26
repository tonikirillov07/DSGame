package com.ds.dj3d;

import com.ds.engine.Constants;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.FrameBuffer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ScoreManager {
    private final GLFont currentPlayerScoreFont, playerBestScoreFont;
    private final FrameBuffer frameBuffer;
    private int score;

    public ScoreManager(FrameBuffer frameBuffer) {
        this.frameBuffer = frameBuffer;

        currentPlayerScoreFont = new GLFont(Utils.getGameFont(Font.BOLD, 35f, Constants.ARCADE_CLASSIC_FONT_PATH));
        playerBestScoreFont = currentPlayerScoreFont;
    }

    public void update(){
        currentPlayerScoreFont.blitString(frameBuffer, "0", calculateScoreTextX(currentPlayerScoreFont, String.valueOf(score)), 50, 1, Color.GRAY);
        playerBestScoreFont.blitString(frameBuffer, "HI: 0", calculateScoreTextX(playerBestScoreFont,"HI: 0"), 100, 1, Color.GRAY);

    }

    private int calculateScoreTextX(@NotNull GLFont glFont, String text){
        return frameBuffer.getWidth() - (glFont.getStringBounds(String.valueOf(text)).width + 30);
    }
}
