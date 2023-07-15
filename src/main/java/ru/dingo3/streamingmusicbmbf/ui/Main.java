package ru.dingo3.streamingmusicbmbf.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class DashedRoundedButton extends JToggleButton {
    private int arcWidth = 20;
    private int arcHeight = 20;

    private int width = 20;
    private int height = 20;

    public DashedRoundedButton(String text, int width, int height, int arcWidth, int arcHeight) {
        super(text);
        this.width = width;
        this.height = height;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }
//    public DashedRoundedButton(String text, int width, int height) {
//        this(text, width, height);
//    }
    public DashedRoundedButton(String text) {
        this(text, 20, 20, 20, 20);
    }



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Set the background color
        if (isSelected()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }

        // Draw the rounded rectangle
        int arcWidth = 20;
        int arcHeight = 20;
        g2.fillRoundRect(0, 0, width, height, this.arcWidth, this.arcHeight);

        // Set the foreground color
        g2.setColor(getForeground());

        // Draw the border
        if (isSelected()) {
            g2.setStroke(new BasicStroke(2.5f));
        } else {
            float[] dashPattern = {5f, 5f};
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1f, dashPattern, 0f));
        }
        g2.drawRoundRect(0, 0, width - 1, height - 1, arcWidth, arcHeight);

        g2.dispose();
        super.paintComponent(g);
    }
}

public class Main extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel loggedInLabel;
    private JLabel totalPlaylistsLabel;

    public Main() {
        setTitle("Playlist App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // set preferred size
        setPreferredSize(new Dimension(900, 600));

        // Create the left panel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.setPreferredSize(new Dimension(150, getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 10, 30));

        ButtonGroup buttonGroup = new ButtonGroup();

        // Create buttons for the left panel
        DashedRoundedButton button1 = new DashedRoundedButton("Option 1");
        DashedRoundedButton button2 = new DashedRoundedButton("Option 2");
        DashedRoundedButton button3 = new DashedRoundedButton("Option 3");


        buttonGroup.add(button1);
        buttonGroup.add(button2);
        buttonGroup.add(button3);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Selected: " + ((DashedRoundedButton) e.getSource()).getText());
            }
        };

        button1.addActionListener(actionListener);
        button2.addActionListener(actionListener);
        button3.addActionListener(actionListener);
        // print button group on change

//        System.out.println(buttonGroup.getSelection());

        // JButton button1 = createStyledButton("Button 1");
        // JButton button2 = createStyledButton("Button 2");
        // JButton button3 = createStyledButton("Button 3");

        // Add buttons to the left panel
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(button1);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(button2);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(button3);

        // Create the right panel
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create the labels for the header panel
        loggedInLabel = new JLabel("You are logged in as Sergeydigl3");
        totalPlaylistsLabel = new JLabel("Total Playlists: 2");

        // Add the labels to the header panel
        headerPanel.add(loggedInLabel, BorderLayout.WEST);
        headerPanel.add(totalPlaylistsLabel, BorderLayout.EAST);

        // Create the flow panel for playlists
        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        flowPanel.setPreferredSize(new Dimension(400, 300));
        flowPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Create playlists for the flow panel
        JPanel playlist1 = createPlaylistPanel("Playlist 1", "path/to/image1.png");
        JPanel playlist2 = createPlaylistPanel("Playlist 2", "path/to/image2.png");
        JPanel playlist3 = createPlaylistPanel("Playlist 3", "path/to/image3.png");
        JPanel playlist4 = createPlaylistPanel("Playlist 4", "path/to/image4.png");

        // Add playlists to the flow panel
        flowPanel.add(playlist1);
        flowPanel.add(playlist2);
        flowPanel.add(playlist3);
        flowPanel.add(playlist4);

        // Add the header panel and flow panel to the right panel
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(flowPanel, BorderLayout.CENTER);

        // Add the panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        Border border = BorderFactory.createDashedBorder(Color.BLACK, 2, 5, 2, true);
        Border roundedBorder = BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setBorder(roundedBorder);
        return button;
    }

    private JPanel createPlaylistPanel(String title, String imagePath) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(150, 150));
        panel.setMinimumSize(new Dimension(150, 150));
        panel.setMaximumSize(new Dimension(150, 150));
        panel.setLayout(new BorderLayout());

        // Create and configure the image label
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(imagePath));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        panel.add(imageLabel, BorderLayout.CENTER);

        // Create and configure the title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}