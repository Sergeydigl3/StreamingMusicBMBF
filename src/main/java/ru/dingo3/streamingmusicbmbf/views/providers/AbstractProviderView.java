package ru.dingo3.streamingmusicbmbf.views.providers;

import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;

import javax.swing.*;

public interface AbstractProviderView {
    public JPanel getSettingsPanel();

    public AbstractProvider getProvider();

    public boolean isAdditionalPlaylistsSupported();
}
