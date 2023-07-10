package ru.dingo3.streamingmusicbmbf.providers;

import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import javax.swing.*;
import java.util.List;

public abstract class AbstractProvider {
    private String providerName;

    public AbstractProvider(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }

    public abstract String getConfig();

    public abstract void setConfig(String config);

    public abstract List<BasePlaylist> getPlaylist();

    public abstract List<BaseTrack> getTracks(String playlistId);

    public abstract JPanel getSettingsPanel();
}
