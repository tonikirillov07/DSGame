package com.ds.dj3d.player;

import com.ds.Constants;
import com.ds.engine.GameWorld;
import com.ds.engine.particles.Particle;
import com.ds.engine.utils.SoundsManager;
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
    private boolean isOnGround = false, previousIsOnGround = false, isJumping = false, isJumpingBySpring = false, isDead;
    private final SimpleVector ELLIPSOID = new SimpleVector(1, 2, 1);
    private float jumpDestinationY = 0;
    private static final float JUMP_SPEED = 10f;
    private static final float PLAYER_SPEED = 10f;
    private List<Particle> particleList;
    private Color particlesColor;
    private float previousAngleToRotate;

    public Player(Object3D[] playerModel, GameWorld gameWorld, Camera camera) {
        this.playerModel = playerModel;
        this.gameWorld = gameWorld;
        this.camera = camera;
    }

    public void init(){
        player = Object3D.mergeAll(playerModel);
        player.setRotationMatrix(new Matrix());
        player.rotateX(Constants.DEGREES_180);
        player.setCollisionMode(Object3D.COLLISION_CHECK_SELF);
        player.translate(new SimpleVector(0, -5f, 0));
        player.setUserObject(this);
        player.build();

        camera.setPosition(new SimpleVector(getPosition().x, getPosition().y, getPosition().z - 20f));
        camera.align(player);
        camera.lookAt(getPosition());

        gameWorld.addObject(player);

        particleList = new ArrayList<>();
    }

    public void jump(float jumpHeight, boolean isOnSpring) {
        if(isDead)
            return;

        isJumpingBySpring = isOnSpring;

        if(isOnGround & !isJumping){
            createParticles();
            SoundsManager.getInstance().playSound("/sounds/jump.ogg");

            jumpDestinationY = getPosition().y - jumpHeight;
            isJumping = true;
        }
    }

    private void move(float deltaTime) {
        if(isDead)
            return;

        byte direction = (byte) ((Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) ? 1 :
                (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) ? -1: 0);

        if(direction == 0)
            return;

        float angleToRotate = direction == 1 ? 180 : -180;
        rotate(angleToRotate);

        player.translate(direction * PLAYER_SPEED * deltaTime * GameWorld.GRAVITY_FORCE, 0f, 0f);
    }

    private void rotate(float angleToRotate) {
        if(previousAngleToRotate == angleToRotate)
            return;

        player.rotateY((float) Math.toRadians(angleToRotate));
        previousAngleToRotate = angleToRotate;
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
        float cameraDistanceX = 40f;
        float cameraDistanceZ = 120f;
        float cameraHeight = 20f;

        float playerRotationY = player.getRotationMatrix().getYAxis().y;

        SimpleVector playerPosition = getPosition();
        float cameraPosX = (playerPosition.x + (cameraDistanceX * (float) Math.sin(playerRotationY)));
        float cameraPosZ = (playerPosition.z + (cameraDistanceZ * (float) Math.cos(playerRotationY)));

        camera.setPosition(new SimpleVector(cameraPosX, playerPosition.y - cameraHeight, cameraPosZ));
        camera.lookAt(playerPosition);
    }

    private void applyGravity(float deltaTime) {
        if(!gameWorld.containsObject(player))
            return;

        SimpleVector gravityVector = new SimpleVector(0, (JUMP_SPEED / 3.5f) * GameWorld.GRAVITY_FORCE * deltaTime, 0);
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

        if(isOnGround & isJumpingBySpring)
            isJumpingBySpring = false;
    }

    private void createParticles() {
        for (int i = 0; i < 10; i++) {
            Particle particle = new Particle(0.3f, 0.2f, 0.3f, particlesColor, gameWorld);

            particle.setOrigin(new SimpleVector(player.getTranslation().x, player.getTranslation().y +  player.getScale(), player.getTranslation().z));
            particle.setRandomVelocity();

            particleList.add(particle);
            gameWorld.addObject(particle);
        }
    }

    public void kill(){
        SoundsManager.getInstance().playSound("/sounds/monsterCrash.ogg");

        player.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
        isDead = true;
    }

    public void makeAlive() {
        player.setCollisionMode(Object3D.COLLISION_CHECK_SELF);
        isDead = false;
    }

    public SimpleVector getPosition() {
        return player.getTranslation();
    }

    public void setPosition(SimpleVector simpleVector){
        isJumping = false;

        player.clearTranslation();
        player.translate(simpleVector);
    }

    public Object3D getPlayer() {
        return player;
    }

    public void setParticlesColor(Color particlesColor) {
        this.particlesColor = particlesColor;
    }
}
