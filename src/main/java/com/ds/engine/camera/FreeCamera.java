package com.ds.engine.camera;

import com.ds.dj3d.player.Player;
import com.threed.jpct.Camera;
import com.threed.jpct.SimpleVector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class FreeCamera {
    private static final float FREE_CAMERA_SPEED = 50f;
    private final Camera camera;
    private final SimpleVector position;

    public FreeCamera(SimpleVector position, Camera camera) {
        this.position = position;
        this.camera = camera;
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
