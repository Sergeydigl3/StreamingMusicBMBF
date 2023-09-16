package ru.dingo3.streamingmusicbmbf.views.providers;

import ru.dingo3.streamingmusicbmbf.providers.YandexProvider;

import javax.swing.*;
import java.awt.*;

public class YandexProviderView implements AbstractProviderView {
    YandexProvider provider;

    JTextField tokenField;

    public YandexProviderView(YandexProvider provider) {
        this.provider = provider;
        tokenField = new JTextField();
        tokenField.setText(provider.getToken());
    }

    @Override
    public YandexProvider getProvider() {
        return provider;
    }

    @Override
    public String getLeftTitleText() {
        return "You are logged in as " + provider.getUsername();
    }



    @Override
    public JPanel getSettingsPanel() {
        JPanel providerSettingsPanel = new JPanel();
        providerSettingsPanel.setBorder(BorderFactory.createTitledBorder("Настройки Яндекс.Музыка"));
        providerSettingsPanel.setLayout(new BoxLayout(providerSettingsPanel, BoxLayout.Y_AXIS));

        JPanel inputTokenPanel = new JPanel();
        inputTokenPanel.setLayout(new BorderLayout());

        JLabel tokenLabel = new JLabel("Токен");

//        tokenField.setMaximumSize(tokenField.getPreferredSize());
        inputTokenPanel.add(tokenLabel, BorderLayout.WEST);
        inputTokenPanel.add(tokenField, BorderLayout.CENTER);
//        inputTokenPanel.add(tokenField, BorderLayout.CENTER);
        providerSettingsPanel.add(inputTokenPanel);

//        providerSettingsPanel.add(tokenLabel);
//        providerSettingsPanel.add(tokenField);
        return providerSettingsPanel;
    }

    @Override
    public boolean isAdditionalPlaylistsSupported() {
        return false;
    }

//    private void loadSettings(){
//        tokenField.setText(provider.getToken());
//    }

    public void saveSettings() {
        provider.setToken(tokenField.getText());
        provider.saveConfig();
    }
}

