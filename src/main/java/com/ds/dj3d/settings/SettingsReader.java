package com.ds.dj3d.settings;

import com.ds.engine.utils.ErrorHandler;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.util.Properties;

public final class SettingsReader {
    private static Properties properties;

    private static void loadProperties(){
        try {
            properties = new Properties();
            FileReader fileReader = new FileReader(SettingsConstants.SETTINGS_PATH);

            properties.load(fileReader);
            fileReader.close();
        }catch (Exception e){
            ErrorHandler.processError(e);
        }
    }

    public static @Nullable String getValue(String key){
        loadProperties();

        return properties.getProperty(key);
    }
}
