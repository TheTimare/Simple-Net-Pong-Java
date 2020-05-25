package by.tshmofen.simplepong.presentation.graphics;

import by.tshmofen.simplepong.domain.AppTabs;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;

import static by.tshmofen.simplepong.domain.Config.*;

public class ConnectNetPanel extends JPanel {
    private final int width;
    private final int height;

    private final JTextField portField;
    private final JTextField ipField;

    public ConnectNetPanel() {
        this.grabFocus();
        width = AppTabs.frame.getContentPane().getWidth();
        height = AppTabs.frame.getContentPane().getHeight();

        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(new JButton("Connect The Game"));
        buttons.add(new JButton("Return"));
        setDesign(buttons);

        JLabel portLabel = new JLabel("Port");
        portField = new JTextField("8080");

        portLabel.setBounds(width / 2 - MENU_WIDTH/2, height * 3 / 12,
                MENU_WIDTH / 5 - 5, height / 12 - 5);
        portLabel.setFont(new Font(APP_FONT, Font.BOLD, 22));
        portLabel.setForeground(MENU_BUTTON_BACKGROUND_COLOR);
        portField.setBounds(width / 2 - MENU_WIDTH/2 + MENU_WIDTH / 5, height * 3 / 12,
                MENU_WIDTH * 4 / 5 - 5, height / 12 - 5);
        portField.setFont(new Font(APP_FONT, Font.BOLD, 22));

        JLabel ipLabel = new JLabel("IP");
        ipField = new JTextField();
        ipLabel.setBounds(width / 2 - MENU_WIDTH/2, height * 4 / 12,
                MENU_WIDTH / 5 - 5, height / 12 - 5);
        ipLabel.setFont(new Font(APP_FONT, Font.BOLD, 22));
        ipLabel.setForeground(MENU_BUTTON_BACKGROUND_COLOR);
        ipField.setBounds(width / 2 - MENU_WIDTH/2 + MENU_WIDTH/5, height * 4 / 12,
                MENU_WIDTH*4/5 - 5, height / 12 - 5);
        ipField.setFont(new Font(APP_FONT, Font.BOLD, 22));

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

    private void setDesign(ArrayList<JButton> buttons) {
        this.setBackground(MENU_BACKGROUND_COLOR);
        for (int i = 0; i < buttons.size(); i++){
            JButton button = buttons.get(i);
            button.setBounds(width / 2 - MENU_WIDTH/2
                    , height * (3*i + 2) / 12, MENU_WIDTH, height / 12 - 5);
            button.setBackground(MENU_BUTTON_BACKGROUND_COLOR);
            button.setForeground(MENU_BUTTON_FOREGROUND_COLOR);
            button.setFont(new Font(APP_FONT, Font.BOLD, 22));
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
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttons.get(1).addActionListener(e -> {
            AppTabs.frame.setContentPane(AppTabs.menu);
            AppTabs.frame.setVisible(true);
        });
    }
}
