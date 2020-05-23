package by.tshmofen.simplepong.domain.geometry;

public class Ball {
    public float x;
    public float y;
    public int diameter;

    public Ball() {
        x = 0;
        y = 0;
        diameter = 0;
    }

    public Ball(float x, float y, int diameter){
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }
}
