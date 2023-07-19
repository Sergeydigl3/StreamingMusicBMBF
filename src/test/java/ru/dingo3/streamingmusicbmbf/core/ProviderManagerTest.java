package ru.dingo3.streamingmusicbmbf.core;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.junit.jupiter.api.Test;
import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;
import ru.dingo3.streamingmusicbmbf.providers.YandexProvider;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;
import ru.dingo3.streamingmusicbmbf.providers.models.SyncState;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProviderManagerTest {
    @Test
    public void saveDbAndLoadShouldBeSameCount() {
        ProviderManager providerManager = new ProviderManager();
        ProviderManager providerManager2 = new ProviderManager();
        AbstractProvider ym = new YandexProvider(Path.of("cache"));
        providerManager.addProvider(ym);
        providerManager.startSync();
        providerManager.saveDbToDisk();
        providerManager2.loadDbFromDisk();
        assertEquals(providerManager.getDb(), providerManager2.getDb());
//        assertFalse(providerManager.getSyncState());
    }

    @Test
    public void test2PlaylistAndTest3PlayShouldStore1Track() {
        ProviderManager providerManager = new ProviderManager();
        AbstractProvider ym = new YandexProvider(Path.of("cache"));
        providerManager.addProvider(ym);
//        providerManager.startSync();
        providerManager.getRecentDataPlaylist(ym);
        providerManager.setPlaylistSyncState(ym.getProviderId(), "1035", true);
        providerManager.setPlaylistSyncState(ym.getProviderId(), "1036", true);
        providerManager.getRecentDataPlaylist(ym);
        providerManager.getRecentDataTrack(ym);
        assertEquals(1, providerManager.getTracksDb().get(ym.getProviderId()).size());
    }

    @Test
    public void syncPlaylistShouldDownloadAllTracks() {
        ProviderManager providerManager = new ProviderManager();
        // relative to global
        String cachePath = "cache";
        cachePath = Path.of(cachePath).toAbsolutePath().toString();
        AbstractProvider ym = new YandexProvider(Path.of(cachePath));
        providerManager.addProvider(ym);

        providerManager.getRecentDataPlaylist(ym);
        providerManager.setPlaylistSyncState(ym.getProviderId(), "1034", true);

        providerManager.startSync();
        // sleep
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int count = 0;
        // check all Tracks downloaded
        for (BaseTrack track : providerManager.getTracksDb().get(ym.getProviderId())) {
            if (track.getSyncState() != SyncState.NOT_DOWNLOADED)
                count++;
        }
        assertEquals(count, providerManager.getTracksDb().get(ym.getProviderId()).size());

    }

    @Test
    public void getMusicBy3IdsShouldReturn3Tracks() {
        ProviderManager providerManager = new ProviderManager();
//        String cachePath = "cache";
//        cachePath = Path.of(cachePath).toAbsolutePath().toString();
        AbstractProvider ym = new YandexProvider(Path.of("cache"));
        providerManager.addProvider(ym);

        providerManager.getRecentDataPlaylist(ym);
        BasePlaylist playlist = new BasePlaylist();
        playlist.setId("1021");
        providerManager.setPlaylistSyncState(ym.getProviderId(), playlist, true);
        providerManager.getRecentDataTrack(ym);
        // ArrayList = [159662006, 69974812, 73333185]
        ArrayList<String> ids = new ArrayList<>();
        ids.add("59662006");
        ids.add("69974812");
        ids.add("73333185");

        ArrayList<BaseTrack> tracksByIds = providerManager.getTracksByIds(ym.getProviderId(), ids);
        assertEquals(3, tracksByIds.size());
    }

//        providerManager.startSync();
}