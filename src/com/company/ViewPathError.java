package com.company;

import javax.swing.*;

public class ViewPathError {
    private ControllerPathError controllerPathError;
    private JFrame frame;
    private JLabel label;

    public void setControllerPathError(ControllerPathError controllerPathError) {
        this.controllerPathError = controllerPathError;
    }

    public void close() {
        frame.dispose();
    }

    public void setLabelText(String text) {
        label.setText(text);
    }

    public void create() {
        frame = new JFrame("Error");
        frame.setSize(600, 150);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JButton button = new JButton("OK");
        button.setBounds(495, 80, 80, 20);
        button.addActionListener(e -> controllerPathError.handleOkButtonClick());
        frame.add(button);

        label = new JLabel();
        label.setBounds(50, 30, 500, 40);
        frame.add(label);



        frame.setVisible(true);
    }
}
