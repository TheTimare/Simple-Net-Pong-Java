package by.tshmofen.simplepong.domain.geometry;

import static by.tshmofen.simplepong.domain.Config.PONG_START_BALL_SPEED;

public class Speed {
    public float x;
    public float y;

    public Speed() {
        x = PONG_START_BALL_SPEED;
        y = PONG_START_BALL_SPEED;
    }

    public Speed(float x, float y){
        this.x = x;
        this.y = y;
    }
}
