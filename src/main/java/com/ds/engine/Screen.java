package com.ds.engine;

import com.ds.Constants;
import com.ds.engine.utils.ErrorHandler;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.IRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Screen {
    private static final Logger log = LoggerFactory.getLogger(Screen.class);
    private FrameBuffer frameBuffer;
    private float deltaTime, timeScale = 1f;
    private final IGameEvents gameEvents;

    public Screen(IGameEvents gameEvents) {
        this.gameEvents = gameEvents;
    }

    public void start(){
        log.info("Starting frame buffer...");

        Config.glWindowName = Constants.TITLE;

        frameBuffer = new FrameBuffer(Constants.START_WIDTH, Constants.START_HEIGHT, FrameBuffer.SAMPLINGMODE_GL_AA_4X);
        frameBuffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        frameBuffer.enableRenderer(IRenderer.RENDERER_OPENGL);

        initDisplay();
        initMouse();
        initKeyboard();

        gameEvents.onStart();
        startUpdate();
    }

    private void initKeyboard() {
        try {
            if(!Keyboard.isCreated())
                Keyboard.create();
        }catch (Exception e){
            ErrorHandler.doError(e);
        }
    }

    private void initMouse() {
        try {
            log.info("Initializing mouse...");

            if(!Mouse.isCreated())
                Mouse.create();
        }catch (Exception e){
            ErrorHandler.doError(e);
        }
    }

    private void initDisplay() {
        try {
            log.info("Initializing display...");

            if(!Display.isCreated())
                Display.create();

            Display.setResizable(true);

            Display.setDisplayMode(new DisplayMode(Constants.PREFER_WIDTH, Constants.PREFER_HEIGHT));
            frameBuffer.resize(Constants.PREFER_WIDTH, Constants.PREFER_HEIGHT);
        }catch (Exception e){
            ErrorHandler.doError(e);
        }
    }

    private void startUpdate() {
        log.info("Starting main loop...");

        while (!Display.isCloseRequested()){
            float startTime = System.nanoTime();

            frameBuffer.clear();

            gameEvents.onUpdate(deltaTime * timeScale);

            frameBuffer.display();
            frameBuffer.update();

            Display.sync(Constants.TARGET_FPS);

            deltaTime = (System.nanoTime() - startTime) / 1_000_000_000f;
        }

        dispose();
    }

    public void dispose() {
        log.info("Disposing engine...");

        gameEvents.onDispose();

        Mouse.destroy();
        Keyboard.destroy();
        AL.destroy();

        frameBuffer.dispose();
        System.exit(0);
    }

    public float getTimeScale() {
        return timeScale;
    }

    public FrameBuffer getFrameBuffer() {
        return frameBuffer;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }
}