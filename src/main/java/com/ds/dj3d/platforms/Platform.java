package com.ds.dj3d.platforms;

import com.ds.dj3d.ScoreManager;
import com.ds.dj3d.platforms.platformsManaging.PlatformsManager;
import com.ds.dj3d.player.Player;
import com.ds.Constants;
import com.ds.engine.GameWorld;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Platform {
    private final Object3D model;
    private final Player player;
    private final GameWorld gameWorld;
    private boolean isCreateMorePlatformWhenPlayerIsNear;
    private final PlatformsManager platformsManager;
    private boolean canGiveScore = true;
    private final ScoreManager scoreManager;

    public Platform(@NotNull Object3D model, Player player, GameWorld gameWorld, PlatformsManager platformsManager, ScoreManager scoreManager, Color particlesColor) {
        this.model = model;
        this.player = player;
        this.gameWorld = gameWorld;
        this.platformsManager = platformsManager;
        this.scoreManager = scoreManager;

        model.addCollisionListener(new CollisionListener() {
            @Override
            public void collision(CollisionEvent collisionEvent) {
                if(collisionEvent.getSource().getUserObject() instanceof Player player) {
                    player.setParticlesColor(particlesColor);
                    player.jump(20f, false);
                }
            }

            @Override
            public boolean requiresPolygonIDs() {
                return false;
            }
        });
    }

    public void update(float deltaTime) {
        boolean isPlayerAbovePlatform = player.getPosition().y < model.getTranslation().y - (model.getScale() + 1);
        model.setCollisionMode(isPlayerAbovePlatform ? Object3D.COLLISION_CHECK_OTHERS : Object3D.COLLISION_CHECK_NONE);

        tryGivePlayerScoreIsHeIsNear();
        createMorePlatformsWhenPlayerIsNear();
        removeWhenPlayerIsFarEnough();
    }

    private void tryGivePlayerScoreIsHeIsNear() {
        if(canGiveScore){
            if((getDistanceAmongPlayerAndPlatform() <= 10f) & (getModel().getTranslation().y > player.getPosition().y)){
                scoreManager.addScore();
                canGiveScore = false;
            }
        }
    }

    private void createMorePlatformsWhenPlayerIsNear() {
        if (isCreateMorePlatformWhenPlayerIsNear){
            if(getDistanceAmongPlayerAndPlatform() <= 30f){
                if(platformsManager == null)
                    return;

                platformsManager.getPlatformsSpawner().create(10,false);
                isCreateMorePlatformWhenPlayerIsNear = false;
            }
        }
    }

    private void removeWhenPlayerIsFarEnough() {
        if(getDistanceAmongPlayerAndPlatform() >= Constants.PLATFORM_DELETE_DISTANCE & (model.getTranslation().y > player.getPosition().y)){
            if(gameWorld.containsObject(model)){
                gameWorld.removeObject(model);
                platformsManager.getPlatformList().remove(this);
            }
        }
    }

    private float getDistanceAmongPlayerAndPlatform(){
        return player.getPosition().distance(model.getTranslation());
    }

    public void setCreateMorePlatformWhenPlayerIsNear(boolean createMorePlatformWhenPlayerIsNear) {
        isCreateMorePlatformWhenPlayerIsNear = createMorePlatformWhenPlayerIsNear;
    }

    public Object3D getModel() {
        return model;
    }
}
