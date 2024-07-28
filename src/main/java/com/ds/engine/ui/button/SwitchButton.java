package com.ds.engine.ui.button;

import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.IOnAction;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;

import java.util.List;

public class SwitchButton extends Button{
    private final List<String> availableValues;
    private String currentValue;
    private int currentValueIndex;

    public SwitchButton(int width, int height, GLFont glFont, String text, Texture texture, FrameBuffer frameBuffer, List<String> availableValues) {
        super(width, height, glFont, text, texture, frameBuffer);
        this.availableValues = availableValues;
    }

    public void setValue(int valueIndex){
        String newValue = availableValues.get(valueIndex);

        setText(newValue);

        currentValueIndex = valueIndex;
        currentValue = newValue;
    }

    @Override
    public void onClick() {
        super.onClick();

        switchValue();
    }

    private void switchValue() {
        if(currentValueIndex + 1 >= availableValues.size())
            currentValueIndex = 0;
        else
            currentValueIndex++;

        setValue(currentValueIndex);
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public int getCurrentValueIndex() {
        return currentValueIndex;
    }
}
