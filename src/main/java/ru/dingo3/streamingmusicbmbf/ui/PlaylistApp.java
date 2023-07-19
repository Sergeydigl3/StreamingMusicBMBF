package ru.dingo3.streamingmusicbmbf.ui;

import ru.dingo3.streamingmusicbmbf.core.ProviderManager;
import ru.dingo3.streamingmusicbmbf.helpers.CachedImageIconDb;
import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;
import ru.dingo3.streamingmusicbmbf.providers.models.SyncState;
import ru.dingo3.streamingmusicbmbf.ui.components.DashedRoundedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//class RoundedImageExample extends JPanel {
//    private BufferedImage image;
//
//    public RoundedImageExample() {
//        try {
//            image = ImageIO.read(new File("playlist1.jpg"));
//            image = image.getSubimage(0, 0, 150, 150);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g.create();
//
//        int x = (getWidth() - image.getWidth()) / 2;
//        int y = (getHeight() - image.getHeight()) / 2;
//
//        RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, image.getWidth(), image.getHeight(), 50, 50);
//        g2d.clip(rect);
//        g2d.drawImage(image, x, y, this);
//
//        g2d.dispose();
//    }
//}

public class PlaylistApp extends JDialog {
    private PlaylistHeader header;
//    private CachedImageIconDb cashedImageIconDb;

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    public PlaylistApp(AbstractProvider provider, BasePlaylist playlist, ProviderManager providerManager) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 400));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

//        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.PAGE_AXIS));

        header = new PlaylistHeader(provider, playlist, providerManager);
        panel.add(header);
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
//        separator.setBackground(Color.BLUE);
        panel.add(separator);
//        panel.setBackground(Color.BLUE);
        ArrayList<BaseTrack> providerTracks = provider.getTracks(playlist.getId());

        MusicList musicList = new MusicList(providerManager, provider, playlist, providerTracks, executorService);
        panel.add(musicList);


        if (providerTracks != null) {
            for (BaseTrack track : providerTracks) {
                musicList.addRow(track, false);
            }
        }
//        musicList.addRow("TRACK", "ARTIST", "STATUS", false);
//        musicList.addRow("TRACK", "ARTIST", "STATUS", false);
//        musicList.addRow("TRACK", "ARTIST", "STATUS", false);

        add(panel);
        pack();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                executorService.shutdownNow();
            }
        });
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlaylistApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

//        java.awt.EventQueue.invokeLater(() -> {
//            new PlaylistApp().setVisible(true);
//        });
    }
}

class PlaylistHeader extends JPanel {
    private JPanel playlistInfo;
    private DashedRoundedButton syncButton;
    private JButton downloadButton;
    CachedImageIconDb cachedImageIconDb;

    public PlaylistHeader(AbstractProvider provider, BasePlaylist playlist, ProviderManager providerManager) {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));


        // -------- playlist info --------
        playlistInfo = new JPanel();
        playlistInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        playlistInfo.setLayout(new javax.swing.BoxLayout(playlistInfo, javax.swing.BoxLayout.LINE_AXIS));
        playlistInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        cachedImageIconDb = new CachedImageIconDb(provider.getCachePath().toString());

//        ImageIcon playlistImage = new ImageIcon("playlist1.jpg");
//        ImageIcon playlistImage = cachedImageIconDb.getByUrlResized(playlist.getImage(), 150,150);
//        Image scaledImage = playlistImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
//        ImageIcon scaledPlaylistImage = new ImageIcon(scaledImage);
        JLabel playlistImageLabel = new JLabel(cachedImageIconDb.getByUrlResized(playlist.getImage(), 150, 150));
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
//        jLabel2.setText("My cool playlist");
        jLabel2.setText(playlist.getTitle());
        jLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        textPlaylistInfo.add(jLabel2);

        JLabel jLabel3 = new JLabel();
        jLabel3.setText("* " + playlist.getMusicCount() + " tracks");
