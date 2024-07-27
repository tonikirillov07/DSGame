package com.ds.engine.ui.button;

import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.IOnAction;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import org.lwjgl.input.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class Button {
    private static final Logger log = LoggerFactory.getLogger(Button.class);
    private int x, y;
    private final int width;
    private final int height;
    private final GLFont glFont;
    private String text;
    private final Texture texture;
    private Texture textureSelected;
    private final FrameBuffer frameBuffer;
    private final Color textColor, selectedTextColor;
    private IOnAction onAction;

    public static final int DEFAULT_WIDTH = 215;
    public static final int DEFAULT_HEIGHT = 49;

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

        log.info("Created button {}", this);
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

        log.info("Created button {}", this);
    }

    public Button(int width, int height, GLFont glFont, String text, Texture texture, FrameBuffer frameBuffer) {
        this.width = width;
        this.height = height;
        this.glFont = glFont;
        this.text = text;
        this.texture = texture;
        this.frameBuffer = frameBuffer;

        textColor = Color.WHITE;
        selectedTextColor = Color.RED;
    }

    public void update(){
        draw();
        listenMouseEvents();
    }

    private void listenMouseEvents() {
        if(Mouse.isButtonDown(0)) {
            if(isMouseInButton())
                onClick();
        }
    }

    public void onClick(){
        if(onAction != null)
            onAction.onAction();
    }

    private boolean isMouseInButton(){
        int mouseX = Mouse.getX();
        int mouseY = frameBuffer.getOutputHeight() - Mouse.getY();

        return (mouseX >= x & mouseX <= (x + width)) & (y <= mouseY & (y + height) >= mouseY);
    }

    private void draw(){
        Texture textureToRender;
        if(textureSelected != null && isMouseInButton())
            textureToRender = textureSelected;
        else
            textureToRender = texture;

        frameBuffer.blit(textureToRender, 0, 0, x, y, width, height, false);
        glFont.blitString(frameBuffer, text, x + (width - glFont.getStringBounds(text).width) / 2, y + (glFont.getStringBounds(text).height) + 5, 1, isMouseInButton() ? selectedTextColor : textColor);
    }

    public int calculateScreenCenterX(){
        return (frameBuffer.getWidth() / 2) - (getWidth() / 2);
    }

    public void setOnAction(IOnAction onAction) {
        this.onAction = onAction;
    }

    public void setTextureSelected(Texture textureSelected) {
        this.textureSelected = textureSelected;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Button{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", glFont=" + glFont +
                ", text='" + text + '\'' +
                ", texture=" + texture +
                ", textureSelected=" + textureSelected +
                ", frameBuffer=" + frameBuffer +
                ", textColor=" + textColor +
                ", selectedTextColor=" + selectedTextColor +
                ", onAction=" + onAction +
                '}';
    }
}
