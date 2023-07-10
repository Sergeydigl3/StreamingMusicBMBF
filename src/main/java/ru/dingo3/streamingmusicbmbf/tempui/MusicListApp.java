package ru.dingo3.streamingmusicbmbf.tempui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MusicListApp extends JFrame {
    private JPanel headerPanel;
    private JPanel mainPanel;

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


        JLabel titleLabel = new JLabel("Title");
        JLabel artistLabel = new JLabel("Artist");
        JLabel statusLabel = new JLabel("Status");


        headerPanel.add(titleLabel);
        headerPanel.add(artistLabel);
        headerPanel.add(statusLabel);

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setColumnHeaderView(headerPanel);





        add(scrollPane);


        for (int i = 1; i < 10; i++) {
            addRow("Song " + i, "Artist " + i, "Playing", false);
        }


        setVisible(true);
    }

    private void addRow(String title, String artist, String status, boolean isHeader) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 3));
        rowPanel.setBackground(Color.lightGray);
//        rowPanel.setAlignmentY(Component.TOP_ALIGNMENT);
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

        mainPanel.add(rowPanel);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicListApp::new);
    }
}

class MusicList extends JScrollPane {
    private JPanel headerPanel;
    private JPanel mainPanel;

    public MusicList() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.YELLOW);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        setColumnHeaderView(new JPanel(new GridLayout(1, 3)));

        headerPanel = new JPanel(new GridLayout(1, 3));


        JLabel titleLabel = new JLabel("Title");
        JLabel artistLabel = new JLabel("Artist");
        JLabel statusLabel = new JLabel("Status");


        headerPanel.add(titleLabel);
        headerPanel.add(artistLabel);
        headerPanel.add(statusLabel);

        setColumnHeaderView(headerPanel);

        setViewportView(mainPanel);
    }

    private void addRow(String title, String artist, String status, boolean isHeader) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 3));
        rowPanel.setBackground(Color.lightGray);
//        rowPanel.setAlignmentY(Component.TOP_ALIGNMENT);
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

        mainPanel.add(rowPanel);

    }

}
//        rowPanel.setAlignmentY(Component.TOP_ALIGNMENT);