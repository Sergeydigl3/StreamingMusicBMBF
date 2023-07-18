package ru.dingo3.streamingmusicbmbf.views.providers;

import javax.swing.*;
import java.awt.*;

public class YandexProviderView implements AbstractProviderView {
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

    @Override
    public boolean isAdditionalPlaylistsSupported() {
        return false;
    }

}
