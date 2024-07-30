package com.ds.dj3d;

import com.ds.dj3d.platforms.Platform;
import com.ds.dj3d.platforms.platformsManaging.PlatformsSpawner;
import com.ds.dj3d.player.Player;

import java.util.ConcurrentModificationException;
import java.util.List;

public class LoseManager {
    private final Player player;
    private final List<Platform> platformList;
    private final PlatformsSpawner platformsSpawner;

    public LoseManager(Player player, List<Platform> platformList, PlatformsSpawner platformsSpawner) {
        this.player = player;
        this.platformList = platformList;
        this.platformsSpawner = platformsSpawner;
    }

    public void update(){
        findLosePlatform();
    }

    private void findLosePlatform(){
        try {
            if(player.getPosition().y > platformList.get(0).getModel().getTranslation().y)
                platformsSpawner.recreate(10);
        }catch (ConcurrentModificationException ignore){}
    }
}
