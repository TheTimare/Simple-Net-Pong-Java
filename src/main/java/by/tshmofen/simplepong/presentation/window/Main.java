package by.tshmofen.simplepong.presentation.window;

import by.tshmofen.simplepong.domain.AppTabs;
import by.tshmofen.simplepong.presentation.graphics.MenuPanel;
import by.tshmofen.simplepong.presentation.graphics.PongPanel;
import static by.tshmofen.simplepong.domain.Config.*;

import javax.swing.*;

public class Main {
    JFrame frame;

    public Main() {
        frame = new JFrame("Simple Net Pong");
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); // forbid resizing
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null); // set to the center of screen

        frame.setVisible(true);

        AppTabs.frame = frame;
        AppTabs.pong = new PongPanel();
        AppTabs.menu = new MenuPanel();
        frame.setContentPane(AppTabs.menu);
    }
}
