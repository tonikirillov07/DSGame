package com.ds.engine.ui.button;

import com.threed.jpct.FrameBuffer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public abstract class GUIElement {

    public int x;
    public int y;

    public int heigth;
    public int width;

    private boolean isVisible;
    private boolean isResizeble;

    private Object parent;
    private Vector<Object> childrens;

    public GUIElement(int x,int y,int heigth,int width)
    {
        isVisible = true;
        isResizeble = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.heigth = heigth;
        childrens = new Vector<>();
    }

    public void setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
    }

    public boolean getVisible()
    {
        return isVisible;
    }

    public Dimension getSize()
    {
        return new Dimension(width, heigth);
    }

    public void setPosition(int x,int y)
    {
        this.x= x;
        this.y = y;
    }

    public Point getPosition()
    {
        return new Point(x,y);
    }

    public GUIElement getParent()
    {
        return (GUIElement)parent;
    }

    public void setParent(Object parent)
    {
        this.parent = parent;
    }

    public void Add(@NotNull GUIElement element)
    {
        element.setParent(this);
        childrens.add(element);
    }

    public abstract void evaluteInput(InputMap inputMap);

    public abstract void Action();

    public abstract void Draw(FrameBuffer buffer);
}