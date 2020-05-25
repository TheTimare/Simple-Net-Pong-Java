package by.tshmofen.simplepong.presentation.graphics.net;

import by.tshmofen.simplepong.domain.AppTabs;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;

public class ConnectNetPanel extends JPanel {
    private final int width;
    private final int height;

    private final JTextField portField;
    private final JTextField ipField;

    public ConnectNetPanel() {
        this.grabFocus();
        width = AppTabs.frame.getContentPane().getWidth();
        height = AppTabs.frame.getContentPane().getHeight();
        int menuWidth = 300;

        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(new JButton("Connect The Game"));
        buttons.add(new JButton("Return"));
        setDesign(buttons, menuWidth);

        JLabel portLabel = new JLabel("Port");
        portField = new JTextField("8080");

        portLabel.setBounds(width / 2 - menuWidth/2, height * 3 / 12,
                menuWidth / 5 - 5, height / 12 - 5);
        portLabel.setFont(new Font("Courier", Font.BOLD, 22));
        portLabel.setForeground(Color.LIGHT_GRAY);
        portField.setBounds(width / 2 - menuWidth/2 + menuWidth / 5, height * 3 / 12,
                menuWidth * 4 / 5 - 5, height / 12 - 5);
        portField.setFont(new Font("Courier", Font.BOLD, 22));

        JLabel ipLabel = new JLabel("IP");
        ipField = new JTextField();
        ipLabel.setBounds(width / 2 - menuWidth/2, height * 4 / 12,
                menuWidth / 5 - 5, height / 12 - 5);
        ipLabel.setFont(new Font("Courier", Font.BOLD, 22));
        ipLabel.setForeground(Color.LIGHT_GRAY);
        ipField.setBounds(width / 2 - menuWidth/2 + menuWidth / 5, height * 4 / 12,
                menuWidth * 4 / 5 - 5, height / 12 - 5);
        ipField.setFont(new Font("Courier", Font.BOLD, 22));

        this.setBackground(Color.DARK_GRAY);
        for (JButton button: buttons) {
            this.add(button);
        }
        this.add(portLabel);
        this.add(portField);
        this.add(ipLabel);
        this.add(ipField);
        this.setLayout(null);
        this.setSize(width, height);

        startListening(buttons);
    }

    private void setDesign(ArrayList<JButton> buttons, int menuWidth) {
        for (int i = 0; i < buttons.size(); i++){
            JButton button = buttons.get(i);
            button.setBounds(width / 2 - menuWidth/2
                    , height * (3*i + 2) / 12, menuWidth, height / 12 - 5);
            button.setBackground(Color.LIGHT_GRAY);
            button.setForeground(Color.DARK_GRAY);
            button.setFont(new Font("Courier", Font.BOLD, 22));
        }
    }

    private void startListening(ArrayList<JButton> buttons) {
        buttons.get(0).addActionListener(e -> {
            try {
                int port = Integer.parseInt(portField.getText());
                if (port < 1024 || port > 65535 || ipField.getText().isEmpty()) {
                    throw new Exception();
                }
                InetAddress ip = InetAddress.getByName(ipField.getText());
                AppTabs.pong.connectTheGame(ip, port);
                AppTabs.frame.setContentPane(AppTabs.pong);
                AppTabs.frame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(AppTabs.frame, "Incorrect port or IP. Try again",
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttons.get(1).addActionListener(e -> {
            AppTabs.frame.setContentPane(AppTabs.menu);
            AppTabs.frame.setVisible(true);
        });
    }
}
