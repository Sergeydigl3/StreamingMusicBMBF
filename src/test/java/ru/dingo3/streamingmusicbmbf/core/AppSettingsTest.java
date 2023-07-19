package ru.dingo3.streamingmusicbmbf.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AppSettingsTest {
    private AppSettings appSettings;

    @BeforeEach
    void setUp() {
        appSettings = AppSettings.getInstance();
    }

    @Test
    void testGetInstance() {
        // Ensure that the instance returned by getInstance is not null
        assertNotNull(appSettings);
        // Ensure that multiple calls to getInstance return the same instance
        assertEquals(appSettings, AppSettings.getInstance());
    }

    @Test
    void testReadConfig() {
        // Ensure that configPath is not null after reading the config
        assertNotNull(appSettings.getConfigPath());
        // Ensure that cachePath is not null after reading the config
        assertNotNull(appSettings.getCachePath());
        // Ensure that startPage is not null after reading the config
        assertNotNull(appSettings.getStartPage());

        // Ensure that the configPath exists
        assertTrue(Files.exists(appSettings.getConfigPath()));
        // Ensure that the cachePath exists
        assertTrue(Files.exists(appSettings.getCachePath()));
    }

    @Test
    void testSaveConfig() throws IOException {
        // Define test paths
        Path tempConfigPath = Files.createTempFile("test-config", ".json");
        Path tempCachePath = Files.createTempDirectory("test-cache");

        // Set the test paths
        appSettings.setConfigPath(tempConfigPath);
        appSettings.setCachePath(tempCachePath);

        // Set the start page
        appSettings.setStartPage("test-start-page");

        // Save the config
        appSettings.saveConfig();

        // Read the saved config
        String json = Files.readString(tempConfigPath);
        AppSettingsConfig settingsConfig = new ObjectMapper().readValue(json, AppSettingsConfig.class);

        // Ensure that the saved config matches the set values
        assertEquals(tempCachePath.toString(), settingsConfig.getCachePath());
        assertEquals("test-start-page", settingsConfig.getStartPage());

        // Clean up the temporary files
        Files.deleteIfExists(tempConfigPath);
        Files.deleteIfExists(tempCachePath);
    }
}




