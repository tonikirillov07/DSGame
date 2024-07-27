package com.ds.dj3d.platforms.platformsManaging;

import com.ds.Constants;
import com.ds.dj3d.ScoreManager;
import com.ds.dj3d.platforms.Platform;
import com.ds.dj3d.platforms.Spring;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.ds.engine.shadows.ShadowsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class PlatformsManager {
    private static final Logger log = LoggerFactory.getLogger(PlatformsManager.class);
    private List<Platform> platformList;
    private List<Spring> springList;
    private PlatformsSpawner platformsSpawner;

    public void init(Player player, GameWorld gameWorld, ShadowsManager shadowsManager, ScoreManager scoreManager){
        log.info("Initializing platforms manager...");

        platformList = new ArrayList<>();
        springList = new ArrayList<>();

        platformsSpawner = new PlatformsSpawner(player, gameWorld, this, shadowsManager, scoreManager, springList);
        platformsSpawner.create(10, platformList, Constants.GAME_SPACE_START_X, Constants.GAME_SPACE_END_X, true);
    }

    public void update(float deltaTime){
        try {
            platformList.forEach(platform -> platform.update(deltaTime));
            for (int i = 0; i < springList.size(); i++) {
                springList.get(i).update(i == 0);
            }
        }catch (ConcurrentModificationException ignore){}
    }

    public List<Platform> getPlatformList() {
        return platformList;
    }

    public PlatformsSpawner getPlatformsSpawner() {
        return platformsSpawner;
    }
}
