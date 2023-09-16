package ru.dingo3.streamingmusicbmbf.views.providers;

import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;

import javax.swing.*;

public interface AbstractProviderView {
    public String getLeftTitleText();
    public JPanel getSettingsPanel();

    public AbstractProvider getProvider();

    public boolean isAdditionalPlaylistsSupported();

    void saveSettings();
}
