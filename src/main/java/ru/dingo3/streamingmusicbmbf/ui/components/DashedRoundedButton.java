package ru.dingo3.streamingmusicbmbf.ui.components;

import javax.swing.*;
import java.awt.*;

public class DashedRoundedButton extends JToggleButton {
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
        setFont(new Font("Segoe UI", Font.BOLD, 14));
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