//        jLabel3.setText("* 4 tracks");
//        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        textPlaylistInfo.add(jLabel3);

        playlistInfo.add(textPlaylistInfo);
        add(playlistInfo);

        // -------- playlist actions buttons --------
        JPanel playlistActions = new JPanel();
        playlistActions.setAlignmentX(Component.LEFT_ALIGNMENT);
        playlistActions.setLayout(new javax.swing.BoxLayout(playlistActions, javax.swing.BoxLayout.LINE_AXIS));

        if (providerManager.getPlaylistSyncState(provider.getProviderId(), playlist.getId())) {
            syncButton = new DashedRoundedButton("Sync enabled");
            syncButton.setSelected(true);
        } else {
            syncButton = new DashedRoundedButton("Sync disabled");
            syncButton.setSelected(false);
        }
//        syncButton = new DashedRoundedButton("Sync disabled");
//        syncButton.setSelected(false);
        syncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (syncButton.getModel().isSelected()) {
                    syncButton.setText("Sync enabled");

                } else {
                    syncButton.setText("Sync disabled");
                }
//                System.out.println("TOAWDWADOAWDWD");
                providerManager.setPlaylistSyncState(provider.getProviderId(), playlist, syncButton.getModel().isSelected());
                providerManager.saveDbToDisk();
            }
        });
        playlistActions.add(syncButton);

//        downloadButton = new JButton();
//        downloadButton.setText("Download");
//        playlistActions.add(downloadButton);

        add(playlistActions);
    }
}


class MusicList extends JScrollPane {
    private JPanel headerPanel;
    private JPanel mainPanel;

    private ConcurrentHashMap<BaseTrack, JLabel> labelStorage = new ConcurrentHashMap<>();

    ArrayList<String> trackIds = new ArrayList<>();
//    private ArrayList<PlaylistContent> playlistContent;

    Thread labelStorageThread;

    public MusicList(ProviderManager providerManager, AbstractProvider provider, BasePlaylist playlist, ArrayList<BaseTrack> playlistContent, ExecutorService executorService) {
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        mainPanel = new JPanel();
//        mainPanel.setBackground(Color.YELLOW);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        setColumnHeaderView(new JPanel(new GridLayout(1, 3)));

        headerPanel = new JPanel(new GridLayout(1, 3));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel titleLabel = new JLabel("TITLE");
        JLabel artistLabel = new JLabel("ARTIST");
        JLabel statusLabel = new JLabel("STATUS");

        headerPanel.add(titleLabel);
        headerPanel.add(artistLabel);
        headerPanel.add(statusLabel);

        setColumnHeaderView(headerPanel);

        setViewportView(mainPanel);

        for (BaseTrack track : playlistContent) {
            trackIds.add(track.getId());
        }
        labelStorageThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (labelStorage.size() > 0) {
                        System.out.println("Terminator is running");
                        ArrayList<BaseTrack> tracksByIds = providerManager.getTracksByIds(provider.getProviderId(), trackIds);
                        for (BaseTrack track : tracksByIds) {
                            if (labelStorage.containsKey(track)) {
                                JLabel label = labelStorage.get(track);
                                if (label != null) {
                                    if (track.getSyncState() == SyncState.DOWNLOADED) {
                                        label.setText("Downloaded");
                                    } else if (track.getSyncState() == SyncState.DOWNLOADING) {
                                        label.setText("Downloading");
                                    } else if (track.getSyncState() == SyncState.NOT_DOWNLOADED) {
                                        label.setText("Not downloaded");
                                    }
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        executorService.submit(labelStorageThread);
    }

    public void addRow(BaseTrack track, boolean isHeader) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 3));
//        rowPanel.setBackground(Color.lightGray);
//        rowPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        rowPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel titleLabel = new JLabel(track.getTitle());
        JLabel artistLabel = new JLabel(track.getArtist());
        JLabel statusLabel = new JLabel("");

        labelStorage.put(track, statusLabel);

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