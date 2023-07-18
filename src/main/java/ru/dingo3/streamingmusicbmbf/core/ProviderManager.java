package ru.dingo3.streamingmusicbmbf.core;

import lombok.Getter;
import lombok.Setter;
import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;
import ru.dingo3.streamingmusicbmbf.providers.models.SyncState;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ProviderManager {
    @Getter
    private boolean syncState = false;

    private static final int nThreads = 2;
    @Setter
    @Getter
    private Integer syncDelay = 1000;

    @Getter
    @Setter
    private String filePath = "db.dat";

    //    Thread syncThread;
    @Getter
    private ArrayList<AbstractProvider> providers;

    @Getter
    private ConcurrentHashMap<String, CopyOnWriteArrayList<ConcurrentHashMap<BasePlaylist, CopyOnWriteArrayList<String>>>> db = new ConcurrentHashMap<>();

    @Getter
    private ConcurrentHashMap<String, CopyOnWriteArrayList<BaseTrack>> tracksDb = new ConcurrentHashMap<>();

    ExecutorService executor;

    public ProviderManager() {
        providers = new ArrayList<>();
    }


    public void saveDbToDisk() {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(db);
            objectOut.writeObject(tracksDb);
            objectOut.close();
            fileOut.close();
            System.out.println("Database saved successfully to disk.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    public void loadDbFromDisk() {
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            db = (ConcurrentHashMap<String, CopyOnWriteArrayList<ConcurrentHashMap<BasePlaylist, CopyOnWriteArrayList<String>>>>) objectIn.readObject();
            tracksDb = (ConcurrentHashMap<String, CopyOnWriteArrayList<BaseTrack>>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("Database loaded successfully from disk.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addProvider(AbstractProvider provider) {
        providers.add(provider);
        db.putIfAbsent(provider.getProviderId(), new CopyOnWriteArrayList<>());
        tracksDb.putIfAbsent(provider.getProviderId(), new CopyOnWriteArrayList<>());
    }

//    public void setSyncState(boolean syncState) {
//        this.syncState = syncState;
//    }

//    public ArrayList<AbstractProvider> getProviders() {
//        return new ArrayList<>(db.keySet());
//    }

    public void getRecentDataPlaylist(AbstractProvider provider) {
//        db.putIfAbsent(provider, new CopyOnWriteArrayList<>());
        AtomicInteger playlists = new AtomicInteger();
        String providerId = provider.getProviderId();
        // Only playlists without tracks
        provider.getPlaylists().forEach(playlist -> {
            // Добавлять несуществующие плейлисты
            if (!db.get(providerId).stream().anyMatch(temp -> temp.containsKey(playlist))) {
                db.get(providerId).add(new ConcurrentHashMap<>());
                db.get(providerId).get(db.get(providerId).size() - 1).put(playlist, new CopyOnWriteArrayList<>());
                playlists.getAndIncrement();
            }
        });
        System.out.println("Playlists added: " + playlists.get());
    }

    // TracksDb - Key value pair. Key is providerId, value is list of tracks

    public void getRecentDataTrack(AbstractProvider provider) {
        // Only playlists without tracks
        AtomicInteger tracks = new AtomicInteger();
        String providerId = provider.getProviderId();
        db.get(providerId).forEach(temp -> {
            temp.keySet().forEach(playlist -> {
                if (!playlist.getSync()) return;
                System.out.println("Syncing playlist: " + playlist.getTitle());
                provider.getTracks(playlist.getId()).forEach(track -> {
                    if (!temp.get(playlist).stream().anyMatch(tempTrack -> tempTrack.equals(track.getId()))) {
                        temp.get(playlist).add(track.getId());
                    }

                    if (!tracksDb.get(providerId).stream().anyMatch(tempTrack -> tempTrack.equals(track))) {
                        tracksDb.get(providerId).add(track);
                        tracks.getAndIncrement();
                    }
                });
            });
        });

        System.out.println("Tracks added: " + tracks.get());
    }

    public BaseTrack getTrackById(String providerId, String trackId) {
        return tracksDb.get(providerId).stream().filter(track -> track.getId().equals(trackId)).findFirst().orElse(null);
    }

    public void processTracks(AbstractProvider provider) {
        String providerId = provider.getProviderId();
        db.get(providerId).forEach(temp -> {
            temp.keySet().forEach(playlist -> {
                if (!playlist.getSync()) return;
                System.out.println("Syncing playlist: " + playlist.getTitle());
                temp.get(playlist).forEach(trackId -> {
                    BaseTrack track = getTrackById(providerId, trackId);
                    if (track == null) return;
                    if (track.getNowSyncing()) return;
                    track.setNowSyncing(true);
                    executor.submit(() -> {
                        // switch case pipline
                        while (track.getSyncState() != SyncState.DOWNLOADED) {
                            System.out.println("Syncing track: " + track.getTitle() + " " + track.getSyncState());
                            switch (track.getSyncState()) {
                                case NOT_DOWNLOADED -> {
                                    track.setSyncState(SyncState.DOWNLOADING);
                                    provider.downloadTrack(playlist, track);
                                    track.setSyncState(SyncState.DOWNLOADED);
                                }
                            }
                        }
                        System.out.println("Syncing track: " + track.getTitle() + " " + track.getSyncState());
                        track.setNowSyncing(false);
                    });

                });
            });
        });
    }

    public void setPlaylistSyncState(String provider, String playlistId, boolean state) {
        db.get(provider).forEach(temp -> {
            temp.keySet().forEach(playlist -> {
                if (playlist.getId().equals(playlistId)) {
                    playlist.setSync(state);
                }
            });
        });
    }

    public void performSync(AbstractProvider provider) {
//        if (!provider.getSync()) return;
        if (provider.getNowSyncing()) return;
        provider.setNowSyncing(true);

        getRecentDataPlaylist(provider);
        getRecentDataTrack(provider);

        processTracks(provider);

    }

    public void startSync() {
        if (syncState) return;

        syncState = true;
        executor = Executors.newFixedThreadPool(nThreads);

        providers.forEach(this::performSync);

//        syncState = false;
    }

    public void stopSync(boolean force) {
        if (!syncState) return;

        if (force) {
            executor.shutdownNow();
        } else {
            executor.shutdown();
            while (true) {
                try {
                    if (executor.awaitTermination(10, TimeUnit.SECONDS))
                        break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        syncState = false;
    }
}
