package com.ds.dj3d;

import com.ds.engine.utils.events.IOnDirectionChanged;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class FromToPointObjectMovement {
    private final float moveXStart, moveXFinal;
    private final Object3D model;
    private byte moveDirection = 1;
    private final float speed;
    private byte previousDirection;
    private IOnDirectionChanged onDirectionChanged;

    public static final byte MOVE_RIGHT = 1;

    public FromToPointObjectMovement(float moveXStart, float moveXFinal, Object3D model, float speed) {
        this.moveXStart = moveXStart;
        this.moveXFinal = moveXFinal;
        this.model = model;
        this.speed = speed;
    }

    public void update(float deltaTime){
        move(deltaTime);
    }

    private void move(float deltaTime) {
        SimpleVector startPointVector = new SimpleVector(moveXStart, model.getTranslation().y, model.getTranslation().z);
        SimpleVector endPointVector = new SimpleVector(moveXFinal, model.getTranslation().y, model.getTranslation().z);

        moveInDirection(startPointVector, endPointVector, deltaTime);
    }

    private void moveInDirection(SimpleVector startPointVector, SimpleVector endPointVector, float deltaTime){
        SimpleVector destinationPoint = moveDirection == MOVE_RIGHT ? startPointVector : endPointVector;

        if (model.getTranslation().distance(destinationPoint) > 1f)
            model.translate((moveDirection == MOVE_RIGHT ? -1 : 1) * speed * deltaTime, 0, 0);
        else
            moveDirection *= -1;

        if(previousDirection != moveDirection)
            onDirectionChanged(moveDirection);
    }

    public void onDirectionChanged(byte direction){
        previousDirection = direction;

        if(onDirectionChanged == null)
            return;

        onDirectionChanged.onChanged(direction);
    }

    public void setOnDirectionChanged(IOnDirectionChanged onDirectionChanged) {
        this.onDirectionChanged = onDirectionChanged;
    }
}
