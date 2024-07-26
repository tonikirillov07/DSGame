package com.ds.dj3d.platforms;

import com.ds.dj3d.player.Player;
import com.threed.jpct.Object3D;
import org.jetbrains.annotations.NotNull;

public class Platform {
    private final Object3D model;
    private final Player player;

    public Platform(@NotNull Object3D model, Player player) {
        this.model = model;
        this.player = player;
    }

    public void update() {
        if(player.getPosition().y > model.getTranslation().y)
            model.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
        else
            model.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
    }

    public Object3D getModel() {
        return model;
    }
}
