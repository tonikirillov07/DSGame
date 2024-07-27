package com.ds.engine.shadows;

import com.ds.dj3d.player.Player;
import com.ds.engine.GameWorld;
import com.threed.jpct.*;
import com.threed.jpct.util.ShadowHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class ShadowsManager {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ShadowsManager.class);
    private Projector projector;
    private ShadowHelper shadowHelper;
    private static final float PROJECTOR_FOV = 1.5f;
    private final GameWorld gameWorld;
    private final FrameBuffer frameBuffer;

    public ShadowsManager(GameWorld gameWorld, FrameBuffer frameBuffer) {
        this.gameWorld = gameWorld;
        this.frameBuffer = frameBuffer;
    }

    public void init(){
        initProjector();
        initShadowsHelper();
    }

    private void initShadowsHelper() {
        log.info("Initializing shadows helper...");

        shadowHelper = new ShadowHelper(gameWorld, frameBuffer, projector, 2048);
        shadowHelper.setCullingMode(false);
        shadowHelper.setAmbientLight(new Color(20, 20, 20));
        shadowHelper.setLightMode(true);
        shadowHelper.setBorder(1);
    }

    private void initProjector() {
        log.info("Initializing projector...");

        projector = new Projector();
        projector.setFOV(PROJECTOR_FOV);
        projector.setYFOV(PROJECTOR_FOV);
    }

    public void update(@NotNull Player player){
        projector.lookAt(player.getPlayer().getTransformedCenter());
        projector.setPosition(player.getPosition());
        projector.moveCamera(new SimpleVector(0, -1, 0), 100);
        projector.moveCamera(new SimpleVector(-1, 0, 0), 30);

        shadowHelper.updateShadowMap();
        shadowHelper.drawScene();
    }

    public void dispose(){
        shadowHelper.dispose();
    }

    public ShadowHelper getShadowHelper() {
        return shadowHelper;
    }
}
