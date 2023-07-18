package ru.dingo3.streamingmusicbmbf.views.providers;

import javax.swing.*;

public interface AbstractProviderView {
    public JPanel getSettingsPanel();

    public boolean isAdditionalPlaylistsSupported();

}
