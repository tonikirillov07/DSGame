package com.ds.dj3d.platforms.platformsManaging;

import com.ds.Constants;
import com.ds.dj3d.Chances;
import com.ds.dj3d.ScoreManager;
import com.ds.dj3d.platforms.MovingPlatform;
import com.ds.dj3d.platforms.Platform;
import com.ds.dj3d.platforms.Spring;
import com.ds.dj3d.enemies.EnemiesManager;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.ds.engine.shadows.ShadowsManager;
import com.ds.engine.utils.SoundsManager;
import com.ds.engine.utils.Utils;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class PlatformsSpawner {
    private static final Logger log = LoggerFactory.getLogger(PlatformsSpawner.class);
    private final Player player;
    private final GameWorld gameWorld;
    private float previousY = 0f;
    private final Object3D greenPlatformModel, bluePlatformModel, springModel;
    private final PlatformsManager platformsManager;
    private final ShadowsManager shadowsManager;
    private final ScoreManager scoreManager;
    private final List<Spring> springList;
    private final List<Platform> platformList;
    private final EnemiesManager enemiesManager;

    public PlatformsSpawner(Player player, GameWorld gameWorld, PlatformsManager platformsManager, ShadowsManager shadowsManager, ScoreManager scoreManager, List<Spring> springList, List<Platform> platformList) {
        this.player = player;
        this.gameWorld = gameWorld;
        this.platformsManager = platformsManager;
        this.shadowsManager = shadowsManager;
        this.scoreManager = scoreManager;
        this.springList = springList;
        this.platformList = platformList;

        enemiesManager = new EnemiesManager(gameWorld, player);

        greenPlatformModel = Object3D.mergeAll((Loader.loadOBJ("models/Green Platform/green_platform.obj", "models/Green Platform/green_platform.mtl", 1f)));
        bluePlatformModel = Object3D.mergeAll((Loader.loadOBJ("models/Blue Platform/blue_platform.obj", "models/Blue Platform/blue_platform.mtl", 1f)));
        springModel = Object3D.mergeAll((Loader.loadOBJ("models/Spring/spring.obj", "models/Spring/spring.mtl", 0.8f)));
    }

    public void create(int count, boolean spawnPlayer){
        Random random = new Random();
        log.info("Creating {} platforms. Spawn player at first flag: {}", count, spawnPlayer);

        for (int i = 0; i < count; i++) {
            Object3D currentPlatform = getCurrentPlatform(i == 0);

            Object3D object3D = new Object3D(currentPlatform);
            object3D.setScale(3f);

            if(Utils.getChance(Chances.SPRING_SPAWN_CHANCE))
                addSpring(object3D);

            Platform platform = definePlatform(currentPlatform, object3D);

            float randomX = random.nextFloat(Constants.GAME_SPACE_START_X, Constants.GAME_SPACE_END_X);
            float randomY = previousY - random.nextFloat(5f, 17f);
            previousY = randomY;

            object3D.translate(new SimpleVector(randomX, randomY, 0f));
            object3D.build();

            gameWorld.addObject(object3D);
            shadowsManager.getShadowHelper().addReceiver(object3D);

            if(i == 0 & spawnPlayer)
                player.setPosition(new SimpleVector(randomX, randomY - (2 * object3D.getScale()), 0));

            if(i == (count / 2))
                platform.setCreateMorePlatformWhenPlayerIsNear(true);

            if(scoreManager.getScore() >= Constants.ENEMIES_SPAWN_SCORE & Utils.getChance(Chances.ENEMY_SPAWN_CHANCE))
                enemiesManager.spawnEnemy(platform, platformList);

            platformList.add(platform);
        }

        log.info("Created platforms");
    }

    public void createPlatformsWithRandomCount(boolean spawnPlayer){
        int count = new Random().nextInt(10, 25);
        create(count, spawnPlayer);
    }

    public void recreate(int count){
        SoundsManager.getInstance().stopAll();

        deleteAll();
        scoreManager.resetScore();
        scoreManager.loadBestScore();

        player.makeAlive();

        create(count, true);
    }

    private void deleteAll() {
        platformList.forEach(platform -> {
            if(gameWorld.containsObject(platform.getModel()))
                gameWorld.removeObject(platform.getModel());
        });

        springList.forEach(spring -> {
            if(gameWorld.containsObject(spring.getSpringObject()))
                gameWorld.removeObject(spring.getSpringObject());
        });

        enemiesManager.deleteAll();

        platformList.clear();
        springList.clear();

        log.info("Every platforms were deleted");
    }

    private void addSpring(@NotNull Object3D platformObject){
        Object3D springObject = new Object3D(springModel);
        springObject.translate(new SimpleVector(platformObject.getTranslation().x + springObject.getScale(), platformObject.getTranslation().y - (springObject.getScale() / 2), platformObject.getTranslation().z));
        springObject.build();

        Spring spring = new Spring(springObject, player, gameWorld);
        spring.init();
        springList.add(spring);

        gameWorld.addObject(springObject);
        platformObject.addChild(springObject);
    }

    private Platform definePlatform(@NotNull Object3D currentPlatform, Object3D platformObject){
        Platform platform = new Platform(platformObject, player, gameWorld, platformsManager, scoreManager, Color.GREEN);
        if(currentPlatform.getUserObject() == PlatformType.MOVING)
            platform = new MovingPlatform(platformObject, player, gameWorld, scoreManager, platformsManager, Constants.GAME_SPACE_START_X, Constants.GAME_SPACE_END_X, Color.BLUE);

        return platform;
    }

    private @NotNull Object3D getCurrentPlatform(boolean isFirst){
        if(Utils.getChance(Chances.calculateBluePlatformChance(scoreManager.getScore())) & !isFirst)
            return getPlatformWithType(bluePlatformModel, PlatformType.MOVING);
       else
           return getPlatformWithType(greenPlatformModel, PlatformType.DEFAULT);
    }

    private @NotNull Object3D getPlatformWithType(Object3D model, PlatformType platformType){
        Object3D object3D = new Object3D(model);
        object3D.setUserObject(platformType);

        log.info("Created platform with type {}", platformType);

        return object3D;
    }

    public EnemiesManager getEnemiesManager() {
        return enemiesManager;
    }
}
