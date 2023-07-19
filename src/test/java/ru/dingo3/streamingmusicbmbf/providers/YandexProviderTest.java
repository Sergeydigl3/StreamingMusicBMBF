package ru.dingo3.streamingmusicbmbf.providers;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class YandexProviderTest {

    private YandexProvider yandexProvider;
    private Path cachePath;

    @BeforeEach
    void setUp() {
        cachePath = Path.of("cache");
        yandexProvider = new YandexProvider(cachePath);
    }

    @Test
    void testGetSync() {
        assertFalse(yandexProvider.getSync());
    }

    @Test
    void testGetProviderName() {
        assertEquals("Yandex.Music", yandexProvider.getProviderName());
    }

    @Test
    void testGetProviderId() {
        assertEquals("yandex", yandexProvider.getProviderId());
    }

    @Test
    void testGetPlaylists() {
        ArrayList<BasePlaylist> playlists = yandexProvider.getPlaylists();
        assertNotNull(playlists);

    }

    @Test
    void testGetTracks() {
        ArrayList<BaseTrack> tracks = yandexProvider.getTracks("1034");
        assertNotNull(tracks);

    }

    @Test
    void testDownloadTrack() {
        BasePlaylist playlist = new BasePlaylist();
        BaseTrack track = new BaseTrack();
        track.setId("97408927");
        yandexProvider.downloadTrack(playlist, track);

        yandexProvider.downloadTrack(playlist, track);
        assertNotNull(track.getLocalPath());
    }

    @Test
    void testGetNowSyncing() {
        assertFalse(yandexProvider.getNowSyncing());
    }

    @Test
    void testSetNowSyncing() {
        yandexProvider.setNowSyncing(true);
        assertTrue(yandexProvider.getNowSyncing());
    }
}
