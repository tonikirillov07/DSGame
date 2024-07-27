package com.ds;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import com.ds.dj3d.settings.SettingsReader;
import com.threed.jpct.*;
import com.threed.jpct.util.*;

import org.lwjgl.input.*;

public class Test implements IPaintListener {
    private KeyMapper keyMapper = null;
    private MouseMapper mouseMapper = null;

    private FrameBuffer buffer = null;

    private World world = null;
    private World sky = null;

    private Object3D plane = null;
    private Object3D snork = null;
    private Object3D rock = null;
    private Object3D dome = null;

    private Light sun = null;

    private ShadowHelper sh = null;
    private Projector projector = null;

    private float xAngle = 0;

    private boolean forward = false;
    private boolean backward = false;
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    private float ind = 0;
    private boolean doLoop = true;
    private int fps = 0;
    private long time = System.currentTimeMillis();
    private Ticker ticker = new Ticker(15);

    public static void main(String[] args) throws Exception {
        float toAngle = (float) Math.toRadians(30);
        List<Float> angles = new ArrayList<>();

        for (float i = 0; i < toAngle; i += (float) Math.toRadians(1)) {
            angles.add(i);
        }

        System.out.println(angles);
    }

    public Test() {
        Config.glAvoidTextureCopies = true;
        Config.maxPolysVisible = 1000;
        Config.glColorDepth = 24;
        Config.glFullscreen = false;
        Config.farPlane = 4000;
        Config.glShadowZBias = 0.8f;
        Config.lightMul = 1;
        Config.collideOffset = 500;
        Config.glTrilinear = true;
    }

    public void finishedPainting() {
        fps++;
    }

    public void startPainting() {
    }

    private void init() throws Exception {

        // Load textures

        TextureManager tm = TextureManager.getInstance();
        tm.addTexture("grass", new Texture("example/GrassSample2.jpg"));
        tm.addTexture("disco", new Texture("example/disco.jpg"));
        tm.addTexture("rock", new Texture("example/rock.jpg"));
        tm.addTexture("normals", new Texture("example/normals.jpg"));
        tm.addTexture("sky", new Texture("example/sky.jpg"));

        // Initialize frame buffer

        buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        buffer.enableRenderer(IRenderer.RENDERER_OPENGL, IRenderer.MODE_OPENGL);
        buffer.setPaintListener(this);

        // Initialize worlds

        world = new World();
        sky = new World();
        world.setAmbientLight(30, 30, 30);
        sky.setAmbientLight(255, 255, 255);

        world.getLights().setRGBScale(Lights.RGB_SCALE_2X);
        sky.getLights().setRGBScale(Lights.RGB_SCALE_2X);

        // Initialize mappers

        keyMapper = new KeyMapper();
        mouseMapper = new MouseMapper(buffer);
        mouseMapper.hide();

        // Load/create and setup objects

        plane = Primitives.getPlane(20, 30);

        float PI = (float) Math.PI;
        plane.rotateX(PI / 2f);
        plane.setSpecularLighting(true);
        plane.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);

        rock = Primitives.getBox(2, 2);
        rock.translate(0, 0, -90);
        rock.rotateX(-PI / 2);
        rock.setAdditionalColor(Color.GRAY);
        rock.setSpecularLighting(true);

        snork = Primitives.getBox(3, 3);
        snork.translate(0, -25, -50);

        dome = Primitives.getBox(1, 1);
        dome.build();
        dome.rotateX(-PI / 2f);
        dome.calcTextureWrap();
        tileTexture(dome, 3);
        dome.translate(plane.getTransformedCenter().calcSub(dome.getTransformedCenter()));
        dome.setLighting(Object3D.LIGHTING_NO_LIGHTS);
        dome.setAdditionalColor(Color.WHITE);

        world.addObject(plane);
        world.addObject(snork);
        world.addObject(rock);
        sky.addObject(dome);

        // Build all world's objects

        world.buildAllObjects();

        // Compile all objects for better performance

        plane.compileAndStrip();
        rock.compileAndStrip();
        dome.compileAndStrip();

        snork.compile(true, true, true, false, 2000);
        snork.setCollisionMode(Object3D.COLLISION_CHECK_SELF);

