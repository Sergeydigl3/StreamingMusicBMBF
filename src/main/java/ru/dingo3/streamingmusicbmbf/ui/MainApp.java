package ru.dingo3.streamingmusicbmbf.ui;

import ru.dingo3.streamingmusicbmbf.helpers.CachedImageIconDb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainApp extends JFrame {
    private final CachedImageIconDb cachedImageIconDb = new CachedImageIconDb("C:\\Projects\\suai\\StreamingMusicBMBF\\cache/");
    public MainApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Playlist App");
        setLayout(new FlowLayout());

        // Добавляем несколько плейлистов
        addPlaylist("https://avatars.yandex.net/get-music-content/97284/6298b07e.a.5976501-1/200x200", "Плейлист 1", 1);
        addPlaylist("https://avatars.yandex.net/get-music-user-playlist/34120/273593788.1023.70670/200x200?1652899062431", "Плейлист 2", 2);
//        addPlaylist("https://playlist3.jpg", "Плейлист 3", 3);

        pack();
        setLocationRelativeTo(null);
    }

    private void addPlaylist(String imagePath, String label, int playlistId) {
        JPanel playlistPanel = new JPanel();
        playlistPanel.setLayout(new BorderLayout());

        ImageIcon originalIcon = cachedImageIconDb.getByUrl(imagePath);
        Image originalImage = originalIcon.getImage();

        // Изменяем размер изображения
        int width = 100;
        int height = 100;
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Создаем новый ImageIcon с измененным изображением
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(resizedIcon);
        playlistPanel.add(imageLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(label);
        playlistPanel.add(nameLabel, BorderLayout.SOUTH);

        // Добавляем обработчик события нажатия на плейлист
        playlistPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openPlaylistWindow(playlistId);
            }
        });

        add(playlistPanel);
    }

    private void openPlaylistWindow(int playlistId) {
        setEnabled(false); // Отключаем основное окно

        // Создаем новое диалоговое окно для плейлиста
        JDialog playlistDialog = new JDialog(this, "Окно плейлиста", true);
        playlistDialog.setLayout(new BorderLayout());
        playlistDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        playlistDialog.setSize(400, 300);
        playlistDialog.setLocationRelativeTo(this);

        // Добавляем компоненты в диалоговое окно плейлиста
        // ...

        // Восстанавливаем доступ к основному окну при закрытии диалогового окна
        playlistDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setEnabled(true); // Включаем основное окно
                toFront(); // Переводим основное окно на передний план
            }
        });

        playlistDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainApp().setVisible(true);
            }
        });
    }
}
