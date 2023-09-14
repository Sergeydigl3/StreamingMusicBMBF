package ru.dingo3.streamingmusicbmbf.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import ru.dingo3.streamingmusicbmbf.converters.AbstractConverter;
import ru.dingo3.streamingmusicbmbf.delivery.BMBFDelivery;
import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;
import ru.dingo3.streamingmusicbmbf.providers.models.SyncState;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ProviderManager {
    @Getter
    private boolean syncState = false;

    private static final int nThreads = 5;
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
    @Setter
    private AbstractConverter converter;

    @Getter
    private ConcurrentHashMap<String, CopyOnWriteArrayList<ConcurrentHashMap<BasePlaylist, CopyOnWriteArrayList<String>>>> db = new ConcurrentHashMap<>();

    @Getter
    private ConcurrentHashMap<String, CopyOnWriteArrayList<BaseTrack>> tracksDb = new ConcurrentHashMap<>();

    ExecutorService executor = Executors.newFixedThreadPool(nThreads);

    public ProviderManager() {
        providers = new ArrayList<>();
    }


    public void saveDbToDisk() {
        try {
            // if directory not exists, create it
//            File directory = new File(filePath).getParentFile();
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }

            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(db);
            objectOut.writeObject(tracksDb);
            objectOut.close();
            fileOut.close();
            // save to json
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(db);
//            String json2 = objectMapper.writeValueAsString(tracksDb);
//            FileWriter fileWriter = new FileWriter(filePath);
//            fileWriter.write(json);
//            fileWriter.write(json2);
//            fileWriter.close();


            System.out.println("Database saved successfully to disk.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    public void loadDbFromDisk() {
        try {
            if (!new File(filePath).exists()) {
                System.out.println("Database file not found.");
                return;
            }
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            db = (ConcurrentHashMap<String, CopyOnWriteArrayList<ConcurrentHashMap<BasePlaylist, CopyOnWriteArrayList<String>>>>) objectIn.readObject();
            tracksDb = (ConcurrentHashMap<String, CopyOnWriteArrayList<BaseTrack>>) objectIn.readObject();
            objectIn.close();
            fileIn.close();

            // load data from json
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = new String(Files.readAllBytes(Paths.get(filePath)));
//            db = objectMapper.readValue(json, ConcurrentHashMap.class);
//            tracksDb = objectMapper.readValue(json, ConcurrentHashMap.class);


            System.out.println("Database loaded successfully from disk.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
    public ArrayList<BaseTrack> getTracksByIds(String providerId, ArrayList<String> trackIds) {
        ArrayList<BaseTrack> tracks = new ArrayList<>();
        trackIds.forEach(trackId -> {
            if (tracksDb.get(providerId).stream().anyMatch(temp -> temp.getId().equals(trackId))) {
                tracks.add(tracksDb.get(providerId).stream().filter(temp -> temp.getId().equals(trackId)).findFirst().get());
            }
        });
        return tracks;
    }

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
        boolean delivery = AppSettings.getInstance().isDeliveryBMBF();
        String deliveryPath = AppSettings.getInstance().getBmbfApiUrl();
        BMBFDelivery bmbfDelivery = new BMBFDelivery(deliveryPath);
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
                        boolean error = false;
                        while (track.getSyncState() != (delivery ? SyncState.SYNCED : SyncState.CONVERTED) && !error) {
                            System.out.println("Syncing track: " + track.getTitle() + " " + track.getSyncState());
                            switch (track.getSyncState()) {
                                case NOT_DOWNLOADED:
                                    track.setSyncState(SyncState.DOWNLOADING);
                                    provider.downloadTrack(playlist, track);
                                    track.setSyncState(SyncState.DOWNLOADED);
                                    break;
                                case DOWNLOADED:
                                    track.setSyncState(SyncState.CONVERSION);
                                    converter.convertTrack(provider.getProviderId(), track);
                                    track.setSyncState(SyncState.CONVERTED);
                                    break;
                                case CONVERTED:
                                    track.setSyncState(SyncState.SYNCING);
                                    for (int i = 0; i < 3; i++) {
                                        try {
                                            int res = bmbfDelivery.deliver(playlist, track);
                                            System.out.println("BMBF delivery result: " + res);
                                            if ( res== 204)
                                                track.setSyncState(SyncState.SYNCED);
                                            else{
                                                track.setSyncState(SyncState.CONVERTED);
                                                error = true;
                                            }
                                            break;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            track.setSyncState(SyncState.CONVERTED);
                                            error = true;
                                        }
                                    }
                                    break;
                            }
                        }
                        System.out.println("Syncing track: " + track.getTitle() + " " + track.getSyncState());
                        track.setNowSyncing(false);
                    });

                });
            });
        });
    }

    public void setPlaylistSyncState(String provider, String basePlaylistId, boolean state) {
        BasePlaylist basePlaylist = new BasePlaylist();
        basePlaylist.setId(basePlaylistId);
        setPlaylistSyncState(provider, basePlaylist, state);
    }

    public void setPlaylistSyncState(String provider, BasePlaylist basePlaylist, boolean state) {
        AtomicBoolean synced = new AtomicBoolean(false);
        db.get(provider).forEach(temp -> {
            temp.keySet().forEach(playlist -> {
                if (playlist.getId().equals(basePlaylist.getId())) {
                    playlist.setSync(state);
                    System.out.println("Playlist " + playlist.getTitle() + " sync state: " + state);
                    synced.set(true);
                }
            });
        });
        if (!synced.get()) {
            System.out.println("Playlist adding " + basePlaylist.getTitle() + " not found. Adding...");
            db.get(provider).add(new ConcurrentHashMap<>());
            db.get(provider).get(db.get(provider).size() - 1).put(basePlaylist, new CopyOnWriteArrayList<>());
            basePlaylist.setSync(state);
        }
    }

//    public void setPlaylistSyncState(String provider, BasePlaylist playlist, boolean state) {
//        // if playlist does not exist add it
//        if (!db.get(provider).stream().anyMatch(temp -> temp.containsKey(playlist))) {
//            db.get(provider).add(new ConcurrentHashMap<>());
//            db.get(provider).get(db.get(provider).size() - 1).put(playlist, new CopyOnWriteArrayList<>());
//        }
//        playlist.setSync(state);
//    }

    public boolean getPlaylistSyncState(String provider, String playlistId) {
        boolean tempBoolean = false;
        for (ConcurrentHashMap<BasePlaylist, CopyOnWriteArrayList<String>> temp : db.get(provider)) {
            for (BasePlaylist playlist : temp.keySet()) {
                if (playlist.getId().equals(playlistId)) {
                    tempBoolean = playlist.getSync();
                }
            }
        }
        System.out.println("Playlist " + playlistId + " sync state: " + tempBoolean);
        return tempBoolean;
//        return s;
    }

    public void performSync(AbstractProvider provider) {
//        if (!provider.getSync()) return;
//        if (provider.getNowSyncing()) return;
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
