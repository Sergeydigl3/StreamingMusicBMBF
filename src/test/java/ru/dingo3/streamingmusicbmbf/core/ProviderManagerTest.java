package ru.dingo3.streamingmusicbmbf.core;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.junit.jupiter.api.Test;
import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;
import ru.dingo3.streamingmusicbmbf.providers.YandexProvider;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import java.nio.file.Path;
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
        providerManager.setPlaylistSyncState(ym.getProviderId(), "1035",true);
        providerManager.setPlaylistSyncState(ym.getProviderId(), "1036",true);
        providerManager.getRecentDataPlaylist(ym);
        providerManager.getRecentDataTrack(ym);
        assertEquals(1, providerManager.getTracksDb().get(ym.getProviderId()).size());
    }

    @Test
    public void syncPlaylistShouldDownloadAllTracks() {
        ProviderManager providerManager = new ProviderManager();
        AbstractProvider ym = new YandexProvider(Path.of("D:\\cache"));
        providerManager.addProvider(ym);

        providerManager.getRecentDataPlaylist(ym);
        providerManager.setPlaylistSyncState(ym.getProviderId(), "1034",true);

        providerManager.startSync();
        // sleep
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(providerManager.getTracksDb().get(ym.getProviderId()).get(0).getLocalPath());
    }


//        providerManager.startSync();
}