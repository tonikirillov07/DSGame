package com.ds.dj3d.player;

import com.ds.engine.GameWorld;
import com.threed.jpct.Camera;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Sound;

public class Player {
    private final Object3D[] playerModel;
    private final GameWorld gameWorld;
    private final Camera camera;
    private Object3D player;
    private boolean isOnGround = true, previousIsOnGround = true, isJumping = false;
    private final SimpleVector ELLIPSOID = new SimpleVector(2, 2, 2);
    private float jumpDestinationY = 0;
    private static final float JUMP_SPEED = 5f;
    private static final float PLAYER_SPEED = 5f;

    public Player(Object3D[] playerModel, GameWorld gameWorld, Camera camera) {
        this.playerModel = playerModel;
        this.gameWorld = gameWorld;
        this.camera = camera;

        init();
    }

    private void init(){
        player = Object3D.mergeAll(playerModel);
        player.rotateX((float) Math.toRadians(180));
        player.setCollisionMode(Object3D.COLLISION_CHECK_SELF);
        player.translate(new SimpleVector(0, -5f, 0));
        player.build();

        camera.setPosition(new SimpleVector(getPosition().x, getPosition().y, getPosition().z - 20f));
        camera.align(player);
        camera.lookAt(getPosition());

        gameWorld.addObject(player);
    }

    private void jump() {
        if(isOnGround & !isJumping){
            jumpDestinationY = getPosition().y - 20;
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
        //applyCamera();
        move(deltaTime);
        applyGravity(deltaTime);

        if(Keyboard.isKeyDown(Keyboard.KEY_P))
            System.out.println(getPosition());
    }

    private void applyCamera() {
        camera.align(player);
        camera.lookAt(player.getTransformedCenter());
        camera.setPosition(new SimpleVector(getPosition().x, getPosition().y, getPosition().z + 20));
    }

    private void applyGravity(float deltaTime) {
        SimpleVector gravityVector = new SimpleVector(0, GameWorld.GRAVITY_FORCE * deltaTime, 0);
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
        try {
            previousIsOnGround = isOnGround;

            if(isOnGround) {
                jump();

                Sound sound = new Sound("/sounds/Jump.ogg");
                sound.play();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public SimpleVector getPosition() {
        return player.getTranslation();
    }
}
