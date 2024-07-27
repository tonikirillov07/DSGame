package com.ds.dj3d.platforms;

import com.ds.dj3d.ScoreManager;
import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import org.jetbrains.annotations.NotNull;

public class MovingPlatform extends Platform{
    private final float moveXStart, moveXFinal;
    private byte moveDirection = 1;
    private float speed = 6f;

    private static final byte MOVE_LEFT = 1;

    public MovingPlatform(@NotNull Object3D model, Player player, GameWorld gameWorld, ScoreManager scoreManager, float moveXStart, float moveXFinal) {
        super(model, player, gameWorld, scoreManager);

        this.moveXStart = moveXStart;
        this.moveXFinal = moveXFinal;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        move(deltaTime);
    }

    private void move(float deltaTime) {
        SimpleVector startPointVector = new SimpleVector(moveXStart, getModel().getTranslation().y, getModel().getTranslation().z);
        SimpleVector endPointVector = new SimpleVector(moveXFinal, getModel().getTranslation().y, getModel().getTranslation().z);

        moveInDirection(startPointVector, endPointVector, deltaTime);
    }

    private void moveInDirection(SimpleVector startPointVector, SimpleVector endPointVector, float deltaTime){
        SimpleVector destinationPoint = moveDirection == MOVE_LEFT ? startPointVector : endPointVector;

        if (getModel().getTranslation().distance(destinationPoint) > 1f)
            getModel().translate((moveDirection == MOVE_LEFT ? -1 : 1) * speed * deltaTime, 0, 0);
        else
            moveDirection *= -1;
    }
}
