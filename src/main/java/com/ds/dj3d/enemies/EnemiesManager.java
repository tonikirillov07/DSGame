package com.ds.dj3d.enemies;

import com.ds.Constants;
import com.ds.dj3d.platforms.Platform;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EnemiesManager {
    private final Object3D[] enemies;
    private final GameWorld gameWorld;
    private float spawnTimer = 0f;
    private final Player player;
    private static final float ENEMY_SPAWN_DELAY = 5f;
    private final List<Enemy> enemiesList;

    public EnemiesManager(GameWorld gameWorld, Player player) {
        this.gameWorld = gameWorld;
        this.player = player;

        Object3D redMonster = Object3D.mergeAll(Loader.loadOBJ("models/Red Monster/red_monster.obj", "models/Red Monster/red_monster.mtl", Constants.ENEMIES_SCALE));
        Object3D spider = Object3D.mergeAll(Loader.loadOBJ("models/Spider/spider.obj", "models/Spider/spider.mtl", Constants.ENEMIES_SCALE));
        spider.rotateY(Constants.DEGREES_180);
        Object3D blueMonster = Object3D.mergeAll(Loader.loadOBJ("models/Blue Monster/blueMonster.obj", "models/Blue Monster/blueMonster.mtl", Constants.ENEMIES_SCALE));

        enemies = new Object3D[]{redMonster, spider, blueMonster};
        enemiesList = new ArrayList<>();
    }

    public void spawnEnemy(@NotNull Platform platform, List<Platform> platformList){
        if(spawnTimer < ENEMY_SPAWN_DELAY)
            return;

        Random random = new Random();

        Object3D object3D = new Object3D(enemies[random.nextInt(enemies.length)]);
        object3D.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        object3D.rotateZ(Constants.DEGREES_180);

        float randomX = random.nextFloat(Constants.GAME_SPACE_START_X, Constants.GAME_SPACE_END_X);
        float y = platform.getModel().getTranslation().y - object3D.getScale();

        SimpleVector enemyPosition = new SimpleVector(randomX, y, 0f);
        boolean canBeSpawn = true;

        for (Platform platformInList : platformList) {
            if(platformInList.getModel().getTranslation().distance(enemyPosition) <= object3D.getScale() * 3){
                canBeSpawn = false;
                break;
            }
        }

        if(!canBeSpawn){
            spawnTimer = 0f;
            return;
        }

        object3D.translate(enemyPosition);

        gameWorld.addObject(object3D);
        enemiesList.add(new Enemy(object3D, gameWorld, player));

        spawnTimer = 0f;
    }

    public void update(float deltaTime){
        if (spawnTimer < ENEMY_SPAWN_DELAY)
            spawnTimer += deltaTime;

        Iterator<Enemy> enemyIterator = enemiesList.iterator();
        while(enemyIterator.hasNext()){
            Enemy enemy = enemyIterator.next();

            if(enemy.isDestroyed())
                enemyIterator.remove();
            else
                enemy.update(deltaTime);
        }
    }
}