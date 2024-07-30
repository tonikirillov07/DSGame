package com.ds.engine.utils;

import com.ds.dj3d.settings.SettingsConstants;
import com.ds.dj3d.settings.SettingsReader;
import org.newdawn.slick.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SoundsManager {
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

    public int playSound(String source){
        try {
            if(Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_SOUNDS_KEY))){
                Sound sound = new Sound(source);
                sound.play();

                allSoundsList.add(sound);

                log.info("Sound {} played and added to list. Sounds in list: {}", source, allSoundsList.size());

                return allSoundsList.size() - 1;
            }
        }catch (Exception e){
            ErrorHandler.doError(e);
        }

        return -1;
    }

    public int loopSound(String source){
        try {
            if(Boolean.parseBoolean(SettingsReader.getValue(SettingsConstants.USE_SOUNDS_KEY))){
                Sound sound = new Sound(source);
                sound.loop();

                allSoundsList.add(sound);

                log.info("Sound {} looped and added to list. Sounds in list: {}", sound, allSoundsList.size());

                return allSoundsList.size() - 1;
            }
        }catch (Exception e){
            ErrorHandler.doError(e);
        }

        return -1;
    }

    public void stopSoundById(int id){
        Sound sound = allSoundsList.get(id);
        if(sound == null){
            log.error("No sound with id {} in sounds list", id);
            return;
        }

        sound.stop();
        allSoundsList.remove(id);

        log.info("Sound with id {} stopped and deleted from the list. Sounds in list: {}", id, allSoundsList.size());
    }

    public void stopAll(){
        allSoundsList.forEach(Sound::stop);
        allSoundsList.clear();

        log.info("Every sounds stopped and deleted from the list");
    }

    public void update(){
        allSoundsList.removeIf(sound -> !sound.playing());
    }
}
