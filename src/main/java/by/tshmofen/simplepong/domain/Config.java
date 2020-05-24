package by.tshmofen.simplepong.domain;

import java.awt.*;
import java.awt.event.KeyEvent;

public final class Config {
    /* TECHNICAL OPTIONS */
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;

    // 50 for 20fps, 33 for ~30fps, 16 for ~60fps
    public static final int MS_PER_FRAME = 16;
    public static final int TARGET_MS = 33; // calculate speed for this value

    /* GAME OPTIONS*/
    public static final float START_BALL_SPEED = 5;
    public static final float SPEED_INCREMENT = 2;

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
