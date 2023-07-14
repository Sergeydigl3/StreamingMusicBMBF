package ru.dingo3.streamingmusicbmbf.providers;

import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import javax.swing.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface AbstractProvider {

//    String getProviderName();

    String getProviderName();

    String getProviderId();

//    public AbstractProvider(String providerName, Path cachePath) {
//    }
    boolean getSync();

    void sync();

//    public abstract String getConfig();

//    public abstract void setConfig(String config);

    ArrayList<BasePlaylist> getPlaylists();

    ArrayList<BaseTrack> getTracks(String playlistId);

    JPanel getSettingsPanel();

    Path getCachePath();
}
