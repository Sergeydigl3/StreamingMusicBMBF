package ru.dingo3.streamingmusicbmbf.ui;

import ru.dingo3.streamingmusicbmbf.converters.AbstractConverter;
import ru.dingo3.streamingmusicbmbf.converters.SingletonConverterArray;
import ru.dingo3.streamingmusicbmbf.core.ProviderManager;
import ru.dingo3.streamingmusicbmbf.core.AppSettings;
import ru.dingo3.streamingmusicbmbf.helpers.CachedImageIconDb;
import ru.dingo3.streamingmusicbmbf.layouts.WrapLayout;
import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;
import ru.dingo3.streamingmusicbmbf.providers.YandexProvider;
import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.ui.components.DashedRoundedButton;
import ru.dingo3.streamingmusicbmbf.ui.components.DashedRoundedSimpleButton;
import ru.dingo3.streamingmusicbmbf.views.providers.AbstractProviderView;
import ru.dingo3.streamingmusicbmbf.views.providers.YandexProviderView;

import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;


public class MainCards extends JFrame {
    private Image logo;
    //    private final java.util.List<AbstractProvider> providers = new ArrayList<>();
    private JPanel leftPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private ProviderManager providerManager;

    public MainCards() {
        AppSettings appSettings = AppSettings.getInstance();

        InputStream streamLogo = getClass().getResourceAsStream("/logo2.png");
        // Load the logo image.
        try {
            logo = ImageIO.read(streamLogo);
        } catch (Exception e) {
            // Error box Swing
            System.out.println("Error loading logo");
        }
        setIconImage(logo);
        setTitle("BMBF Streaming Music");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                providerManager.saveDbToDisk();
                System.exit(0);
            }
        });
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(970, 580));

        providerManager = new ProviderManager();
        providerManager.setFilePath(appSettings.getCachePath().toString() + "/providers.dat");
        providerManager.loadDbFromDisk();
        // Create providers

        ArrayList<AbstractProviderView> providerViews = new ArrayList<>();
        YandexProvider yandexProvider = new YandexProvider(appSettings.getCachePath());
        YandexProviderView yandexProviderView = new YandexProviderView(yandexProvider);
        providerViews.add(yandexProviderView);

        providerManager.addProvider(yandexProvider);

//        switch (appSettings.getConverter()) {
//            case "yandex":
//                yandexProvider.setConverter(new AbstractConverter() {
//                    @Override
//                    public void convertTrack(BaseTrack track) {
//                        System.out.println("Convert track: " + track.getTrackName());
//                    }
//                });
//                break;
//            default:
//                System.out.println("Converter not found");
//        }
//
//        appSettings.
//        providerManager.set
        providerManager.setConverter(SingletonConverterArray.getInstance().getConverterById(appSettings.getConverterId()));
        String mapsPath = appSettings.getCachePath().toString() + "/maps";
        // Check maps folder for exist. If not exist - create it. mapsPath - is a folder. DO NOT CHECK parent folder.
        if (!new java.io.File(mapsPath).exists()) {
            System.out.println("Create maps folder");
            new java.io.File(mapsPath).mkdirs();
        }





        SingletonConverterArray.getInstance().setMapsPath(mapsPath);

        // Create the card panel with CardLayout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Отступы по краям



        cardPanel.add(new HomeCard(), "home");

        for (AbstractProviderView provider : providerViews) {
            cardPanel.add(new BaseCard(provider, providerManager), provider.getProvider().getProviderId());
        }

        // Create the button panel

        leftPanel = new MusicPanelButtons(providerViews, cardLayout, cardPanel);

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

    public MusicPanelButtons(ArrayList<AbstractProviderView> providers, CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(23, 23, 20, 10)); // Отступы по краям
        // Create the button panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 23)); // Отступы по краям

        ButtonGroup buttonGroup = new ButtonGroup();

        // Create home button
        DashedRoundedButton homeCardButton = new DashedRoundedButton("Home", "home");
        homeCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "home");
            }
        });

        // add backlogo
        InputStream streamBackLogo = getClass().getResourceAsStream("/backlogo.png");
        Image backLogo = null;
        try {
            backLogo = ImageIO.read(streamBackLogo);
            // Resize image
            backLogo = backLogo.getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            // Error box Swing
            System.out.println("Error loading backlogo");
        }
        ImageIcon backLogoIcon = new ImageIcon(backLogo);
        JLabel backLogoLabel = new JLabel(backLogoIcon, SwingConstants.CENTER);
        JLabel nameLabel = new JLabel("BMBF Streaming Music", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        buttonsPanel.add(backLogoLabel);
//        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(nameLabel);
        buttonsPanel.add(Box.createVerticalStrut(22));

        buttonsPanel.add(homeCardButton);
        buttonGroup.add(homeCardButton);
        buttonsPanel.add(Box.createVerticalStrut(30));
        // Create buttons for each provider
        for (AbstractProviderView providerView : providers) {
            AbstractProvider provider = providerView.getProvider();
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

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getClass().getResourceAsStream("/home.txt");

            if(is != null) {
                br = new BufferedReader(new InputStreamReader(is));

                //Print the content of Test.txt
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } else {
                //Resource not found
                System.out.println("Resource unavailable!");
            }

        } catch (IOException e) {
            e.printStackTrace(); //Log IO Error
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {}
            }
        }



        JLabel appInfoLabel = new JLabel("<html>" + sb.toString() + "</html>");
        appInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        appInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        appInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        AppInfo.add(appInfoLabel);



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
    public BaseCard(AbstractProviderView provider, ProviderManager providerManager) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
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


        JLabel titleLabel = new JLabel(provider.getLeftTitleText());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel rightLabel = new JLabel("Total playlist: 0");
        rightLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rightLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
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
        ArrayList<BasePlaylist> playlists = provider.getProvider().getPlaylists();
        rightLabel.setText("Total playlist: " + playlists.size());
        if (playlists != null) {
            for (BasePlaylist playlist : playlists) {
                playlistPanel.add(new PlaylistPanel(playlist, provider.getProvider(), providerManager));
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

        JCheckBox checkBox = new JCheckBox("Enable background sync (not working): ");
        checkBox.setHorizontalTextPosition(SwingConstants.LEFT);

//        JButton performSyncButton = new JButton("Perform sync");
        DashedRoundedSimpleButton performSyncButton = new DashedRoundedSimpleButton("Perform sync");
        performSyncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                providerManager.getRecentDataTrack(provider.getProvider());
                providerManager.performSync(provider.getProvider());
            }
        });
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
    public PlaylistPanel(BasePlaylist playlist, AbstractProvider provider, ProviderManager providerManager) {
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
        titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
//        titleLabel.setPreferredSize(new Dimension(150, 50));
        add(titleLabel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open playlist
                System.out.println("Clicked on playlist " + playlist.getId());

                JFrame dialog = new PlaylistApp(provider, playlist, providerManager);
                dialog.setVisible(true);
            }
        });
    }
}

