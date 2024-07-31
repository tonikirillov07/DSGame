package com.ds.dj3d.enemies.monsters;

import com.ds.Constants;
import com.ds.dj3d.FromToPointObjectMovement;
import com.ds.dj3d.enemies.Enemy;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.threed.jpct.Object3D;
import org.jetbrains.annotations.NotNull;

public class BlueMonster extends Enemy {
    private final FromToPointObjectMovement fromToPointObjectMovement;
    private float previousAngle;

    public BlueMonster(@NotNull Object3D enemyModel, GameWorld gameWorld, Player player) {
        super(enemyModel, gameWorld, player);

        fromToPointObjectMovement = new FromToPointObjectMovement(Constants.GAME_SPACE_START_X, Constants.GAME_SPACE_END_X, enemyModel, 10f);
        fromToPointObjectMovement.setOnDirectionChanged(direction -> rotateMonster(direction == FromToPointObjectMovement.MOVE_RIGHT ? Constants.DEGREES_180 : -Constants.DEGREES_180));
    }

    private void rotateMonster(float angle){
        if(previousAngle == angle)
            return;

        previousAngle = angle;
        getEnemyModel().rotateY(angle);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        fromToPointObjectMovement.update(deltaTime);
    }
}
