package com.ds.engine;

import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.IRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Screen {
    private FrameBuffer frameBuffer;
    private float deltaTime, timeScale = 1f;
    private final IGameEvents gameEvents;

    public Screen(IGameEvents gameEvents) {
        this.gameEvents = gameEvents;
    }

    public void start(){
        Config.glWindowName = Constants.TITLE;

        frameBuffer = new FrameBuffer(Constants.START_WIDTH, Constants.START_HEIGHT, FrameBuffer.SAMPLINGMODE_NORMAL);
        frameBuffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        frameBuffer.enableRenderer(IRenderer.RENDERER_OPENGL);

        initDisplay();

        gameEvents.onStart();
        startUpdate();
    }

    private void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(Constants.PREFER_WIDTH, Constants.PREFER_HEIGHT));
            frameBuffer.resize(Constants.PREFER_WIDTH, Constants.PREFER_HEIGHT);

            Mouse.setGrabbed(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startUpdate() {
        while (!Display.isCloseRequested()){
            float startTime = System.nanoTime();

            frameBuffer.clear();

            listenEspecialInput();

            gameEvents.onUpdate(deltaTime * timeScale);

            frameBuffer.display();
            frameBuffer.update();

            Display.sync(Constants.TARGET_FPS);

            deltaTime = (System.nanoTime() - startTime) / 1_000_000_000f;
        }

        dispose();
    }

    public void dispose() {
        gameEvents.onDispose();

        AL.destroy();
        frameBuffer.dispose();
        System.exit(0);
    }

    private void listenEspecialInput(){
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            dispose();
    }

    public FrameBuffer getFrameBuffer() {
        return frameBuffer;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }
}
