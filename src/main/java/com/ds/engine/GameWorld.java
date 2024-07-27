package com.ds.engine;

import com.ds.Main;
import com.ds.engine.gameWorld.SkyboxConstants;
import com.threed.jpct.*;
import com.threed.jpct.util.SkyBox;
import org.slf4j.LoggerFactory;

public class GameWorld extends World {
    public static final float GRAVITY_FORCE = 9.81f;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameWorld.class);
    private SkyBox skyBox;

    public GameWorld() {
        initSkybox();
        initLight();
        initFog();
    }

    private void initFog() {
        setFogging(FOGGING_ENABLED);
        setFogParameters(400, 255, 255, 255);
        setFoggingMode(World.FOGGING_PER_PIXEL);
    }

    private void initLight() {
        log.info("Initializing light settings...");

        Config.fadeoutLight = true;
        Config.linearDiv = 100f;
        Config.lightDiscardDistance = 350f;
        getLights().setOverbrightLighting(Lights.OVERBRIGHT_LIGHTING_DISABLED);
        getLights().setRGBScale(Lights.RGB_SCALE_2X);
        setAmbientLight(100, 105, 105);
    }

    public void update(FrameBuffer frameBuffer){
        renderScene(frameBuffer);
        draw(frameBuffer);

        updateSkybox(frameBuffer);
    }

    private void initSkybox(){
        log.info("Initializing skybox...");

        prepareSkyboxTextures();
        skyBox = new SkyBox(SkyboxConstants.SKYBOX_LEFT_NAME, SkyboxConstants.SKYBOX_FRONT_NAME, SkyboxConstants.SKYBOX_RIGHT_NAME,
                SkyboxConstants.SKYBOX_BACK_NAME, SkyboxConstants.SKYBOX_UP_NAME, SkyboxConstants.SKYBOX_DOWN_NAME, 500f);
    }

    private void updateSkybox(FrameBuffer frameBuffer){
        skyBox.render(this, frameBuffer);
    }

    private void prepareSkyboxTextures(){
        log.info("Preparing skybox textures...");

        TextureManager textureManager = TextureManager.getInstance();
        textureManager.addTexture(SkyboxConstants.SKYBOX_BACK_NAME, new Texture(Main.class.getResourceAsStream(SkyboxConstants.SKYBOX_BACK)));
        textureManager.addTexture(SkyboxConstants.SKYBOX_FRONT_NAME, new Texture(Main.class.getResourceAsStream(SkyboxConstants.SKYBOX_FRONT)));
        textureManager.addTexture(SkyboxConstants.SKYBOX_LEFT_NAME, new Texture(Main.class.getResourceAsStream(SkyboxConstants.SKYBOX_LEFT)));
        textureManager.addTexture(SkyboxConstants.SKYBOX_RIGHT_NAME, new Texture(Main.class.getResourceAsStream(SkyboxConstants.SKYBOX_RIGHT)));
        textureManager.addTexture(SkyboxConstants.SKYBOX_UP_NAME, new Texture(Main.class.getResourceAsStream(SkyboxConstants.SKYBOX_UP)));
        textureManager.addTexture(SkyboxConstants.SKYBOX_DOWN_NAME, new Texture(Main.class.getResourceAsStream(SkyboxConstants.SKYBOX_DOWN)));
    }

    @Override
    public void dispose() {
        super.dispose();
        
        log.info("Disposing world...");
        skyBox.dispose();
    }
}
