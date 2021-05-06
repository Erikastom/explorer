package com.company;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class ViewProperties {
    private ControllerProperties controllerProperties;
    private JFrame frame;
    private JLabel label;
    private ImageIcon icon;

    public void setControllerProperties(ControllerProperties controllerProperties) {
        this.controllerProperties = controllerProperties;
    }

    public void create(String folderIconPath) {
        frame = new JFrame("Properties");
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        label = new JLabel();
        label.setBounds(20, 20, 100, 100);
        label.setIcon(icon = new ImageIcon(folderIconPath));
        frame.add(label);

        frame.setVisible(true);
    }
}
