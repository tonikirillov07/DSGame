package com.ds.dj3d;

import com.ds.dj3d.platforms.Platform;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import java.util.List;
import java.util.Random;

public class PlatformsSpawner {
    public void create(int count, Object3D[] platformsObjects, GameWorld gameWorld, float startX, float endX, Player player, List<Platform> allPlatforms){
        Object3D[] platforms = new Object3D[count];
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            platforms[i] = platformsObjects[random.nextInt(platformsObjects.length)];
        }

        float previousY = 0f;

        for (Object3D currentPlatform : platforms) {
            Object3D object3D = new Object3D(currentPlatform);
            object3D.setScale(3f);

            Platform platform = new Platform(object3D, player);
            allPlatforms.add(platform);

            float randomX = random.nextFloat(startX, endX);
            float randomY = previousY - random.nextFloat(10f, 20f);
            previousY = randomY;

            object3D.translate(new SimpleVector(randomX, randomY, 0f));
            object3D.build();

            gameWorld.addObject(object3D);
        }
    }
}
