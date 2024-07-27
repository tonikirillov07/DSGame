package com.ds.dj3d.platforms;

import com.ds.dj3d.player.Player;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;

import java.awt.*;

public class Spring {
    private final Object3D springObject;
    private final Player player;
    public static final String SPRING_NAME = "Spring";

    public Spring(Object3D springObject, Player player) {
        this.springObject = springObject;
        this.player = player;
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

    public void update(boolean debug){
        boolean isPlayerAboveSpring = player.getPosition().y < springObject.getTranslation().y - springObject.getScale();

        springObject.setCollisionMode(isPlayerAboveSpring ? Object3D.COLLISION_CHECK_OTHERS : Object3D.COLLISION_CHECK_NONE);
    }

    private float getDistanceAmongSpringAndPlayer(){
        return springObject.getTranslation().distance(player.getPosition());
    }
}
