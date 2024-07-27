package com.ds.dj3d;

import com.ds.Constants;
import com.ds.dj3d.platforms.platformsManaging.PlatformsManager;
import com.ds.dj3d.player.Player;
import com.ds.dj3d.ui.MainMenu;
import com.ds.dj3d.ui.PauseMenu;
import com.ds.engine.utils.ErrorHandler;
import com.ds.engine.GameWorld;
import com.ds.engine.IGameEvents;
import com.ds.engine.Screen;
import com.ds.engine.camera.FreeCamera;
import com.ds.engine.shadows.ShadowsManager;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.*;
import org.lwjgl.input.Mouse;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class Game implements IGameEvents {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Game.class);
    private Screen screen;
    private GameWorld gameWorld;
    private FreeCamera freeCamera;
    private Player player;
    private GLFont fpsText;
    private PlatformsManager platformsManager;
    private PauseMenu pauseMenu;
    private ScoreManager scoreManager;
    private ShadowsManager shadowsManager;
    private MainMenu mainMenu;
    private boolean isGameStarted;

    public void start(){
        screen = new Screen(this);
        screen.start();
    }

    @Override
    public void onStart() {
        try {
            log.info("Staring main game...");

            initConfigs();

            gameWorld = new GameWorld();
            player = new Player(Loader.loadOBJ("models/dj.obj", "models/dj.mtl", 1f), gameWorld, gameWorld.getCamera());
            freeCamera = new FreeCamera(gameWorld.getCamera());
            pauseMenu = new PauseMenu(screen);
            mainMenu = new MainMenu(screen.getFrameBuffer(), this, gameWorld.getCamera());
            mainMenu.init();

            fpsText = new GLFont(Utils.getFont(Font.BOLD,35f, Constants.ARCADE_CLASSIC_FONT_PATH), GLFont.ENGLISH);

            scoreManager = new ScoreManager(screen.getFrameBuffer());

            log.info("Main game started");
        }catch (Exception e){
            ErrorHandler.doError(e);
        }
    }

    public void startGame(){
        Mouse.setGrabbed(true);

        shadowsManager = new ShadowsManager(gameWorld, screen.getFrameBuffer());
        platformsManager = new PlatformsManager();

        player.init();
        shadowsManager.init();
        platformsManager.init(player, gameWorld, shadowsManager, scoreManager);

        shadowsManager.getShadowHelper().addCaster(player.getPlayer());

        isGameStarted = true;
    }

    private void updateGame(float deltaTime){
        shadowsManager.update(player);
        player.update(deltaTime);
        platformsManager.update(deltaTime);
        scoreManager.update();

        if(screen.getTimeScale() != 0f)
            fpsText.blitString(screen.getFrameBuffer(), "FPS: " + Math.round(1 / deltaTime), 30, 50, 1, Color.RED);

        pauseMenu.update();
    }

    private void initConfigs() {
        Config.glShadowZBias = 0.8f;
        Config.lightMul = 1;
        Config.glColorDepth = 24;
        Config.glAvoidTextureCopies = true;
        Config.glTrilinear = true;
        Config.glVerbose = true;
        Config.collideOffset = 500f;
    }

    @Override
    public void onUpdate(float deltaTime) {
        gameWorld.update(screen.getFrameBuffer());

        if(mainMenu.isOpen())
            mainMenu.update(deltaTime);

        if(isGameStarted)
            updateGame(deltaTime);
    }

    @Override
    public void onDispose() {
        if(shadowsManager != null)
            shadowsManager.dispose();

        gameWorld.dispose();
    }

    public Screen getScreen() {
        return screen;
    }
}
