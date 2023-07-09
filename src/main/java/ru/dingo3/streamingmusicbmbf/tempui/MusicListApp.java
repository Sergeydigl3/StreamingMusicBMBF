package ru.dingo3.streamingmusicbmbf.tempui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MusicListApp extends JFrame {
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JScrollPane scrollPane;

    public MusicListApp() {
        setTitle("Music List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.YELLOW);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);




        headerPanel = new JPanel(new GridLayout(1, 3));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        JLabel titleLabel = new JLabel("Title");
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        JLabel artistLabel = new JLabel("Artist");
        artistLabel.setHorizontalAlignment(JLabel.LEFT);
        JLabel statusLabel = new JLabel("Status");
        statusLabel.setHorizontalAlignment(JLabel.LEFT);

        headerPanel.add(titleLabel);
        headerPanel.add(artistLabel);
        headerPanel.add(statusLabel);

        scrollPane = new JScrollPane(mainPanel);

        scrollPane.setColumnHeaderView(headerPanel);

        scrollPane.getViewport().setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.getViewport().setAlignmentY(Component.TOP_ALIGNMENT);



        add(scrollPane);

        // Add sample music entries
//        addRow("Song 1", "Artist 1", "Playing", false);
//        addRow("Song 2", "Artist 2", "Paused", false);
//        addRow("Song 3", "Artist 3", "Stopped", false);
//        addRow("Song 4", "Artist 4", "Playing", false);
//        addRow("Song 5", "Artist 5", "Paused", false);

        for (int i = 0; i < 100; i++) {
            addRow("Song " + i, "Artist " + i, "Playing", false);
        }


        setVisible(true);
    }

    private void addRow(String title, String artist, String status, boolean isHeader) {
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        constraints.weightx = 1.0;
//        constraints.anchor = GridBagConstraints.NORTHWEST; // Align elements to the top-left
//        constraints.insets = new Insets(10, 5, 10, 5); // Add padding around the elements

        JPanel rowPanel = new JPanel(new GridLayout(1, 3));
        rowPanel.setBackground(Color.lightGray);
        rowPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        rowPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 18));

        JLabel titleLabel = new JLabel(title);
        JLabel artistLabel = new JLabel(artist);
        JLabel statusLabel = new JLabel(status);

        if (isHeader) {
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
            artistLabel.setFont(artistLabel.getFont().deriveFont(Font.BOLD));
            statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
        }

        rowPanel.add(titleLabel);
        rowPanel.add(artistLabel);
        rowPanel.add(statusLabel);

//        constraints.gridy = musicList.size() + 1;
//        mainPanel.add(rowPanel, constraints);
        mainPanel.add(rowPanel);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicListApp::new);
    }
}
