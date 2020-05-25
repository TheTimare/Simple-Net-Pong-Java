package by.tshmofen.simplepong.presentation.graphics;

import by.tshmofen.simplepong.domain.AppTabs;
import static by.tshmofen.simplepong.domain.Config.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MenuPanel extends JPanel {
    private final int width;
    private final int height;

    public MenuPanel() {
        this.grabFocus();
        width = AppTabs.frame.getContentPane().getWidth();
        height = AppTabs.frame.getContentPane().getHeight();

        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(new JButton("Start Local Game"));
        buttons.add(new JButton("Start Multiplayer Game"));
        buttons.add(new JButton("Connect to Game"));
        buttons.add(new JButton("Exit"));
        setDesign(buttons);

        for (JButton button: buttons) {
            this.add(button);
        }
        this.setLayout(null);
        this.setSize(width, height);

        startListening(buttons);
    }

    private void setDesign(ArrayList<JButton> buttons) {
        this.setBackground(MENU_BACKGROUND_COLOR);
        for (int i = 0; i < buttons.size(); i++){
            JButton button = buttons.get(i);
            button.setBounds(width / 2 - MENU_WIDTH/2
                    , height * (i + 2) / 12, MENU_WIDTH, height / 12 - 5);
            button.setBackground(MENU_BUTTON_BACKGROUND_COLOR);
            button.setForeground(MENU_BUTTON_FOREGROUND_COLOR);
            button.setFont(new Font(APP_FONT, Font.BOLD, 22));
        }
    }

    private void startListening(ArrayList<JButton> buttons) {
        buttons.get(0).addActionListener(e -> {
            AppTabs.frame.setContentPane(AppTabs.pong);
            AppTabs.frame.setVisible(true);
            AppTabs.pong.startTheGame();
        });

        buttons.get(1).addActionListener(e -> {
            AppTabs.frame.setContentPane(AppTabs.startNetMenu);
            AppTabs.frame.setVisible(true);
        });

        buttons.get(2).addActionListener(e -> {
            AppTabs.frame.setContentPane(AppTabs.connectNetPanel);
            AppTabs.frame.setVisible(true);
        });

        buttons.get(3).addActionListener(e ->
                AppTabs.frame.dispatchEvent(new WindowEvent(AppTabs.frame, WindowEvent.WINDOW_CLOSING))
        );
    }
}
