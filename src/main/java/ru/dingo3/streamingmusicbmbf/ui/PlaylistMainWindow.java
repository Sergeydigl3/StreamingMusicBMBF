package ru.dingo3.streamingmusicbmbf.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PlaylistMainWindow extends JFrame {
    private int playlistId;
    private JLabel playlistNameLabel;
    private JLabel numSongsLabel;
    private JTable songsTable;

    public PlaylistMainWindow(int playlistId) {
        this.playlistId = playlistId;
        setTitle("Playlist Window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));
        // setMargin(new Insets(20, 20, 20, 20));

        // Top half of the window
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(0, 150));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        ImageIcon playlistImage = new ImageIcon("playlist_image.png");
        JLabel imageLabel = new JLabel(playlistImage);
        topPanel.add(imageLabel, BorderLayout.WEST);
        JPanel labelsPanel = new JPanel(new GridLayout(3, 1));
        playlistNameLabel = new JLabel();
        labelsPanel.add(new JLabel("Playlist"));
        labelsPanel.add(playlistNameLabel);
        numSongsLabel = new JLabel();
        labelsPanel.add(numSongsLabel);
        topPanel.add(labelsPanel, BorderLayout.CENTER);
        JButton syncButton = new JButton("Sync enabled");
        topPanel.add(syncButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Separator line
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(0, 1));
        add(separator, BorderLayout.CENTER);

        // Bottom half of the window
        String[] columnNames = {"", "TRACK", "ARTIST", ""};
        Object[][] data = {
                {new ImageIcon("song1_cover.png"), "Song 1", "Artist 1", "Downloading", new ImageIcon("processing_icon.png")},
                {new ImageIcon("song2_cover.png"), "Song 2", "Artist 2", "Processing", new ImageIcon("processing_icon.png")},
                {new ImageIcon("song3_cover.png"), "Song 3", "Artist 3", "Downloaded", new ImageIcon("processed_icon.png")},
                {new ImageIcon("song4_cover.png"), "Song 4", "Artist 4", "Downloaded", new ImageIcon("processed_icon.png")}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 3) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        songsTable = new JTable(model);
        songsTable.setShowGrid(false);
        songsTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(songsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setPlaylistName(String name) {
        playlistNameLabel.setText(name);
    }

    public void setNumSongs(int numSongs) {
        numSongsLabel.setText("* " + numSongs + " tracks");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlaylistMainWindow playlistWindow = new PlaylistMainWindow(4);
            playlistWindow.setPlaylistName("My Playlist");
            playlistWindow.setNumSongs(10);
        });
    }
}

class PlaylistDetail extends JFrame {
    public PlaylistDetail() {
        setTitle("Playlist Detail");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create multiple playlists
        for (int i = 1; i <= 3; i++) {
            PlaylistPanel playlistPanel = new PlaylistPanel(i);
            add(playlistPanel);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class PlaylistPanel extends JPanel {
    private int playlistId;

    public PlaylistPanel(int playlistId) {
        this.playlistId = playlistId;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Playlist Image
        ImageIcon playlistImage = new ImageIcon("playlist_image.png");
        JLabel imageLabel = new JLabel(playlistImage);
        add(imageLabel, BorderLayout.CENTER);
        JLabel titleLabel = new JLabel("Playlist " + playlistId);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.SOUTH);

        // Click listener
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PlaylistMainWindow playlistWindow = new PlaylistMainWindow(playlistId);
                playlistWindow.setPlaylistName("Playlist " + playlistId);
                playlistWindow.setNumSongs(4);
            }
        });
    }
}