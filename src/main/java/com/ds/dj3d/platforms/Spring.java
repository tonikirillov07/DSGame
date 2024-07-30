package com.ds.dj3d.platforms;

import com.ds.Constants;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.ds.engine.utils.SoundsManager;
import com.ds.engine.utils.Utils;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;

public class Spring {
    private final Object3D springObject;
    private final Player player;
    private final GameWorld gameWorld;

    public Spring(Object3D springObject, Player player, GameWorld gameWorld) {
        this.springObject = springObject;
        this.player = player;
        this.gameWorld = gameWorld;
    }

    public void init() {
        springObject.addCollisionListener(new CollisionListener() {
            @Override
            public void collision(CollisionEvent collisionEvent) {
                SoundsManager.getInstance().playSound("/sounds/spring.ogg");
                player.jump(90f, true);
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


    }

    private float getDistanceAmongSpringAndPlayer(){
        return springObject.getTranslation().distance(player.getPosition());
    }

    public Object3D getSpringObject() {
        return springObject;
    }
}
