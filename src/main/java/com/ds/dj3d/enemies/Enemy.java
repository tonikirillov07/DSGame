package com.ds.dj3d.enemies;

import com.ds.Constants;
import com.ds.dj3d.DestroyableObject;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.ds.engine.utils.SoundsManager;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import org.jetbrains.annotations.NotNull;
import org.newdawn.slick.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enemy extends DestroyableObject {
    private static final Logger log = LoggerFactory.getLogger(Enemy.class);
    private final Object3D enemyModel;
    private final GameWorld gameWorld;
    private final Player player;
    private Sound enemySound;
    private boolean isKilled;
    private float destroyTimer;

    public Enemy(@NotNull Object3D enemyModel, GameWorld gameWorld, Player player) {
        this.enemyModel = enemyModel;
        this.gameWorld = gameWorld;
        this.player = player;

        enemyModel.setUserObject(this);

        startSound();
        initCollisionListener();

        log.info("Enemy spawned");
    }

    private void initCollisionListener() {
        enemyModel.addCollisionListener(new CollisionListener() {
            @Override
            public void collision(CollisionEvent collisionEvent) {
                if(collisionEvent.getSource().getUserObject() instanceof Player){
                    enemyModel.setCollisionMode(Object3D.COLLISION_CHECK_NONE);

                    if(player.getPosition().y < enemyModel.getTranslation().y - enemyModel.getScale()){
                        SoundsManager.getInstance().stopSound(enemySound);
                        SoundsManager.getInstance().playSound("/sounds/monsterJump.ogg");
                        
                        player.jump(Player.DEFAULT_PLAYER_JUMP_HEIGHT, false);
                        isKilled = true;
                    } else{
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

        if(isKilled){
            enemyModel.translate(new SimpleVector(0f, 5f * GameWorld.GRAVITY_FORCE * deltaTime, 0f));

            if(destroyTimer < 5)
                destroyTimer += deltaTime;
            else
                destroy();
        }
    }

    private void startSound(){
        enemySound = SoundsManager.getInstance().loopSound("/sounds/monster.ogg");
    }

    private void removeEnemyIfPlayerIsFarEnough() {
        if(player.getPosition().distance(enemyModel.getTranslation()) >= Constants.PLATFORM_DELETE_DISTANCE & player.getPosition().y < enemyModel.getTranslation().y)
            destroy();
    }

    @Override
    public void destroy(){
        super.destroy();

        if(gameWorld.containsObject(enemyModel))
            gameWorld.removeObject(enemyModel);

        log.info("Enemy destroyed");
    }


    public Object3D getEnemyModel() {
        return enemyModel;
    }
}
