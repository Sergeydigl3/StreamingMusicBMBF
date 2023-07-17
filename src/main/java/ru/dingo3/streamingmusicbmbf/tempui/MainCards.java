package ru.dingo3.streamingmusicbmbf.tempui;

import ru.dingo3.streamingmusicbmbf.helpers.AppSettings;
import ru.dingo3.streamingmusicbmbf.helpers.CachedImageIconDb;
import ru.dingo3.streamingmusicbmbf.layouts.WrapLayout;
import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;
import ru.dingo3.streamingmusicbmbf.providers.YandexProvider;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.ui.PlaylistApp;
import ru.dingo3.streamingmusicbmbf.ui.components.DashedRoundedButton;
import ru.dingo3.streamingmusicbmbf.ui.components.DashedRoundedSimpleButton;

import javax.swing.*;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;


public class MainCards extends JFrame {
    private Image logo;
    private final java.util.List<AbstractProvider> providers = new ArrayList<>();
    private JPanel leftPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MainCards() {
        AppSettings appSettings = AppSettings.getInstance();

        // Load the logo image.
        logo = Toolkit.getDefaultToolkit().getImage("media/logo2.png");
        setIconImage(logo);
        setTitle("Main Cards");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 600));

        // Create providers
        YandexProvider yandexProvider = new YandexProvider(appSettings.getCachePath());
        providers.add(yandexProvider);


        // Create the card panel with CardLayout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.add(new HomeCard(), "home");

        for (AbstractProvider provider : providers) {
            cardPanel.add(new BaseCard(provider), provider.getProviderId());
        }

        // Create the button panel

        leftPanel = new MusicPanelButtons(providers, cardLayout, cardPanel);

        cardLayout.show(cardPanel, appSettings.getStartPage());

        add(leftPanel, BorderLayout.WEST);



        add(cardPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainCards::new);
    }
}

class MusicPanelButtons extends JPanel {
    private static SettingsDialog settingsDialog;

    public MusicPanelButtons(java.util.List<AbstractProvider> providers, CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new BorderLayout());

        // Create the button panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10)); // Отступы по краям

        ButtonGroup buttonGroup = new ButtonGroup();

        // Create home button
        DashedRoundedButton homeCardButton = new DashedRoundedButton("Home", "home");
        homeCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "home");
            }
        });

        buttonsPanel.add(homeCardButton);
        buttonGroup.add(homeCardButton);
        buttonsPanel.add(Box.createVerticalStrut(20));
        // Create buttons for each provider
        for (AbstractProvider provider : providers) {
            DashedRoundedButton providerButton = new DashedRoundedButton(provider.getProviderName(), provider.getProviderId());
            providerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println(provider.getProviderId());
                    cardLayout.show(cardPanel, provider.getProviderId());
                }
            });
            buttonsPanel.add(Box.createVerticalStrut(10));
            buttonsPanel.add(providerButton);
            buttonGroup.add(providerButton);
        }

//        JButton yandexCardButton = new JButton("Yandex Music");

        DashedRoundedButton youtubeCardButton = new DashedRoundedButton("Youtube Music", "youtube");
        DashedRoundedButton localMusicCardButton = new DashedRoundedButton("Local Music", "local");


//        homeCardButton.addActionListener(this);
//        yandexCardButton.addActionListener(this);
//        youtubeCardButton.addActionListener(this);
//        localMusicCardButton.addActionListener(this);


//        buttonsPanel.add(yandexCardButton);
        buttonsPanel.add(Box.createVerticalStrut(20));
        buttonsPanel.add(youtubeCardButton);
        buttonsPanel.add(Box.createVerticalStrut(20));
        buttonsPanel.add(localMusicCardButton);

        buttonGroup.add(youtubeCardButton);
        buttonGroup.add(localMusicCardButton);

        // set selected button
        for (Component component : buttonsPanel.getComponents()) {
            if (component instanceof DashedRoundedButton) {
                DashedRoundedButton button = (DashedRoundedButton) component;
                if (button.getButtonId().equals(AppSettings.getInstance().getStartPage())) {
                    button.setSelected(true);
                }
            }
        }

        JPanel statusSettingsPanel = new JPanel();
        statusSettingsPanel.setLayout(new BoxLayout(statusSettingsPanel, BoxLayout.Y_AXIS));

        DashedRoundedSimpleButton settingsButton = new DashedRoundedSimpleButton("Settings");
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                settingsDialog = new SettingsDialog(providers);
                settingsDialog.setVisible(true);
            }
        });
