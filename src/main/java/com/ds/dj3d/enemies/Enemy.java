package com.ds.dj3d.enemies;

import com.ds.Constants;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.ds.engine.utils.ErrorHandler;
import com.ds.engine.utils.Utils;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import org.jetbrains.annotations.NotNull;
import org.newdawn.slick.Sound;

public class Enemy {
    private final Object3D enemyModel;
    private final GameWorld gameWorld;
    private final Player player;
    private boolean isDestroyed;
    private Sound sound;

    public Enemy(@NotNull Object3D enemyModel, GameWorld gameWorld, Player player) {
        this.enemyModel = enemyModel;
        this.gameWorld = gameWorld;
        this.player = player;

        enemyModel.setUserObject(this);

        startSound();
        initCollisionListener();
    }

    private void initCollisionListener() {
        enemyModel.addCollisionListener(new CollisionListener() {
            @Override
            public void collision(CollisionEvent collisionEvent) {
                if(collisionEvent.getSource().getUserObject() instanceof Player player){
                    if(player.getPosition().y < enemyModel.getTranslation().y - enemyModel.getScale()){
                        Utils.playSound("/sounds/monsterJump.ogg");
                        destroy();
                    } else{
                        enemyModel.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
                        player.kill();
                    }
                }
            }

            @Override
            public boolean requiresPolygonIDs() {
                return false;
            }
        });
    }

    public void update(float deltaTime){
        removeEnemyIfPlayerIsFarEnough();
    }

    private void startSound(){
        try {
            sound = new Sound("/sounds/monster.ogg");
            sound.loop();
        }catch (Exception e){
            ErrorHandler.doError(e);
        }
    }

    private void removeEnemyIfPlayerIsFarEnough() {
        if(player.getPosition().distance(enemyModel.getTranslation()) >= Constants.PLATFORM_DELETE_DISTANCE & player.getPosition().y < enemyModel.getTranslation().y)
            destroy();
    }

    private void destroy(){
        if(gameWorld.containsObject(enemyModel))
            gameWorld.removeObject(enemyModel);

        if(sound.playing())
            sound.stop();

        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}