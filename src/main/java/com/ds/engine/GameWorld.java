package com.ds.engine;

import com.ds.Main;
import com.ds.engine.gameWorld.SkyboxConstants;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.SkyBox;

public class GameWorld extends World {
    public static final float GRAVITY_FORCE = 9.81f;
    private SkyBox skyBox;

    public GameWorld() {
        initSkybox();
    }

    public void update(FrameBuffer frameBuffer){
        renderScene(frameBuffer);
        draw(frameBuffer);

        updateSkybox(frameBuffer);
    }

    private void initSkybox(){
        prepareSkyboxTextures();
        skyBox = new SkyBox(SkyboxConstants.SKYBOX_LEFT_NAME, SkyboxConstants.SKYBOX_FRONT_NAME, SkyboxConstants.SKYBOX_RIGHT_NAME,
                SkyboxConstants.SKYBOX_BACK_NAME, SkyboxConstants.SKYBOX_UP_NAME, SkyboxConstants.SKYBOX_DOWN_NAME, 500f);
    }

    private void updateSkybox(FrameBuffer frameBuffer){
        skyBox.render(this, frameBuffer);
    }

    private void prepareSkyboxTextures(){
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

        skyBox.dispose();
    }
}