//        JButton statusButton = new JButton("Status");

        statusSettingsPanel.add(settingsButton);
//        statusSettingsPanel.add(statusButton);

        add(buttonsPanel, BorderLayout.NORTH);
        add(statusSettingsPanel, BorderLayout.SOUTH);
    }
}

class HomeCard extends JPanel {
    public HomeCard() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setStroke(new BasicStroke(1.5F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 12, new float[]{8}, 3));
                g2d.setColor(Color.BLACK);
                g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2d.dispose();
            }
        };
        headerPanel.setBorder(new EmptyBorder(15, 0, 14, 0));
        headerPanel.setLayout(new BorderLayout());


        JLabel titleLabel = new JLabel("This is a home page");
        JLabel rightLabel = new JLabel("v0.1");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightLabel, BorderLayout.EAST);


        JPanel AppInfo = new JPanel();
        JScrollPane playlistScrollPane = new JScrollPane(AppInfo);
        playlistScrollPane.getVerticalScrollBar().setUnitIncrement(25);
        playlistScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playlistScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        playlistScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        playlistPanel.setLayout(new WrapLayout(FlowLayout.LEADING));
        AppInfo.setLayout(new BoxLayout(AppInfo, BoxLayout.Y_AXIS));

        AppInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));




//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setLayout(new BorderLayout());
//        bottomPanel.setBorder(
//                new CompoundBorder(
//                        BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray),
//                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
//
//                )
////                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray)
//        );
//
//        JCheckBox checkBox = new JCheckBox("Enable background sync: ");
//        checkBox.setHorizontalTextPosition(SwingConstants.LEFT);
//
//        JButton performSyncButton = new JButton("Perform sync");
////        performSyncButton.setPreferredSize(new Dimension(100, 20));
//
//        bottomPanel.add(checkBox, BorderLayout.WEST);
//        bottomPanel.add(performSyncButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(playlistScrollPane, BorderLayout.CENTER);
//        add(new JLabel("Base Card"), BorderLayout.CENTER);
//        add(bottomPanel, BorderLayout.SOUTH);


    }
}

class BaseCard extends JPanel {
    public BaseCard(AbstractProvider provider) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setStroke(new BasicStroke(1.5F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 12, new float[]{8}, 3));
                g2d.setColor(Color.BLACK);
                g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2d.dispose();
            }
        };
        headerPanel.setBorder(new EmptyBorder(15, 0, 14, 0));
        headerPanel.setLayout(new BorderLayout());


        JLabel titleLabel = new JLabel("You are logged in as SomeUser");
        JLabel rightLabel = new JLabel("Total playlist: 123");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightLabel, BorderLayout.EAST);


        JPanel playlistPanel = new JPanel();
        JScrollPane playlistScrollPane = new JScrollPane(playlistPanel);
        playlistScrollPane.getVerticalScrollBar().setUnitIncrement(25);
        playlistScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playlistScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        playlistScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        playlistPanel.setLayout(new WrapLayout(FlowLayout.LEADING));
        playlistPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        playlistPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        playlistPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        ArrayList<BasePlaylist> playlists = provider.getPlaylists();
        if (playlists != null) {
            for (BasePlaylist playlist : playlists) {
                playlistPanel.add(new PlaylistPanel(playlist, provider));
            }
        }
//        playlistPanel.add(new PlaylistPanel("Playlist 1", "playlist2.jpg"));
//        playlistPanel.add(new PlaylistPanel("Playlist 1", "playlist2.jpg"));
//        playlistPanel.add(new PlaylistPanel("Playlist 1", "playlist1.jpg"));


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(
                new CompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)

                )
//                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray)
        );

        JCheckBox checkBox = new JCheckBox("Enable background sync: ");
        checkBox.setHorizontalTextPosition(SwingConstants.LEFT);

