package com.ds.engine.ui.button;

import com.ds.engine.ui.text.GLFont;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import org.lwjgl.input.Mouse;

import javax.swing.*;
import java.awt.*;

public class GUIButton extends GUIElement{

    //???????? ????????? ?????
    private Texture tex;
    //???????? ????????? ??????
    private Texture texSel;

    private String text;
    private GLFont font;
    private Color color;
    /**
     * ????????? ??????
     * 0 ?? ??????? ? ?? ??????, 1 ??????? , 2 ??????
     */
    private int Status=0;

    public GUIButton(int x, int y, int heigth, int width, Texture tex, Texture texSel)
    {
        super(x,y,heigth,width);
        this.tex = tex;
        this.texSel = texSel;
        setVisible(true);
        color = Color.BLACK;
    }

    @Override
    public void Draw(FrameBuffer buffer) {
        if (!getVisible())
        {
            return;
        }

        int text_x = 0;
        int text_y = 0;

        int parent_x=0;
        int parent_y=0;

        //????????? ????????? ????????????? ????????
        if (getParent()!=null)
        {
            parent_x=getParent().x;
            parent_y=getParent().y;
        }
        //?????? ????????? ???????
        if (font!=null)
        {
            Dimension d = font.getStringBounds(text).getSize();
            text_x=x+(width-d.width)/2 + parent_x;
            text_y=y+heigth-d.height + parent_y;
        }

        switch(Status)
        {
            case 0:
            {
                buffer.blit(tex, parent_x+0, parent_y+0, x, y, width, heigth, true);
                break;
            }
            case 1:
            {
                buffer.blit(texSel, parent_x+0, parent_y+0, x, y, width, heigth, true);
                break;
            }
            case 2:
            {
                buffer.blit(texSel, parent_x+0, parent_y+0, x, y+2, width, heigth, true);
                text_y+=2;
                break;
            }
        }
        //????????? ??????? ???? ??? ????
        if (font!=null)
        {
            font.blitString(buffer, text, text_x, text_y, 1, color);
        }
    }

    @Override
    public void evaluteInput(InputMap inputMap) {
        if(getVisible())
        {
            int xpos = Mouse.getX();
            int ypos = Mouse.getY();

            if((xpos>=x)&&(xpos<=x+width)&&(ypos>=y)&&(ypos<=y+heigth))
            {
                Status=1;
                if (Mouse.isButtonDown(0))
                {
                    Status = 2;
                    Action();
                }
            }
            else
            {
                Status = 0;
            }
        }
    }

    @Override
    public void Action()
    {}

    public void setFont(GLFont font)
    {
        this.font = font;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setTextColor(Color color)
    {
        this.color = color;
    }

    public Color getTextColor()
    {
        return color;
    }
}

