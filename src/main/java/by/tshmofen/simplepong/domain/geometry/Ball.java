package by.tshmofen.simplepong.domain.geometry;

import static by.tshmofen.simplepong.domain.Config.PONG_BALL_DIAMETER;

public class Ball {
    public float x;
    public float y;
    public int diameter;

    public Ball() {
        x = 0;
        y = 0;
        diameter = PONG_BALL_DIAMETER;
    }

    public Ball(float x, float y, int diameter){
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }
}
