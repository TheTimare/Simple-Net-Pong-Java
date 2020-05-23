package by.tshmofen.simplepong.presentation.window;

import by.tshmofen.simplepong.presentation.graphics.PongPanel;
import static by.tshmofen.simplepong.domain.Config.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainPongFrame {
    JFrame frame;

    public MainPongFrame() {
        frame = new JFrame("Simple Net Pong");
    }

    public static void main(String[] args) {
        MainPongFrame mainFrame = new MainPongFrame();
        mainFrame.start();
    }

    public void start() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); // forbid resizing
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null); // set to the center of screen
        // turn off the cursor
        frame.setCursor( frame.getToolkit().createCustomCursor(
                new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),
                new Point(),
                null ) );

        frame.setVisible(true);
        PongPanel pong = new PongPanel(frame);
        frame.add(pong);


        pong.startTheGame();
    }
}
