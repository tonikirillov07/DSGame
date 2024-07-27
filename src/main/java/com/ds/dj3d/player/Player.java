package com.ds.dj3d.player;

import com.ds.engine.GameWorld;
import com.ds.engine.particles.Particle;
import com.ds.engine.utils.Utils;
import com.threed.jpct.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private final Object3D[] playerModel;
    private final GameWorld gameWorld;
    private final Camera camera;
    private Object3D player;
    private boolean isOnGround = false, previousIsOnGround = false, isJumping = false;
    private final SimpleVector ELLIPSOID = new SimpleVector(2, 2, 2);
    private float jumpDestinationY = 0;
    private static final float JUMP_SPEED = 10f;
    private static final float PLAYER_SPEED = 10f;
    private static final float DEFAULT_JUMP_HEIGHT = 20f;
    private List<Particle> particleList;

    public Player(Object3D[] playerModel, GameWorld gameWorld, Camera camera) {
        this.playerModel = playerModel;
        this.gameWorld = gameWorld;
        this.camera = camera;
    }

    public void init(){
        player = Object3D.mergeAll(playerModel);
        player.rotateX((float) Math.toRadians(180));
        player.setCollisionMode(Object3D.COLLISION_CHECK_SELF);
        player.translate(new SimpleVector(0, -5f, 0));
        player.setName("Player");
        player.build();

        camera.setPosition(new SimpleVector(getPosition().x, getPosition().y, getPosition().z - 20f));
        camera.align(player);
        camera.lookAt(getPosition());

        gameWorld.addObject(player);

        particleList = new ArrayList<>();
    }

    public void jump(float jumpHeight) {
        if(isOnGround & !isJumping){
            jumpDestinationY = getPosition().y - jumpHeight;
            isJumping = true;
        }
    }

    private void move(float deltaTime) {
        byte direction;
        direction = (byte) (Keyboard.isKeyDown(Keyboard.KEY_A) ? 1 : Keyboard.isKeyDown(Keyboard.KEY_D) ? -1: 0);

        if(direction == 0)
            return;

        player.translate(direction * PLAYER_SPEED * deltaTime * GameWorld.GRAVITY_FORCE, 0f, 0f);
    }

    public void update(float deltaTime) {
        applyCamera();
        move(deltaTime);
        applyGravity(deltaTime);

        if(Keyboard.isKeyDown(Keyboard.KEY_P))
            System.out.println(getPosition());

        Iterator<Particle> particleIterator = particleList.iterator();
        while (particleIterator.hasNext()){
            Particle particle = particleIterator.next();

            if(particle.isDestroyed())
                particleIterator.remove();
            else
                particle.update(deltaTime);
        }
    }

    private void applyCamera() {
        camera.align(player);
        camera.rotateZ((float) Math.toRadians(180));
        camera.rotateY((float) Math.toRadians(30));
        camera.rotateX((float) Math.toRadians(-20));
        camera.setPosition(new SimpleVector(getPosition().x - 30, getPosition().y - 20, getPosition().z + 90));
    }

    private void applyGravity(float deltaTime) {
        if(!gameWorld.containsObject(player))
            return;

        SimpleVector gravityVector = new SimpleVector(0, (JUMP_SPEED / 4) * GameWorld.GRAVITY_FORCE * deltaTime, 0);
        SimpleVector gravityVectorOriginal = gravityVector;

        if(isJumping) {
            gravityVector = new SimpleVector(0, -JUMP_SPEED * GameWorld.GRAVITY_FORCE * deltaTime, 0);

            if (getPosition().y <= jumpDestinationY) {
                isJumping = false;
            }
        }

        gravityVector = player.checkForCollisionEllipsoid(gravityVector, ELLIPSOID,1);
        player.translate(gravityVector);

        isOnGround = !gravityVector.equals(gravityVectorOriginal);

        if(previousIsOnGround != isOnGround)
            onIsOnGroundChanged(isOnGround);
    }

    private void onIsOnGroundChanged(boolean isOnGround){
        previousIsOnGround = isOnGround;

        if(isOnGround) {
            createParticles();

            jump(DEFAULT_JUMP_HEIGHT);
            Utils.playSound("/sounds/Jump.ogg");
        }
    }

    private void createParticles() {
        for (int i = 0; i < 10; i++) {
            Particle particle = new Particle(0.3f, 0.2f, 0.3f, Color.RED, gameWorld);

            particle.setOrigin(new SimpleVector(player.getTranslation().x, player.getTranslation().y +  player.getScale(), player.getTranslation().z));
            particle.setVelocity(new SimpleVector(1 - Math.random() * 1,  1 - (Math.random() / 2f), 1 - Math.random() * 1));

            particleList.add(particle);
            gameWorld.addObject(particle);
        }
    }

    public SimpleVector getPosition() {
        return player.getTranslation();
    }

    public void setPosition(SimpleVector simpleVector){
        player.translate(simpleVector);
    }

    public Object3D getPlayer() {
        return player;
    }
}