//        JButton performSyncButton = new JButton("Perform sync");
        DashedRoundedSimpleButton performSyncButton = new DashedRoundedSimpleButton("Perform sync");
//        performSyncButton.setPreferredSize(new Dimension(100, 20));

        bottomPanel.add(checkBox, BorderLayout.WEST);
        bottomPanel.add(performSyncButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(playlistScrollPane, BorderLayout.CENTER);
//        add(new JLabel("Base Card"), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


    }
}

class PlaylistPanel extends JPanel {
    public PlaylistPanel(BasePlaylist playlist, AbstractProvider provider) {
        CachedImageIconDb cachedImageIconDb = new CachedImageIconDb(provider.getCachePath().toString());
        ImageIcon imageIcon = cachedImageIconDb.getByUrlResized(playlist.getImage(), 160, 160);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 15));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 200));
//        setMinimumSize(new Dimension(150, 150));
//        setMaximumSize(new Dimension(150, 150));
//        setSize(new Dimension(150, 150));

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        add(imageLabel, BorderLayout.CENTER);

        // Create and configure the title label
        JLabel titleLabel = new JLabel(playlist.getTitle());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open playlist
                System.out.println("Clicked on playlist " + playlist.getId());

                JDialog dialog = new PlaylistApp(provider, playlist);
                dialog.setVisible(true);
            }
        });
    }
}

class SettingsDialog extends JDialog {
    public SettingsDialog(java.util.List<AbstractProvider> providers) {
//        super(owner, "Settings", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        setUndecorated(true);
        setLayout(new BorderLayout());
        setSize(new Dimension(400, 300));
//        setPreferredSize(new Dimension(400, 300));
//        setMinimumSize(new Dimension(400, 300));
        setModal(false);
//        setResizable(false);
        setLocationRelativeTo(null);

        // First is AppSettings
        AppSettings appSettings = AppSettings.getInstance();

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setBorder(new EmptyBorder(15, 0, 14, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
//        add(, BorderLayout.NORTH);


        JPanel appSettingsPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        appSettingsPanel.setLayout(gridBagLayout);
        appSettingsPanel.setBorder(new TitledBorder("App settings"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 10, 5, 10);
        // Configure cache path and default page. Label in left side with weight 1 and text field in right side with weight 2
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        appSettingsPanel.add(new JLabel("Cache path:"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 8;
        JTextField cachePathTextField = new JTextField(appSettings.getCachePath().toString());
        cachePathTextField.setText(appSettings.getCachePath().toString());
        appSettingsPanel.add(cachePathTextField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        appSettingsPanel.add(new JLabel("Default page:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 8;

        // Options for default page
        JComboBox<ComboBoxElement> defaultPageComboBox = new JComboBox<>();
        ComboBoxElement currentElement = new ComboBoxElement("Home", "home");
        defaultPageComboBox.addItem(currentElement);
        ComboBoxElement selectedElement = currentElement;
        for (AbstractProvider provider : providers) {
            currentElement = new ComboBoxElement(provider.getProviderName(), provider.getProviderId());
            defaultPageComboBox.addItem(currentElement);
            if (currentElement.getValue().equals(appSettings.getStartPage())) {
                selectedElement = currentElement;
            }
        }

        defaultPageComboBox.setSelectedItem(selectedElement);
        appSettingsPanel.add(defaultPageComboBox, c);
        add(appSettingsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        DashedRoundedSimpleButton cancelButton = new DashedRoundedSimpleButton("Cancel");
        cancelButton.addActionListener(e -> {
            dispose();
        });
        bottomPanel.add(cancelButton, BorderLayout.WEST);

        DashedRoundedSimpleButton saveButton = new DashedRoundedSimpleButton("Save");
        saveButton.addActionListener(e -> {
            // Save settings

            appSettings.setCachePath(Paths.get(cachePathTextField.getText()));
            appSettings.setStartPage(((ComboBoxElement) Objects.requireNonNull(defaultPageComboBox.getSelectedItem())).getValue());
            appSettings.saveConfig();
            dispose();
        });
        bottomPanel.add(saveButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);












    }
}

class ComboBoxElement {
    private String name;
    private String value;

    public ComboBoxElement(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String toString() {
        return name;
    }

    public String getValue() {
        return value;
    }
}