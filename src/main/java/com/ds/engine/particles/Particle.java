package com.ds.engine.particles;

import com.ds.engine.GameWorld;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

import java.awt.*;

public class Particle extends Object3D {
    private SimpleVector velocity;
    private float time;
    private final float aliveTime, speed;
    private final Color color;
    private final GameWorld gameWorld;
    private boolean isDestroyed;

    public Particle(float aliveTime, float speed, float size, Color color, GameWorld gameWorld) {
        super(Primitives.getCube(size));

        this.aliveTime = aliveTime;
        this.speed = speed;
        this.color = color;
        this.gameWorld = gameWorld;

        velocity = new SimpleVector();

        init();
    }

    private void init() {
        setBillboarding(true);
        setTransparency(12);
        enableLazyTransformations();
        setAdditionalColor(color);
        setTranslationMatrix(new Matrix());
        setCollisionMode(COLLISION_CHECK_SELF);
        build();

        reset();
    }

    public void setVelocity(SimpleVector velocity) {
        this.velocity.set(velocity);
    }

    public void setRandomVelocity(){
        velocity.set(new SimpleVector(Math.random(), Math.random(), Math.random()));
    }

    public void reset(){
        time = 0;
        getTranslationMatrix().setIdentity();
    }

    public void update(float deltaTime){
        time += deltaTime;

        if(time >= aliveTime) {
            reset();
            gameWorld.removeObject(this);

            isDestroyed = true;
        }else{
            velocity.add(new SimpleVector(0f, -speed * deltaTime, 0f));
            velocity = checkForCollisionEllipsoid(velocity, new SimpleVector(1f, 1f, 1f), 1);

            translate(velocity);
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
