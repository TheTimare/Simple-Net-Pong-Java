package by.tshmofen.simplepong.presentation.graphics;

import by.tshmofen.simplepong.domain.AppTabs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static by.tshmofen.simplepong.domain.Config.*;

public class StartNetPanel extends JPanel {
    private final int width;
    private final int height;

    private final JTextField portField;

    public StartNetPanel() {
        this.grabFocus();
        width = AppTabs.frame.getContentPane().getWidth();
        height = AppTabs.frame.getContentPane().getHeight();

        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(new JButton("Start Multiplayer Server"));
        buttons.add(new JButton("Return"));
        setDesign(buttons);

        JLabel portLabel = new JLabel("Port");
        portField = new JTextField("8080");
        portLabel.setBounds(width / 2 - MENU_WIDTH/2, height * 3 / 12,
                MENU_WIDTH / 5 - 5, height / 12 - 5);
        portLabel.setFont(new Font(APP_FONT, Font.BOLD, 22));
        portLabel.setForeground(Color.LIGHT_GRAY);
        portField.setBounds(width / 2 - MENU_WIDTH/2 + MENU_WIDTH / 5, height * 3 / 12,
                MENU_WIDTH * 4 / 5 - 5, height / 12 - 5);
        portField.setFont(new Font(APP_FONT, Font.BOLD, 22));

        for (JButton button: buttons) {
            this.add(button);
        }
        this.add(portLabel);
        this.add(portField);
        this.setLayout(null);
        this.setSize(width, height);

        startListening(buttons);
    }

    private void setDesign(ArrayList<JButton> buttons) {
        this.setBackground(MENU_BACKGROUND_COLOR);
        for (int i = 0; i < buttons.size(); i++){
            JButton button = buttons.get(i);
            button.setBounds(width / 2 - MENU_WIDTH/2
                    , height * (2*i + 2) / 12, MENU_WIDTH, height / 12 - 5);
            button.setBackground(MENU_BUTTON_BACKGROUND_COLOR);
            button.setForeground(MENU_BUTTON_FOREGROUND_COLOR);
            button.setFont(new Font(APP_FONT, Font.BOLD, 22));
        }
    }

    private void startListening(ArrayList<JButton> buttons) {
        buttons.get(0).addActionListener(e -> {
            try {
                JOptionPane jop = new JOptionPane();
                jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                jop.setMessage("Server is waiting for connections.\n"
                        + "Click OK to stop listening." );
                JDialog dialog = jop.createDialog(AppTabs.frame, "Listening");

                int port = Integer.parseInt(portField.getText());
                AppTabs.pong.startTheMultiplayerGame(port, dialog);
                dialog.setVisible(true);
                if (AppTabs.pong.isBadConnection()){
                    System.out.println("working");
                    AppTabs.pong.stopTheGame();
                    throw new Exception("exit");
                }

                AppTabs.frame.setContentPane(AppTabs.pong);
                AppTabs.frame.setVisible(true);
            } catch (Exception ex) {
                if (ex.getMessage() != null && !ex.getMessage().equals("exit")) {
                    JOptionPane.showMessageDialog(AppTabs.frame, "Incorrect port. Try again",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    this.requestFocusInWindow();
                    AppTabs.frame.setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        buttons.get(1).addActionListener(e -> {
            AppTabs.frame.setContentPane(AppTabs.menu);
            AppTabs.frame.setVisible(true);
        });
    }
}
