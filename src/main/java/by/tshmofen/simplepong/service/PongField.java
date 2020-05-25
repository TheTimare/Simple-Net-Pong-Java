package by.tshmofen.simplepong.service;

import by.tshmofen.simplepong.domain.dto.ClientResponseDTO;
import by.tshmofen.simplepong.domain.dto.PongFieldDTO;
import by.tshmofen.simplepong.domain.geometry.Ball;
import by.tshmofen.simplepong.domain.geometry.Speed;
import static by.tshmofen.simplepong.domain.Config.*;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Random;

public class PongField {
    private final int width;
    private final int height;

    private final Ball ball;
    private final Speed speed;

    private final Rectangle pl1;
    private final Rectangle pl2;

    private int pl1Points;
    private int pl2Points;

    private boolean onPause;
    private int lastLoser;
    private int remotePlayer;

    public PongField(int width, int height) {
        this.width = width;
        this.height = height;

        ball = new Ball(width/2f, height/2f, PONG_BALL_DIAMETER);
        speed = new Speed(PONG_START_BALL_SPEED, PONG_START_BALL_SPEED);

        pl1 = new Rectangle();
        pl1.width = width / 80;
        pl1.height = height / 10;

        pl2 = new Rectangle();
        pl2.width = width / 80;
        pl2.height = height / 10;

        remotePlayer = 0;
        reset();
        onPause = true;
    }

    public Ball getBall() {
        return ball;
    }
    public Speed getSpeed() {
        return speed;
    }
    public Rectangle getPl1() {
        return pl1;
    }
    public Rectangle getPl2() {
        return pl2;
    }

    public int getPl1Points() {
        return pl1Points;
    }
    public int getPl2Points() {
        return pl2Points;
    }
    public int getLastLoser() {
        return lastLoser;
    }
    public int getRemotePlayer() {
        return remotePlayer;
    }

    public boolean isOnPause(){
        return onPause;
    }
    public void pause() {
        onPause = true;
    }
    public void unpause() {
        onPause = false;
    }
    public void setRemotePlayer(int remotePlayer) {
        this.remotePlayer = remotePlayer;
    }

    public void reset() {
        pl1.x = width * 19 / 20;
        pl1.y = height / 2 - pl1.height/2;
        pl2.x = width / 20;
        pl2.y = height / 2 - pl1.height/2;

        pl1Points = 0;
        pl2Points = 0;
        lastLoser = 1;

        ball.x = width/2f;
        ball.y = height/2f;

        speed.x = PONG_START_BALL_SPEED;
        speed.y = PONG_START_BALL_SPEED;

        pause();
    }

    public void update(PongFieldDTO data) {
        ball.x = data.ballX;
        ball.y = data.ballY;

        speed.x = data.speedX;
        speed.y = data.speedY;

        if (remotePlayer == 1) {
            pl1.x = data.remotePlayerX;
            pl1.y = data.remotePlayerY;
        }
        else {
            pl2.x = data.remotePlayerX;
            pl2.y = data.remotePlayerY;
        }

        pl1Points = data.pl1Points;
        pl2Points = data.pl2Points;

        onPause = data.onPause;
        lastLoser = data.lastLoser;
    }
    public void updateRemotePlayer(ClientResponseDTO response) {
        Rectangle pl = (remotePlayer == 1) ? pl1 : pl2;
        pl.x = response.x;
        pl.y = response.y;
        if (response.toThrow){
            throwBall(pl);
        }
    }

    // return time of the last frame
    public long moveBall(long prevFrameTime) {
        float deltaFrame = (System.currentTimeMillis() - prevFrameTime) / (float)TARGET_MS ;
        prevFrameTime = System.currentTimeMillis();

        if (onPause) { // wait before player throw the ball
            Rectangle pl = (lastLoser == 1) ? pl1 : pl2;
            putBallToPlayer(pl);
            return prevFrameTime;
        }

        float relativeSpeedX = speed.x * deltaFrame ,
                relativeSpeedY = speed.y * deltaFrame;

        float newX = ball.x + relativeSpeedX;
        float newY = ball.y + relativeSpeedY;

        if (newX + ball.diameter > width) {
            pl2Points++;
            lastLoser = 1;
            putBallToPlayer(pl1);
            pause();
            return prevFrameTime;
        }
        if (newX < 0) {
            pl1Points++;
            lastLoser = 2;
            putBallToPlayer(pl2);
            pause();
            return prevFrameTime;
        }

        if (newY + ball.diameter > height || newY  < 0) {
            newY -= relativeSpeedY;
            speed.y = -speed.y;
        }

        if (isNewBallIntersectPlayers(newX, newY)) {
            newX -= relativeSpeedX;
            speed.x = -speed.x;
            speed.x = (speed.x < 0) ? speed.x - PONG_SPEED_INCREMENT : speed.x + PONG_SPEED_INCREMENT;
        }

        ball.x = newX;
        ball.y = newY;

        return prevFrameTime;
    }

    private boolean isNewBallIntersectPlayers(float newX, float newY) {
        Line2D ballLineCenter = formBallLine(ball.x, ball.y, newX, newY);
        if (ballLineCenter.intersects(pl1) || ballLineCenter.intersects(pl2)) {
            return true;
        }

        Line2D ballLineDown = formBallLine(ball.x, ball.y - ball.diameter/2f
                , newX, newY - ball.diameter/2f);
        if (ballLineDown.intersects(pl1) || ballLineDown.intersects(pl2)) {
            return true;
        }

        Line2D ballLineTop = formBallLine(ball.x, ball.y + ball.diameter/2f
                , newX, newY + ball.diameter/2f);
        return ballLineTop.intersects(pl1) || ballLineTop.intersects(pl2);
    }

    private Line2D formBallLine(float x, float y, float newX, float newY) {
        if (x < newX) {
            newX += ball.diameter;
        }
        if (y < newY) {
            newY += ball.diameter;
        }
        return new Line2D.Float(x, y, newX, newY);
    }

    public void throwBall(Rectangle pl) {
        putBallToPlayer(pl);

        speed.x = (pl.x < width / 2) ? PONG_START_BALL_SPEED : -PONG_START_BALL_SPEED;
        speed.y = new Random().nextInt() % (PONG_START_BALL_SPEED) - PONG_START_BALL_SPEED / 2;
        speed.y = (speed.y == 0) ? speed.y + 1 : speed.y;

        unpause();
    }

    public void putBallToPlayer(Rectangle pl) {
        if (ball.x > width / 2f){
            ball.x = pl.x - ball.diameter - 1;
        }
        else {
            ball.x = pl.x + pl.width + 1;
        }

        ball.y = pl.y + pl.height/2f - ball.diameter/2f;
    }
}
