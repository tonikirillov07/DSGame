package com.ds.engine.camera;

import com.threed.jpct.Camera;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreeCamera {
    private static final float FREE_CAMERA_SPEED = 50f;
    private static final Logger log = LoggerFactory.getLogger(FreeCamera.class);
    private final Camera camera;

    public FreeCamera(Camera camera) {
        this.camera = camera;

        log.info("Created free camera");
    }

    public void move(float deltaTime){
        rotate();

        if(Keyboard.isKeyDown(Keyboard.KEY_W))
            camera.moveCamera(Camera.CAMERA_MOVEIN, FREE_CAMERA_SPEED * deltaTime);

        if(Keyboard.isKeyDown(Keyboard.KEY_S))
            camera.moveCamera(Camera.CAMERA_MOVEOUT, FREE_CAMERA_SPEED * deltaTime);

        if(Keyboard.isKeyDown(Keyboard.KEY_A))
            camera.moveCamera(Camera.CAMERA_MOVELEFT, FREE_CAMERA_SPEED * deltaTime);

        if(Keyboard.isKeyDown(Keyboard.KEY_D))
            camera.moveCamera(Camera.CAMERA_MOVERIGHT, FREE_CAMERA_SPEED * deltaTime);

        if(Keyboard.isKeyDown(Keyboard.KEY_Q))
            camera.moveCamera(Camera.CAMERA_MOVEUP, FREE_CAMERA_SPEED * deltaTime);

        if(Keyboard.isKeyDown(Keyboard.KEY_E))
            camera.moveCamera(Camera.CAMERA_MOVEDOWN, FREE_CAMERA_SPEED * deltaTime);
    }

    private void rotate() {
        int dx = Mouse.getDX();
        int dy = Mouse.getDY();

        if (dx != 0)
            camera.rotateAxis(camera.getYAxis(), dx / 500f);

        if (dy != 0)
            camera.rotateX(dy / 500f);
    }
}
