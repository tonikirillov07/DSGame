package com.ds.dj3d.platforms;

import com.ds.dj3d.FromToPointObjectMovement;
import com.ds.dj3d.ScoreManager;
import com.ds.dj3d.platforms.platformsManaging.PlatformsManager;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.threed.jpct.Object3D;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MovingPlatform extends Platform{
    private final FromToPointObjectMovement fromToPointObjectMovement;

    public MovingPlatform(@NotNull Object3D model, Player player, GameWorld gameWorld, ScoreManager scoreManager, PlatformsManager platformsManager, float moveXStart, float moveXFinal, Color color) {
        super(model, player, gameWorld, platformsManager, scoreManager, color);

        fromToPointObjectMovement = new FromToPointObjectMovement(moveXStart, moveXFinal, model, 6f);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        fromToPointObjectMovement.update(deltaTime);
    }
}
