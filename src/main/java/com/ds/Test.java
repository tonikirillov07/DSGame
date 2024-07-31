package com.ds;

import com.threed.jpct.*;

import com.threed.jpct.util.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Test {
    private World world;
    private FrameBuffer buffer;
    private Camera cam;
    private ArrayList<SimpleVector> raindrops;

    private static final int NUM_DROPS = 500; // ?????????? ??????
    private static final int GROUND_Y = 100; // ?????? ?????
    private static final int GROUND_HEIGHT = 600; // ?????? ????

    public Test() {
        world = new World();
        cam = new Camera();
        buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
        raindrops = new ArrayList<>();

        // ??????? ????? ?????
        for (int i = 0; i < NUM_DROPS; i++) {
            float x = (float) (Math.random() * 800); // ????????? ????????? ?? X
            float y = (float) (Math.random() * GROUND_HEIGHT);
            raindrops.add(new SimpleVector(x, y, 0)); // (x, y, z)
        }
    }

    public void render() {
        buffer.clear();
        world.draw(buffer);
        drawRain();
        buffer.display(buffer.getGraphics());
    }

    private void drawRain() {
        Graphics g = buffer.getGraphics();
        g.setColor(Color.BLUE);
        for (SimpleVector drop : raindrops) {
            g.drawLine((int) drop.x, (int) drop.y, (int) drop.x, (int) (drop.y + 10)); // ?????? ?????
            // ????????? ??????? ?????
            drop.y += 5; // ???????? ???????
            if (drop.y > GROUND_Y) {
                drop.y = 0; // ???? ????? ???????? ?????, ?????????? ?? ????
                drop.x = (float) (Math.random() * 800); // ????????? ????? ????????? ?? X
            }
        }
    }

    public static void main(String[] args) {
        Test rainEffect = new Test();

        // ???????? ???? ??????????
        while (true) {
            rainEffect.render();
            try {
                Thread.sleep(16); // ???????? ??? 60 ?????? ? ???????
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}