        // Deform the plane

        Mesh planeMesh = plane.getMesh();
        planeMesh.setVertexController(new Mod(), false);
        planeMesh.applyVertexController();
        planeMesh.removeVertexController();

        // Initialize shadow helper

        projector = new Projector();
        projector.setFOV(1.5f);
        projector.setYFOV(1.5f);

        sh = new ShadowHelper(world, buffer, projector, 2048);
        sh.setCullingMode(false);
        sh.setAmbientLight(new Color(30, 30, 30));
        sh.setLightMode(true);
        sh.setBorder(1);

        sh.addCaster(snork);
        sh.addCaster(rock);

        sh.addReceiver(plane);

        // Setup dynamic light source

        sun = new Light(world);
        sun.setIntensity(250, 250, 250);
        sun.setAttenuation(800);
        // Move camera

        Camera cam = world.getCamera();
        cam.moveCamera(Camera.CAMERA_MOVEOUT, 150);
        cam.moveCamera(Camera.CAMERA_MOVEUP, 100);
        cam.lookAt(plane.getTransformedCenter());
        cam.setFOV(1.5f);
    }

    private void pollControls() {

        KeyState ks = null;
        while ((ks = keyMapper.poll()) != KeyState.NONE) {
            if (ks.getKeyCode() == KeyEvent.VK_ESCAPE) {
                doLoop = false;
            }

            if (ks.getKeyCode() == KeyEvent.VK_UP) {
                forward = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_DOWN) {
                backward = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_LEFT) {
                left = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                up = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                down = ks.getState();
            }
        }

        if (org.lwjgl.opengl.Display.isCloseRequested()) {
            doLoop = false;
        }
    }

    private void move(long ticks) {

        if (ticks == 0) {
            return;
        }

        // Key controls

        SimpleVector ellipsoid = new SimpleVector(5, 5, 5);

        if (forward) {
            world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEIN,
                    ellipsoid, ticks, 5);
        }

        if (backward) {
            world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEOUT,
                    ellipsoid, ticks, 5);
        }

        if (left) {
            world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVELEFT,
                    ellipsoid, ticks, 5);
        }

        if (right) {
            world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVERIGHT,
                    ellipsoid, ticks, 5);
        }

        if (up) {
            world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEUP,
                    ellipsoid, ticks, 5);
        }

        if (down) {
            world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEDOWN,
                    ellipsoid, ticks, 5);
        }

        // mouse rotation

        Matrix rot = world.getCamera().getBack();
        int dx = mouseMapper.getDeltaX();
        int dy = mouseMapper.getDeltaY();

        float ts = 0.2f * ticks;
        float tsy = ts;

        if (dx != 0) {
            ts = dx / 500f;
        }
        if (dy != 0) {
            tsy = dy / 500f;
        }

        if (dx != 0) {
            rot.rotateAxis(rot.getYAxis(), ts);
        }

        if ((dy > 0 && xAngle < Math.PI / 4.2)
                || (dy < 0 && xAngle > -Math.PI / 4.2)) {
            rot.rotateX(tsy);
            xAngle += tsy;
        }

        sky.getCamera().setBack(world.getCamera().getBack().cloneMatrix());
        dome.rotateY(0.00005f * ticks);
    }

    private void gameLoop() throws Exception {
        SimpleVector pos = snork.getTransformedCenter();
        SimpleVector offset = new SimpleVector(1, 0, -1).normalize();

        long ticks = 0;

        while (doLoop) {

            ticks = ticker.getTicks();
            if (ticks > 0) {
                // animate the snork and the dome

                animate(ticks);
                offset.rotateY(0.007f * ticks);

                // move the camera

                pollControls();
                move(ticks);
            }

            // update the projector for the shadow map

            projector.lookAt(plane.getTransformedCenter());
            projector.setPosition(pos);
            projector.moveCamera(new SimpleVector(0, -1, 0), 200);
            projector.moveCamera(offset, 215);
            sun.setPosition(projector.getPosition());

            // update the shadow map

            sh.updateShadowMap();

            // render the scene

            buffer.clear();

            buffer.setPaintListenerState(false);
            sky.renderScene(buffer);
            sky.draw(buffer);
            buffer.setPaintListenerState(true);
            sh.drawScene();
            buffer.update();
            buffer.displayGLOnly();

            // print out the fps to the console

            if (System.currentTimeMillis() - time >= 1000) {
                System.out.println(fps);
                fps = 0;
                time = System.currentTimeMillis();
            }
        }

        // exit...

        System.exit(0);
    }

    private void animate(long ticks) {
        if (ticks > 0) {
            float ft = (float) ticks;
            ind += 0.02f * ft;
            if (ind > 1) {
                ind -= 1;
            }
            snork.animate(ind, 2);
            snork.rotateY(-0.02f * ft);
            snork.translate(0, -50, 0);
            SimpleVector dir = snork.getXAxis();
            dir.scalarMul(ft);
            dir = snork.checkForCollisionEllipsoid(dir, new SimpleVector(5, 20,	5), 5);
            snork.translate(dir);
            dir = snork.checkForCollisionEllipsoid(new SimpleVector(0, 100, 0),	new SimpleVector(5, 20, 5), 1);
            snork.translate(dir);
        }
    }

    private void tileTexture(Object3D obj, float tileFactor) {
        PolygonManager pm = obj.getPolygonManager();

        int end = pm.getMaxPolygonID();
        for (int i = 0; i < end; i++) {
            SimpleVector uv0 = pm.getTextureUV(i, 0);
            SimpleVector uv1 = pm.getTextureUV(i, 1);
            SimpleVector uv2 = pm.getTextureUV(i, 2);

            uv0.scalarMul(tileFactor);
            uv1.scalarMul(tileFactor);
            uv2.scalarMul(tileFactor);

            int id = pm.getPolygonTexture(i);

            TextureInfo ti = new TextureInfo(id, uv0.x, uv0.y, uv1.x, uv1.y,
                    uv2.x, uv2.y);
            pm.setPolygonTexture(i, ti);
        }
    }

    private static class MouseMapper {

        private boolean hidden = false;

        private int height = 0;

        public MouseMapper(FrameBuffer buffer) {
            height = buffer.getOutputHeight();
            init();
        }

        public void hide() {
            if (!hidden) {
                Mouse.setGrabbed(true);
                hidden = true;
            }
        }

        public void show() {
            if (hidden) {
                Mouse.setGrabbed(false);
                hidden = false;
            }
        }

        public boolean isVisible() {
            return !hidden;
        }

        public void destroy() {
            show();
            if (Mouse.isCreated()) {
                Mouse.destroy();
            }
        }

        public boolean buttonDown(int button) {
            return Mouse.isButtonDown(button);
        }

        public int getMouseX() {
            return Mouse.getX();
        }

        public int getMouseY() {
            return height - Mouse.getY();
        }

        public int getDeltaX() {
            if (Mouse.isGrabbed()) {
                return Mouse.getDX();
            } else {
                return 0;
            }
        }

        public int getDeltaY() {
            if (Mouse.isGrabbed()) {
                return Mouse.getDY();
            } else {
                return 0;
            }
        }

        private void init() {
            try {
                if (!Mouse.isCreated()) {
                    Mouse.create();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Mod extends GenericVertexController {
        private static final long serialVersionUID = 1L;

        public void apply() {
            SimpleVector[] s = getSourceMesh();
            SimpleVector[] d = getDestinationMesh();
            for (int i = 0; i < s.length; i++) {
                d[i].z = s[i].z
                        - (10f * ((float) Math.sin(s[i].x / 50f) + (float) Math.cos(s[i].y / 50f)));
                d[i].x = s[i].x;
                d[i].y = s[i].y;
            }
        }
    }

    private static class Ticker {

        private int rate;
        private long s2;

        public static long getTime() {
            return System.currentTimeMillis();
        }

        public Ticker(int tickrateMS) {
            rate = tickrateMS;
            s2 = Ticker.getTime();
        }

        public int getTicks() {
            long i = Ticker.getTime();
            if (i - s2 > rate) {
                int ticks = (int) ((i - s2) / (long) rate);
                s2 += (long) rate * ticks;
                return ticks;
            }
            return 0;
        }
    }
}
