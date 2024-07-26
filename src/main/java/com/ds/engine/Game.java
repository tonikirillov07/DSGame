package com.ds.engine;

import com.ds.dj3d.PauseMenu;
import com.ds.dj3d.PlatformsManager;
import com.ds.dj3d.ScoreManager;
import com.ds.dj3d.player.Player;
import com.ds.engine.camera.FreeCamera;
import com.ds.engine.ui.text.GLFont;
import com.ds.engine.utils.Utils;
import com.threed.jpct.*;
import com.threed.jpct.util.Light;

import java.awt.*;

public class Game implements IGameEvents {
    private Screen screen;
    private GameWorld gameWorld;
    private FreeCamera freeCamera;
    private Player player;
    private GLFont fpsText;
    private PlatformsManager platformsManager;
    private PauseMenu pauseMenu;
    private ScoreManager scoreManager;

    public void start(){
        screen = new Screen(this);
        screen.start();
    }

    @Override
    public void onStart() {
        try {
            gameWorld = new GameWorld();
            player = new Player(Loader.loadOBJ("models/dj.obj", "models/dj.mtl", 1f), gameWorld, gameWorld.getCamera());
            freeCamera = new FreeCamera(SimpleVector.ORIGIN, gameWorld.getCamera());
            pauseMenu = new PauseMenu(screen);

            fpsText = new GLFont(Utils.getGameFont(Font.BOLD,35f, Constants.ARCADE_CLASSIC_FONT_PATH), GLFont.ENGLISH);

            Object3D plane = Primitives.getPlane(200, 200);
            plane.translate(new SimpleVector(0, 0, 0));
            plane.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
            plane.rotateX((float) Math.toRadians(90));
            plane.build();

            Light light = new Light(gameWorld);
            light.setPosition(new SimpleVector(0, -40, 0));
            light.setIntensity(new SimpleVector(255, 255, 255));

            platformsManager = new PlatformsManager();
            platformsManager.init(player, gameWorld);

            scoreManager = new ScoreManager(screen.getFrameBuffer());

            gameWorld.addObject(plane);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(float deltaTime) {
        gameWorld.update(screen.getFrameBuffer());
        freeCamera.move(deltaTime);
        player.update(deltaTime);
        platformsManager.update();
        pauseMenu.update();
        scoreManager.update();

        fpsText.blitString(screen.getFrameBuffer(), "FPS: " + Math.round(1 /deltaTime), 30, 50, 1, Color.RED);
    }

    @Override
    public void onDispose() {
        gameWorld.dispose();
    }
}
