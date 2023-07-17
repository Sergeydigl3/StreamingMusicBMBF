package ru.dingo3.streamingmusicbmbf.providers;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class YandexProviderTest {

    YandexProvider yandexProvider = new YandexProvider(Path.of("D:\\cache"));
    @Test
    public void getProviderNameShouldReturnYandex() {
        String actualProviderName = yandexProvider.getProviderName();
        String expectedProviderName = "Yandex.Music";
        assertEquals(expectedProviderName, actualProviderName);
    }

    @Test
    public void getProviderIdShouldReturnYandex() {
        String actualProviderId = yandexProvider.getProviderId();
        String expectedProviderId = "yandex";
        assertEquals(expectedProviderId, actualProviderId);
    }
}