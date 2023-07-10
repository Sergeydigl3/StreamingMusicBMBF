package ru.dingo3.streamingmusicbmbf.providers;

import ru.dingo3.streamingmusicbmbf.core.YandexMusicClient;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class YandexProvider extends AbstractProvider{
    private String providerName = "Yandex.Music";
    private YandexMusicClient yandexMusicClient;

    private String token;

    public YandexProvider(String providerName) {
        super(providerName);
        yandexMusicClient = new YandexMusicClient(token);
    }

    public List<BasePlaylist> getPlaylist() {
        List<BasePlaylist> playlists = new ArrayList<>();
        return playlists;
    }
    public List<BaseTrack> getTracks(String playlistId) {
        List<BaseTrack> tracks = new ArrayList<>();
        return tracks;
    }

    public String getConfig() {
        return token;
    }

    public void setConfig(String config) {
        this.token = config;
    }

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
}

