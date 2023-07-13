package ru.dingo3.streamingmusicbmbf.providers;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import ru.dingo3.streamingmusicbmbf.core.YandexMusicClient;
import ru.dingo3.streamingmusicbmbf.models.PlaylistResponse;
import ru.dingo3.streamingmusicbmbf.models.PlaylistsResponse;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ArrayList;

public class YandexProvider implements AbstractProvider {
    private static final String providerName = "Yandex.Music";
    private static final String providerId = "yandex";
    private final Path cachePath = Paths.get(System.getProperty("user.home"), ".streamingmusicbmbf", "yandex");
    private final YandexMusicClient yandexMusicClient;

    private boolean sync = false;

    @Override
    public boolean getSync() {
        return sync;
    }

    private String token; // TODO: Implement token in settings

    public YandexProvider(Path cachePath) {
        token = System.getenv("YM_TOKEN"); // TODO: Remove env set
        cachePath = Paths.get(cachePath.toString(), providerId);
        System.out.println("YandexProvider: " + cachePath);
        yandexMusicClient = new YandexMusicClient(token);
        yandexMusicClient.init();
    }

    private void loadConfig() {
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
    private void saveConfig() {
        JSONObject configJson = new JSONObject();
        configJson.put("token", token);
        Path configPath = Paths.get(cachePath.toString(), "config.json");
        try {
            Files.writeString(configPath, configJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getProviderName() {
        return providerName;
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
            playlist.setImage("https://"+ymPlaylistItem.getOgImage().replace("%%", "400x400"));
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
                BaseTrack track = new BaseTrack();
                track.setId(ymTrack.getId());
                track.setTitle(ymTrack.getTrack().getTitle());
                track.setArtist(ymTrack.getTrack().getArtists().get(0).getName());


//                track.setImage("https://"+ymTrack.getOgImage().replace("%%", "400x400"));
                tracks.add(track);
            }
        }
//        System.out.println("YandexProvider: getTracks: " + tracks.size());
        return tracks;
    }

    @Override
    public JPanel getSettingsPanel() {
        JPanel providerSettingsPanel = new JPanel();
        providerSettingsPanel.setBorder(BorderFactory.createTitledBorder("Настройки Яндекс.Музыка"));
        providerSettingsPanel.setLayout(new BoxLayout(providerSettingsPanel, BoxLayout.Y_AXIS));

        JPanel inputTokenPanel = new JPanel();
        inputTokenPanel.setLayout(new BorderLayout());

        JLabel tokenLabel = new JLabel("Токен");
        JTextField tokenField = new JTextField();
//        tokenField.setMaximumSize(tokenField.getPreferredSize());
        inputTokenPanel.add(tokenLabel, BorderLayout.WEST);
        inputTokenPanel.add(tokenField, BorderLayout.CENTER);
//        inputTokenPanel.add(tokenField, BorderLayout.CENTER);
        providerSettingsPanel.add(inputTokenPanel);

//        providerSettingsPanel.add(tokenLabel);
//        providerSettingsPanel.add(tokenField);
        return providerSettingsPanel;
    }

    public void sync() {

    }

    public Path getCachePath() {
        return cachePath;
    }
}

