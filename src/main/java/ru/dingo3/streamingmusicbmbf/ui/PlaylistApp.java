package ru.dingo3.streamingmusicbmbf.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

class RoundedImageExample extends JPanel {
    private BufferedImage image;

    public RoundedImageExample() {
        try {
            image = ImageIO.read(new File("playlist1.jpg"));
            image = image.getSubimage(0, 0, 150, 150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int x = (getWidth() - image.getWidth()) / 2;
        int y = (getHeight() - image.getHeight()) / 2;

        RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, image.getWidth(), image.getHeight(), 50, 50);
        g2d.clip(rect);
        g2d.drawImage(image, x, y, this);

        g2d.dispose();
    }
}

public class PlaylistApp extends JFrame {
    private PlaylistHeader header;
    private PlaylistContent content;
    private PlaylistContent content2;

    public PlaylistApp() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 400));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.PAGE_AXIS));

        header = new PlaylistHeader();
        panel.add(header);

        JSeparator separator = new JSeparator();
        panel.add(separator);

        content = new PlaylistContent("TRACK", "ARTIST", "STATUS");
        panel.add(content);

        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setAlignmentX(0.0F);
        jScrollPane1.setAlignmentY(0.0F);
        jScrollPane1.setColumnHeader(null);
        jScrollPane1.setColumnHeaderView(null);
        jScrollPane1.setRowHeaderView(null);

        JPanel jPanel2 = new JPanel();
        jPanel2.setAlignmentY(0.0F);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));

        content2 = new PlaylistContent("TRACK", "ARTIST", "STATUS");
        jPanel2.add(content2);

        jScrollPane1.setViewportView(jPanel2);
        panel.add(jScrollPane1);

        add(panel);
        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlaylistApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new PlaylistApp().setVisible(true);
        });
    }
}

class PlaylistHeader extends JPanel {
    private JPanel playlistInfo;
    private JButton syncButton;
    private JButton downloadButton;

    public PlaylistHeader() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));


        // -------- playlist info --------
        playlistInfo = new JPanel();
        playlistInfo.setAlignmentX(0.0F);
        playlistInfo.setLayout(new javax.swing.BoxLayout(playlistInfo, javax.swing.BoxLayout.LINE_AXIS));
        playlistInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        ImageIcon playlistImage = new ImageIcon("playlist1.jpg");
        Image scaledImage = playlistImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledPlaylistImage = new ImageIcon(scaledImage);
        JLabel playlistImageLabel = new JLabel(scaledPlaylistImage);
//        RoundedImageExample playlistImageLabel = new RoundedImageExample();
        playlistImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
//        JButton jButton3 = new JButton();
//        jButton3.setText("imagePlace");
        playlistInfo.add(playlistImageLabel);

        JPanel textPlaylistInfo = new JPanel();
        textPlaylistInfo.setLayout(new javax.swing.BoxLayout(textPlaylistInfo, javax.swing.BoxLayout.PAGE_AXIS));
//        jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel jLabel1 = new JLabel();
        jLabel1.setText("PLAYLIST");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 15));
        textPlaylistInfo.add(jLabel1);

        JLabel jLabel2 = new JLabel();
        jLabel2.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 40));
        jLabel2.setText("My cool playlist");
        jLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        textPlaylistInfo.add(jLabel2);

        JLabel jLabel3 = new JLabel();
        jLabel3.setText("* 4 tracks");
//        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        textPlaylistInfo.add(jLabel3);

        playlistInfo.add(textPlaylistInfo);
        add(playlistInfo);

        // -------- playlist actions buttons --------
        JPanel playlistActions = new JPanel();
        playlistActions.setAlignmentX(0.0F);
        playlistActions.setLayout(new javax.swing.BoxLayout(playlistActions, javax.swing.BoxLayout.LINE_AXIS));

        syncButton = new JButton();
        syncButton.setText("Sync enabled");
        playlistActions.add(syncButton);

        downloadButton = new JButton();
        downloadButton.setText("Download");
        playlistActions.add(downloadButton);

        add(playlistActions);
    }
}

class PlaylistContent extends JPanel {
    private JLabel jTrack;
    private JLabel jArtist;
    private JLabel jStatus;

    public PlaylistContent(String track, String artist, String status) {
        initComponents(track, artist, status);
    }

    private void initComponents(String track, String artist, String status) {
        setAlignmentX(0.0F);
        setMaximumSize(new java.awt.Dimension(2147483647, 18));
        setMinimumSize(new java.awt.Dimension(212, 18));
        java.awt.GridBagLayout contentLayout = new java.awt.GridBagLayout();
        contentLayout.columnWidths = new int[]{1, 5, 3, 4};
        contentLayout.columnWeights = new double[]{1.0, 5.0, 3.0, 4.0};
        setLayout(contentLayout);

        JLabel jLabel4 = new JLabel();
        jLabel4.setText("icon");
        add(jLabel4, new java.awt.GridBagConstraints());

        jTrack = new JLabel();
        jTrack.setText(track);
        add(jTrack, new java.awt.GridBagConstraints());

        jArtist = new JLabel();
        jArtist.setText(artist);
        add(jArtist, new java.awt.GridBagConstraints());

        jStatus = new JLabel();
        jStatus.setText(status);
        add(jStatus, new java.awt.GridBagConstraints());
    }
}