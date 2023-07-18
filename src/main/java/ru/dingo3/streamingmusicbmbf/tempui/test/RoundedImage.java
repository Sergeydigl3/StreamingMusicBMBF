package ru.dingo3.streamingmusicbmbf.tempui.test;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedImage {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoundedImage::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Create a JFrame to hold the components
        JFrame frame = new JFrame("Rounded Image Border Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Create a JLabel to display the image
        ImageIcon imageIcon = new ImageIcon("playlist1.jpg");
        JLabel imageLabel = new JLabel(imageIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                // Create a rounded rectangle shape to clip the image
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 20, 20);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setClip(roundedRect);

                // Call the parent's paintComponent to draw the image
                super.paintComponent(g);

                g2.dispose();
            }
        };

        // Set a rounded border on the label
        Border roundedBorder = BorderFactory.createLineBorder(Color.BLACK, 25);
        roundedBorder = BorderFactory.createCompoundBorder(
                roundedBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        imageLabel.setBorder(roundedBorder);

        // Add the label to the frame
        frame.getContentPane().add(imageLabel);

        // Show the frame
        frame.setVisible(true);
    }
}