class SettingsDialog extends JFrame{
    private Image logo;

    public SettingsDialog(ArrayList<AbstractProviderView> providers) {
//        super(owner, "Settings", true);
        setTitle("Settings");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        setUndecorated(true);
        setLayout(new BorderLayout());
        setSize(new Dimension(400, 420));
//        setPreferredSize(new Dimension(400, 300));
//        setMinimumSize(new Dimension(400, 300));
//        setResizable(false);
        setLocationRelativeTo(null);

        InputStream streamLogo = getClass().getResourceAsStream("/logo2.png");

        logo = Toolkit.getDefaultToolkit().getImage("media/logo2.png");
        try {
            logo = ImageIO.read(streamLogo);
        } catch (Exception e) {
            // Error box Swing
            System.out.println("Error loading logo");
        }
        setIconImage(logo);

        // First is AppSettings
        AppSettings appSettings = AppSettings.getInstance();

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setBorder(new EmptyBorder(15, 0, 14, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
//        add(, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints center = new GridBagConstraints();
        center.insets = new Insets(5, 10, 5, 10);
        center.fill = GridBagConstraints.HORIZONTAL;
        center.gridx = 0;
        center.gridy = 0;
        center.weightx = 1;
        center.weighty = 1;
//        center.anchor = GridBagConstraints;
        center.ipady = 10;

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
        for (AbstractProviderView providerView : providers) {
            AbstractProvider provider = providerView.getProvider();
            currentElement = new ComboBoxElement(provider.getProviderName(), provider.getProviderId());
            defaultPageComboBox.addItem(currentElement);
            if (currentElement.getValue().equals(appSettings.getStartPage())) {
                selectedElement = currentElement;
            }
        }

        // TODO: Convert Providers to Singletone
        defaultPageComboBox.setSelectedItem(selectedElement);
        appSettingsPanel.add(defaultPageComboBox, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        appSettingsPanel.add(new JLabel("Default converter:"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 8;




        JComboBox<ComboBoxElement> defaultConverterComboBox = new JComboBox<>();
//        defaultConverterComboBox.addItem(currentElement);
        for (AbstractConverter converter : SingletonConverterArray.getInstance().getConverters()) {
            currentElement = new ComboBoxElement(converter.getConverterName(), converter.getConverterId());
            defaultConverterComboBox.addItem(currentElement);
            if (currentElement.getValue().equals(appSettings.getConverterId())) {
                selectedElement = currentElement;
            }
        }

        defaultConverterComboBox.setSelectedItem(selectedElement);
        appSettingsPanel.add(defaultConverterComboBox, c);

        // Is delivery enabled
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        appSettingsPanel.add(new JLabel("Enable delivery:"), c);
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 8;
        JCheckBox enableDeliveryCheckBox = new JCheckBox();
        enableDeliveryCheckBox.setSelected(appSettings.isDeliveryBMBF());
        appSettingsPanel.add(enableDeliveryCheckBox, c);

        // Delivery path
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        appSettingsPanel.add(new JLabel("Delivery path:"), c);
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 8;
        JTextField deliveryPathTextField = new JTextField(appSettings.getBmbfApiUrl());
//        deliveryPathTextField.setText(appSettings.getBmbfApiUrl());
        appSettingsPanel.add(deliveryPathTextField, c);



        centerPanel.add(appSettingsPanel, center);
        for (AbstractProviderView providerView : providers) {
            center.gridy++;
            centerPanel.add(providerView.getSettingsPanel(), center);
        }

        add(centerPanel, BorderLayout.CENTER);

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
            appSettings.setConverterId(((ComboBoxElement) Objects.requireNonNull(defaultConverterComboBox.getSelectedItem())).getValue());
            appSettings.setDeliveryBMBF(enableDeliveryCheckBox.isSelected());
            appSettings.setBmbfApiUrl(deliveryPathTextField.getText());

            for (AbstractProviderView providerView : providers) {
                providerView.saveSettings();
            }

            appSettings.saveConfig();
            JOptionPane.showMessageDialog(null, "Settings saved. Restart the app.", "Info", JOptionPane.INFORMATION_MESSAGE);
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