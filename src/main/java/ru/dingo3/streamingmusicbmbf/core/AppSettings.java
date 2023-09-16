package ru.dingo3.streamingmusicbmbf.core;

// singleton style class

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
//import ru.dingo3.streamingmusicbmbf.core.ProviderManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppSettings {
    private static AppSettings instance;

    private static final String configName = "config.json";
    @Getter
    @Setter
    private Path configPath;
    @Getter
    @Setter
    private Path cachePath;
    @Getter
    @Setter
    private String startPage;
    @Getter
    @Setter
    private String converterId;
    @Getter
    @Setter
    private boolean deliveryBMBF;
    @Getter
    @Setter
    private String bmbfApiUrl;

    private AppSettings() {
        setConfigPath(Path.of(System.getProperty("user.home"), ".streamingmusicbmbf", configName));
        readConfig();
    }

    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }
        return instance;
    }

    public void readConfig() {
        // if config path does not exist, create it
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
//                Files.createFile(configPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // read config file json
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = Files.readString(configPath);
            AppSettingsConfig settingsConfig = objectMapper.readValue(json, AppSettingsConfig.class);
            // Use the populated settingsConfig object as needed
            // For example, you can set the cachePath and startPage properties
            setCachePath(Path.of(settingsConfig.getCachePath()));
            setStartPage(settingsConfig.getStartPage());
            setConverterId(settingsConfig.getConverterId());
            setDeliveryBMBF(settingsConfig.isDeliveryBMBF());
            setBmbfApiUrl(settingsConfig.getBmbfApiUrl());
        } catch (IOException e) {
//            System.out.println("Error reading config file");
//            e.printStackTrace();
            setCachePath(Path.of(System.getProperty("user.home"), ".streamingmusicbmbf", "cache"));
            setStartPage("home");
            setConverterId("beatsage");
            setDeliveryBMBF(false);
            setBmbfApiUrl("192.168.1.104:50000");
        }

        // if cache path does not exist, create it
        if (!Files.exists(cachePath)) {
            try {
                Files.createDirectories(cachePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveConfig() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AppSettingsConfig settingsConfig = new AppSettingsConfig();
            settingsConfig.setCachePath(cachePath.toString());
            settingsConfig.setStartPage(startPage);
            settingsConfig.setConverterId(converterId);
            settingsConfig.setDeliveryBMBF(deliveryBMBF);
            settingsConfig.setBmbfApiUrl(bmbfApiUrl);
//            settingsConfig.setConverterName();
            String json = objectMapper.writeValueAsString(settingsConfig);
            Files.writeString(configPath, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

@Data
class AppSettingsConfig {
    private String cachePath;
    private String startPage;
    private String converterId;
    private boolean deliveryBMBF;
    private String bmbfApiUrl;
}