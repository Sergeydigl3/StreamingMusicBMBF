package ru.dingo3.streamingmusicbmbf.providers;

import com.mpatric.mp3agic.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import ru.dingo3.streamingmusicbmbf.libs.YandexMusicClient;
import ru.dingo3.streamingmusicbmbf.models.PlaylistResponse;
import ru.dingo3.streamingmusicbmbf.models.PlaylistsResponse;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class YandexProvider implements AbstractProvider, Serializable {

    private static final String providerName = "Yandex.Music";

    private static final String providerId = "yandex";
    private Path cachePath;
    private final YandexMusicClient yandexMusicClient;

    private boolean sync = false;

    private boolean nowSyncing = false;

    @Override
    public boolean getSync() {
        return sync;
    }

    @Getter
    @Setter
    private String token; // TODO: Implement token in settings

    public YandexProvider(Path cachePath) {
//        token = System.getenv("YM_TOKEN"); // TODO: Remove env set
        this.cachePath = Paths.get(cachePath.toString(), providerId);
        loadConfig();
//        System.out.println("YandexProvider: " + cachePath);
        yandexMusicClient = new YandexMusicClient(token);
        yandexMusicClient.init();
    }

    public void loadConfig() {
        Path configPath = Paths.get(cachePath.toString(), "config.json");
        if (Files.exists(configPath)) {
            try {
                String config = Files.readString(configPath);
                JSONObject configJson = new JSONObject(config);
                token = configJson.getString("token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveConfig() {
        JSONObject configJson = new JSONObject();
        configJson.put("token", token);
        Path configPath = Paths.get(cachePath.toString(), "config.json");
        try {
            Files.writeString(configPath, configJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUsername() {
        return yandexMusicClient.getMe().getLogin();
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public ArrayList<BasePlaylist> getPlaylists() {
        ArrayList<BasePlaylist> playlists = new ArrayList<>();
        ArrayList<PlaylistsResponse.Playlist> ymPlaylist = yandexMusicClient.getPlaylists();
        if (ymPlaylist == null) {
            return playlists;
        }
        for (PlaylistsResponse.Playlist ymPlaylistItem : ymPlaylist) {
            BasePlaylist playlist = new BasePlaylist();
            playlist.setId(Integer.toString(ymPlaylistItem.getKind()));
            playlist.setTitle(ymPlaylistItem.getTitle());
//            System.out.println("YandexProvider: getPlaylists: " + ymPlaylistItem.getOgImage());
            if (
                    ymPlaylistItem.getOgImage().isEmpty()
            )
                playlist.setImage("https://raw.githubusercontent.com/Sergeydigl3/StreamingMusicBMBF/main/playlist1.jpg");
            else
                playlist.setImage("https://" + ymPlaylistItem.getOgImage().replace("%%", "400x400"));

            playlist.setMusicCount(ymPlaylistItem.getTrackCount());
            playlists.add(playlist);
        }
        return playlists;
    }

    @Override
    public ArrayList<BaseTrack> getTracks(String playlistId) {
        ArrayList<BaseTrack> tracks = new ArrayList<>();
        java.util.List<PlaylistResponse.Track> ymTracks = yandexMusicClient.getPlaylistTracks(playlistId);
        if (ymTracks != null) {
            for (PlaylistResponse.Track ymTrack : ymTracks) {
                if (ymTrack.getTrack() == null || ymTrack.getTrack().getArtists() == null || ymTrack.getTrack().getArtists().isEmpty() || ymTrack.getTrack().getTitle() == null) {
                    continue;
                }
                BaseTrack track = new BaseTrack();
                track.setId(Integer.toString(ymTrack.getId()));
                track.setTitle(ymTrack.getTrack().getTitle());
                track.setArtist(ymTrack.getTrack().getArtists().get(0).getName());

                track.setCoverUrl("https://"+ymTrack.getTrack().getOgImage().replace("%%", "400x400"));
                tracks.add(track);
            }
        }
//        System.out.println("YandexProvider: getTracks: " + tracks.size());
        return tracks;
    }

    public void sync() {

    }

    public Path getCachePath() {
        return cachePath;
    }

    @Override
    public void downloadTrack(BasePlaylist playlist, BaseTrack track) {
        System.out.println("YandexProvider: downloadTrack: " + track.getTitle());
        Path trackPath = Paths.get(cachePath.toString(), "/music/", track.getId() + ".mp3");
        yandexMusicClient.downloadTrack(track.getId(), trackPath.toString());
        try {
            Mp3File mp3file = new Mp3File(trackPath.toString());
            ID3v2 id3v2Tag = new ID3v23Tag();
//            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
//            mp3file.setId3v2Tag();

            HttpURLConnection connection = (HttpURLConnection) new URL(track.getCoverUrl()).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "image/jpeg");
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            byte[] imageData = inputStream.readAllBytes();


            id3v2Tag.setAlbumImage(imageData, "image/jpeg");
            id3v2Tag.setTitle(track.getTitle());
            id3v2Tag.setArtist(track.getArtist());
            mp3file.setId3v2Tag(id3v2Tag);
            mp3file.save(trackPath.toString()+".mp3");
            // Remove old file
            Files.deleteIfExists(trackPath);
            Files.move(Paths.get(trackPath.toString()+".mp3"), trackPath);
        } catch (Exception e) {
            System.out.println("YandexProvider: downloadTrack: ERROR " + e.getMessage());
        }

        track.setLocalPath(trackPath.toString());

    }

    @Override
    public boolean getNowSyncing() {
        return nowSyncing;
    }

    public void setNowSyncing(boolean nowSyncing) {
        this.nowSyncing = nowSyncing;
    }
}


@Data
class YandexProviderConfig {
    private String token;
    private Boolean syncState;
    private List<Boolean> syncPlaylistsState;
}