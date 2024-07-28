package com.ds.dj3d;

import com.ds.Constants;
import com.ds.dj3d.settings.SettingsConstants;
import com.ds.dj3d.settings.SettingsReader;
import com.ds.dj3d.settings.SettingsWriter;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.FrameBuffer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class ScoreManager {
    private final GLFont currentPlayerScoreFont, playerBestScoreFont;
    private final FrameBuffer frameBuffer;
    private int score;
    private final int bestScore;

    public ScoreManager(FrameBuffer frameBuffer) {
        this.frameBuffer = frameBuffer;

        currentPlayerScoreFont = new GLFont(Utils.getFont(Font.BOLD, 26f, Constants.JOYSTIX_MONOSPACE_FONT_PATH));
        playerBestScoreFont = currentPlayerScoreFont;

        bestScore = Integer.parseInt(Objects.requireNonNull(SettingsReader.getValue(SettingsConstants.BEST_SCORE_KEY)));
    }

    public void update(){
        String bestScoreText = "HI: " + bestScore;

        currentPlayerScoreFont.blitString(frameBuffer, String.valueOf(score), calculateScoreTextX(currentPlayerScoreFont, String.valueOf(score)), 50, 1, Color.GRAY);
        playerBestScoreFont.blitString(frameBuffer, bestScoreText, calculateScoreTextX(playerBestScoreFont, bestScoreText), 100, 1, Color.GRAY);

    }

    private int calculateScoreTextX(@NotNull GLFont glFont, String text){
        return frameBuffer.getWidth() - (glFont.getStringBounds(String.valueOf(text)).width + 30);
    }

    public void addScore(){
        score += 1;

        if(score > bestScore)
            SettingsWriter.writeValue(SettingsConstants.BEST_SCORE_KEY, String.valueOf(score));
    }
}
