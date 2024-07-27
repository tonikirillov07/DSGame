package com.ds.dj3d.settings;

import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.util.Properties;

public final class SettingsReader {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            FileReader fileReader = new FileReader(SettingsConstants.SETTINGS_PATH);

            properties.load(fileReader);
            fileReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static @Nullable String getValue(String key){
        return properties.getProperty(key);
    }
}
