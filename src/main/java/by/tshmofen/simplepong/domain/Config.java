package by.tshmofen.simplepong.domain;

import java.awt.*;
import java.awt.event.KeyEvent;

public final class Config {
    /* VIEW OPTIONS */
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String APP_FONT = "Courier";

    public static final Color MENU_BACKGROUND_COLOR = Color.DARK_GRAY;
    public static final Color MENU_BUTTON_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    public static final Color MENU_BUTTON_FOREGROUND_COLOR = Color.DARK_GRAY;
    public static final int MENU_WIDTH = 300;

    public static final Color PONG_LEFT_PART_COLOR = Color.DARK_GRAY;
    public static final Color PONG_RIGHT_PART_COLOR = Color.decode("#F7F7F7");
    public static final Color PONG_BALL_COLOR = Color.ORANGE;
    public static final Color PONG_PLAYER1_COLOR = Color.RED;
    public static final Color PONG_PLAYER2_COLOR = Color.BLUE;

    /*UPDATE OPTIONS*/
    public static final int MS_PER_FRAME = 16; // 50 for 20fps, 33 for ~30fps, 16 for ~60fps
    public static final int TARGET_MS = 33; // calculate speed for this value
    public static final int TARGET_PING = 10;

    /* GAME OPTIONS*/
    public static final float PONG_START_BALL_SPEED = 5;
    public static final float PONG_SPEED_INCREMENT = 2;
    public static final int PONG_BALL_DIAMETER = 10;

    /* KEYS OPTIONS */
    public static final int KEY_SENSITIVITY = 25;
    public static final int KEY_PL2_UP = KeyEvent.VK_W;
    public static final int KEY_PL2_DOWN = KeyEvent.VK_S;
    public static final int KEY_PL2_THROW = KeyEvent.VK_E;

    /* CONSTRUCTOR CALL IS FORBIDDEN */
    public Config() {
        throw new IllegalArgumentException();
    }
}
