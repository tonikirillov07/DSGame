package com.ds.dj3d;

import com.ds.dj3d.platforms.Platform;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;

import java.util.ArrayList;
import java.util.List;

public class PlatformsManager {
    private List<Platform> allPlatforms;

    public void init(Player player, GameWorld gameWorld){
        allPlatforms = new ArrayList<>();

        PlatformsSpawner platformsSpawner = new PlatformsSpawner();
        platformsSpawner.create(10, new Object3D[]{Object3D.mergeAll((Loader.loadOBJ("models/Green Platform/green_platform.obj", "models/Green Platform/green_platform.mtl", 1f)))}, gameWorld,
                -20f, 80f, player, allPlatforms);
    }

    public void update(){
        allPlatforms.forEach(Platform::update);
    }
}
