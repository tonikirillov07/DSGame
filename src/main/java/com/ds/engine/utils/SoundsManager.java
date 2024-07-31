package com.ds.engine.utils;

import com.ds.dj3d.settings.SettingsConstants;
import com.ds.dj3d.settings.SettingsReader;
import org.jetbrains.annotations.Nullable;
import org.newdawn.slick.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class SoundsManager {
    private static final Logger log = LoggerFactory.getLogger(SoundsManager.class);
    private static SoundsManager instance = null;
    private final List<Sound> allSoundsList;

    public SoundsManager() {
        allSoundsList = new ArrayList<>();
    }

    public static synchronized SoundsManager getInstance(){
        if(instance == null)
            instance = new SoundsManager();

        return instance;
    }

    public @Nullable Sound playSound(String source){
        try {
            if(Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_SOUNDS_KEY))){
                Sound sound = new Sound(source);
                sound.play();

                allSoundsList.add(sound);

                log.info("Sound {} played and added to list. Sounds in list: {}", source, allSoundsList.size());

                return sound;
            }
        }catch (Exception e){
            ErrorHandler.doError(e);
        }

        return null;
    }

    public Sound loopSound(String source){
        try {
            if(Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_SOUNDS_KEY))){
                Sound sound = new Sound(source);
                sound.loop();

                allSoundsList.add(sound);

                log.info("Sound {} looped and added to list. Sounds in list: {}", sound, allSoundsList.size());

                return sound;
            }
        }catch (Exception e){
            ErrorHandler.doError(e);
        }

        return null;
    }

    public void stopSound(Sound soundObject){
        int soundId = allSoundsList.indexOf(soundObject);
        if(soundId == -1){
            log.error("No sound with {} in sounds list", soundObject);
            return;
        }

        soundObject.stop();
        allSoundsList.remove(soundObject);

        log.info("Sound with id {} stopped and deleted from the list. Sounds in list: {}", soundId, allSoundsList.size());
    }

    public void stopAll(){
        allSoundsList.forEach(Sound::stop);
        allSoundsList.clear();

        log.info("Every sounds stopped and deleted from the list");
    }

    public void update(){
        if(allSoundsList.isEmpty())
            return;

        allSoundsList.removeIf(sound -> !sound.playing());
    }
}
