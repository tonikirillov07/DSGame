package com.ds.engine.ui.button;

import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.IOnAction;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class Button {
    private final int x, y;
    private final int width, height;
    private final GLFont glFont;
    private final String text;
    private Texture texture, textureSelected;
    private final FrameBuffer frameBuffer;
    private final Color textColor, selectedTextColor;
    private IOnAction onAction;

    public Button(int x, int y, int width, int height, GLFont glFont, String text, Texture texture, FrameBuffer frameBuffer, Color textColor, Color selectedTextColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.glFont = glFont;
        this.text = text;
        this.texture = texture;
        this.frameBuffer = frameBuffer;
        this.textColor = textColor;
        this.selectedTextColor = selectedTextColor;
    }

    public Button(int x, int y, int width, int height, GLFont glFont, String text, Texture texture, Texture textureSelected, FrameBuffer frameBuffer, Color textColor, Color selectedTextColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.glFont = glFont;
        this.text = text;
        this.texture = texture;
        this.textureSelected = textureSelected;
        this.frameBuffer = frameBuffer;
        this.textColor = textColor;
        this.selectedTextColor = selectedTextColor;
    }

    public void update(){
        draw();
        listenMouseEvents();

    }

    private void listenMouseEvents() {
        if(Mouse.isButtonDown(0)) {
            if(onAction == null)
                return;

            if(isMouseInButton())
                onAction.onAction();
        }
    }

    private boolean isMouseInButton(){
        int mouseX = Mouse.getX();
        int mouseY = Mouse.getY();

        return (mouseX >= x && mouseX <= (x + width)) && (mouseY <= y && mouseY >= (y - height));
    }

    private void draw(){
        Texture textureToRender;
        if(textureSelected != null && isMouseInButton())
            textureToRender = textureSelected;
        else
            textureToRender = texture;

        frameBuffer.blit(textureToRender, 0, 0, x, y, width, height, false);
        glFont.blitString(frameBuffer, text, x + (width - glFont.getStringBounds(text).width) / 2, y + (height - glFont.getStringBounds(text).height) + 5, 1, isMouseInButton() ? selectedTextColor : textColor);
    }

    public void setOnAction(IOnAction onAction) {
        this.onAction = onAction;
    }

    public void setTextureSelected(Texture textureSelected) {
        this.textureSelected = textureSelected;
    }
}
