package by.tshmofen.simplepong.presentation.graphics;

import by.tshmofen.simplepong.domain.geometry.Ball;
import by.tshmofen.simplepong.domain.geometry.Speed;

import static by.tshmofen.simplepong.domain.Config.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

public class PongPanel extends JPanel implements ActionListener {
    private JFrame frame;
    private int width;
    private int height;

    private Ball ball;
    private Speed speed;

    private Rectangle pl1;
    private Rectangle pl2;

    private long prevFrameTime;

    Timer timer;

    public PongPanel(JFrame frame) {
        this.frame = frame;
        this.width = frame.getContentPane().getWidth();
        this.height = frame.getContentPane().getHeight();

        ball = new Ball((float)width/2, (float)height/2, 15);
        speed = new Speed(START_BALL_SPEED, START_BALL_SPEED);

        pl1 = new Rectangle();
        pl1.width = width / 40;
        pl1.x = width * 19 / 20;
        pl1.height = height / 10;
        pl1.y = height / 2 - pl1.height/2;
        pl2 = new Rectangle();

        prevFrameTime = System.currentTimeMillis();
        setDoubleBuffered(true);

        timer = new Timer(MS_PER_FRAME, this);
    }

    public void startTheGame() {
        if (timer.isRunning()) {
            return;
        }

        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mousePos = PongPanel.super.getMousePosition();
                int shift = pl1.height / 2;
                if (mousePos != null && mousePos.y + shift < height && mousePos.y - shift > 0 ) {
                    pl1.y = mousePos.y - pl1.height / 2;
                }
            }
        });

        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // turn on antialiasing
        Graphics2D g2d = (Graphics2D) g;
        g2d.addRenderingHints(
               new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        );

        // paint background
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, width, height);

        // paint ball
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)ball.x, (int)ball.y, ball.diameter, ball.diameter);

        //paint platform
        g2d.setColor(Color.RED);
        g2d.fill(pl1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveBall();


        repaint();
    }

    private void moveBall() {
        float deltaFrame = (System.currentTimeMillis() - prevFrameTime) / (float)TARGET_MS ;
        prevFrameTime = System.currentTimeMillis();

        float relativeSpeedX = speed.x * deltaFrame ,
                relativeSpeedY = speed.y * deltaFrame;

        float newX = ball.x + relativeSpeedX;
        float newY = ball.y + relativeSpeedY;

        if (newX + ball.diameter > width || newX < 0) {
            newX -= relativeSpeedX;
            speed.x = -speed.x;
        }
        if (newY + ball.diameter > height || newY  < 0) {
            newY -= relativeSpeedY;
            speed.y = -speed.y;
        }

        Line2D ballLine = new Line2D.Float(ball.x, ball.y, newX + ball.diameter, newY + ball.diameter);

        if (ballLine.intersectsLine(pl1.x, pl1.y, pl1.x + pl1.width, pl1.y)
                || ballLine.intersectsLine(pl1.x, pl1.y + pl1.height, pl1.x + width, pl1.y + pl1.height)) {
            newY -= relativeSpeedY;
            speed.y = -speed.y;
        }
        if (ballLine.intersectsLine(pl1.x, pl1.y, pl1.x, pl1.y + pl1.height)
                || ballLine.intersectsLine(pl1.x + pl1.width, pl1.y, pl1.x + width, pl1.y + pl1.height)) {
            newX -= relativeSpeedX;
            speed.x = -speed.x;
        }

        ball.x = newX;
        ball.y = newY;
    }

}
