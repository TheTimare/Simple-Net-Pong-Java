package by.tshmofen.simplepong.presentation.graphics;

import by.tshmofen.simplepong.domain.AppTabs;

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

        this.setBackground(Color.DARK_GRAY);
        for (JButton button: buttons) {
            this.add(button);
        }
        this.setLayout(null);
        this.setSize(width, height);

        startListening(buttons);
    }

    private void setDesign(ArrayList<JButton> buttons) {
        int menuWidth = 300;
        for (int i = 0; i < buttons.size(); i++){
            JButton button = buttons.get(i);
            button.setBounds(width / 2 - menuWidth/2
                    , height * (i + 2) / 12, menuWidth, height / 12);
            button.setBackground(Color.LIGHT_GRAY);
            button.setForeground(Color.DARK_GRAY);
            button.setFont(new Font("Courier", Font.BOLD, 22));
        }
    }

    private void startListening(ArrayList<JButton> buttons) {
        buttons.get(0).addActionListener(e -> {
            AppTabs.frame.setContentPane(AppTabs.pong);
            AppTabs.frame.setVisible(true);
            AppTabs.pong.startTheGame();
        });

        buttons.get(3).addActionListener(e ->
                AppTabs.frame.dispatchEvent(new WindowEvent(AppTabs.frame, WindowEvent.WINDOW_CLOSING))
        );
    }
}
