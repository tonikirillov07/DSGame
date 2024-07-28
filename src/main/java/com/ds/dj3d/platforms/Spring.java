package com.ds.dj3d.platforms;

import com.ds.Constants;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;

public class Spring {
    private final Object3D springObject;
    private final Player player;
    private final GameWorld gameWorld;
    public static final String SPRING_NAME = "Spring";
    private boolean isDestroyed;

    public Spring(Object3D springObject, Player player, GameWorld gameWorld) {
        this.springObject = springObject;
        this.player = player;
        this.gameWorld = gameWorld;
    }

    public void init() {
        springObject.addCollisionListener(new CollisionListener() {
            @Override
            public void collision(CollisionEvent collisionEvent) {
                System.out.println(1);
            }

            @Override
            public boolean requiresPolygonIDs() {
                return false;
            }
        });
    }

    public void update(){
        boolean isPlayerAboveSpring = player.getPosition().y < springObject.getTranslation().y - springObject.getScale();
        springObject.setCollisionMode(isPlayerAboveSpring ? Object3D.COLLISION_CHECK_OTHERS : Object3D.COLLISION_CHECK_NONE);

        if(isPlayerAboveSpring & getDistanceAmongSpringAndPlayer() >= Constants.PLATFORM_DELETE_DISTANCE){
            if(gameWorld.containsObject(springObject))
                gameWorld.removeObject(springObject);
            isDestroyed = true;
        }
    }

    private float getDistanceAmongSpringAndPlayer(){
        return springObject.getTranslation().distance(player.getPosition());
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
