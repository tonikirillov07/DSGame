package com.ds.dj3d.settings;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public final class SettingsWriter {
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

    public static void writeValue(String key, String newValue){
        try {
            FileWriter fileWriter = new FileWriter(SettingsConstants.SETTINGS_PATH);

            properties.setProperty(key, newValue);
            properties.store(fileWriter, null);
            fileWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
