package ru.dingo3.streamingmusicbmbf.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PlaylistWindow extends JFrame {
    private JPanel topPanel;
    private JButton syncButton;

    public PlaylistWindow() {
        setTitle("Playlist Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Панель для размещения компонентов
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Отступы по краям

        // Верхняя часть плейлиста с картинкой и подписями
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Картинка плейлиста
        ImageIcon playlistImage = new ImageIcon("playlist1.jpg");
        Image scaledImage = playlistImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledPlaylistImage = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledPlaylistImage);
        topPanel.add(imageLabel, BorderLayout.WEST);

        // Подписи плейлиста
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridLayout(4, 1));

        JLabel constantLabel = new JLabel("playlist");
        JLabel nameLabel = new JLabel("My playlist");
        JLabel tracksLabel = new JLabel("* 4 tracks");
        syncButton = new JButton("Sync enabled");
//        syncButton.setMaximumSize(new Dimension(10, 10));
//        syncButton.setMinimumSize(new Dimension(10, 10));
//        syncButton.setPreferredSize(new Dimension(100, 100));
//        topPanel.add(syncButton, BorderLayout.EAST);
        labelsPanel.add(constantLabel);
        labelsPanel.add(nameLabel);
        labelsPanel.add(tracksLabel);
        labelsPanel.add(syncButton, BorderLayout.WEST);

        topPanel.add(labelsPanel, BorderLayout.CENTER);

        // Кнопка синхронизации


        // Горизонтальная линия разделителя
        JPanel separatorPanel = new JPanel(new BorderLayout());
        JSeparator separator = new JSeparator();
        separatorPanel.add(separator, BorderLayout.CENTER);
        separatorPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Отступы сверху и снизу

        // Добавляем компоненты на панель
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(separatorPanel, BorderLayout.CENTER);

        // Добавляем панель на окно
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlaylistWindow window = new PlaylistWindow();
            window.setVisible(true);
        });
    }
}