package com.ds.engine.utils;

import com.ds.dj3d.settings.SettingsConstants;
import com.ds.dj3d.settings.SettingsReader;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.List;

public class SoundsManager {
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

                return allSoundsList.size() - 1;
            }
        }catch (Exception e){
            ErrorHandler.doError(e);
        }

        return -1;
    }

    public void stopSoundById(int id){
        Sound sound = allSoundsList.get(id);
        if(sound == null)
            return;

        sound.stop();
        allSoundsList.remove(id);
    }

    public void stopAll(){
        allSoundsList.forEach(Sound::stop);
        allSoundsList.clear();
    }
}
