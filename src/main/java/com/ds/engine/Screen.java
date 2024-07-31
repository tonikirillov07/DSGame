package com.ds.engine;

import com.ds.Constants;
import com.ds.dj3d.settings.SettingsConstants;
import com.ds.dj3d.settings.SettingsReader;
import com.ds.engine.utils.ErrorHandler;
import com.ds.engine.utils.events.IGameEvents;
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

import java.util.concurrent.TimeUnit;

public class Screen {
    private static final Logger log = LoggerFactory.getLogger(Screen.class);
    private FrameBuffer frameBuffer;
    private float deltaTime, timeScale = 1f;
    private final IGameEvents gameEvents;
    private int frameCount = 0;
    private boolean isLimitFps;

    public Screen(IGameEvents gameEvents) {
        this.gameEvents = gameEvents;
    }

    public void start(){
        log.info("Starting frame buffer...");

        Config.glWindowName = Constants.TITLE;
        isLimitFps = Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.LIMIT_FPS_KEY));

        frameBuffer = new FrameBuffer(Constants.START_WIDTH, Constants.START_HEIGHT, FrameBuffer.SAMPLINGMODE_NORMAL);
        frameBuffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        frameBuffer.enableRenderer(IRenderer.RENDERER_OPENGL, IRenderer.MODE_OPENGL);

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

            Display.setVSyncEnabled(Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_VSYNC_KEY)));
        }catch (Exception e){
            ErrorHandler.doError(e);
        }
    }

    private void startUpdate() {
        log.info("Starting main loop...");

        long lastTime = 0L;
        int lastFps = 0;

        while (!Display.isCloseRequested()){
            long currentTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
            float startTime = System.nanoTime();

            if(lastTime < currentTime - 1000){
                lastTime = currentTime;
                lastFps = frameCount;
                frameCount = 0;
            }

            frameBuffer.clear();

            frameCount++;
            gameEvents.onUpdate(deltaTime * timeScale, lastFps);

            frameBuffer.update();
            frameBuffer.display();

            Display.sync(isLimitFps ? Constants.TARGET_FPS : -1);

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

        frameBuffer.disableRenderer(IRenderer.RENDERER_OPENGL);
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

    public void setLimitFps(boolean limitFps) {
        isLimitFps = limitFps;
    }
}
