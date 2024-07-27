package com.ds.engine.particles;

import com.ds.engine.GameWorld;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Particle extends Object3D {
    private final SimpleVector velocity;
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
        setVisibility(OBJ_VISIBLE);
        setTransparency(12);
        setLighting(LIGHTING_NO_LIGHTS);
        enableLazyTransformations();
        setAdditionalColor(color);
        setTranslationMatrix(new Matrix());
        build();

        reset();
    }

    public void setVelocity(SimpleVector velocity) {
        this.velocity.set(velocity);
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
            translate(velocity);
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
