package by.tshmofen.simplepong.presentation.graphics;

import by.tshmofen.simplepong.domain.geometry.Ball;
import by.tshmofen.simplepong.domain.geometry.Speed;

import static by.tshmofen.simplepong.domain.Config.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class PongPanel extends JPanel implements ActionListener {
    private JFrame frame;
    private int width;
    private int height;

    private Ball ball;
    private Speed speed;

    private Rectangle pl1;
    private Rectangle pl2;

    private int pl1Points;
    private int pl2Points;

    private long prevFrameTime;

    Timer timer;

    public PongPanel(JFrame frame) {
        this.frame = frame;
        this.width = frame.getContentPane().getWidth();
        this.height = frame.getContentPane().getHeight();

        ball = new Ball((float)width/2, (float)height/2, 15);
        speed = new Speed(START_BALL_SPEED, START_BALL_SPEED);

        pl1 = new Rectangle();
        pl1.width = width / 80;
        pl1.x = width * 19 / 20;
        pl1.height = height / 10;
        pl1.y = height / 2 - pl1.height/2;

        pl2 = new Rectangle();
        pl2.width = width / 80;
        pl2.x = width / 20;
        pl2.height = height / 10;
        pl2.y = height / 2 - pl1.height/2;

        pl1Points = 0;
        pl2Points = 0;

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
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                moveFigures(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                moveFigures(e);
            }

            void moveFigures(KeyEvent e) {
                int shift1 = pl1.height / 2;
                int shift2 = pl2.height / 2;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (pl1.y - shift1 - KEY_SENSITIVITY > 0) {
                            pl1.y -= KEY_SENSITIVITY;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (pl1.y + shift1 + KEY_SENSITIVITY < height) {
                            pl1.y += KEY_SENSITIVITY;
                        }
                        break;
                    case KeyEvent.VK_W:
                        if (pl2.y - shift2 - KEY_SENSITIVITY > 0) {
                            pl2.y -= KEY_SENSITIVITY;
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (pl2.y + shift2 + KEY_SENSITIVITY < height) {
                            pl2.y += KEY_SENSITIVITY;
                        }
                        break;
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

        // paint info and layout
        g2d.setColor(Color.decode("#F7F7F7"));
        g2d.fillRect(width/2, 0, width, height);

        g2d.setFont(new Font("Courier", Font.BOLD, 75));
        g2d.drawString(String.valueOf(pl2Points), width/5, height/2);
        g2d.setColor(BACKGROUND_COLOR);
        g2d.drawString(String.valueOf(pl1Points), width*7/10, height/2);

        // paint ball
        g2d.setColor(Color.ORANGE);
        g2d.fillOval((int)ball.x, (int)ball.y, ball.diameter, ball.diameter);

        //paint platform
        g2d.setColor(Color.RED);
        g2d.fill(pl1);
        g2d.setColor(Color.BLUE);
        g2d.fill(pl2);
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

        if (newX + ball.diameter > width) {
            pl2Points++;
            ball.x = width / 2f;
            ball.y = height / 2f;
            speed.x = speed.y = START_BALL_SPEED;
            return;
        }
        if (newX < 0) {
            pl1Points++;
            ball.x = width / 2f;
            ball.y = height / 2f;
            speed.x = speed.y = START_BALL_SPEED;
            return;
        }

        if (newY + ball.diameter > height || newY  < 0) {
            newY -= relativeSpeedY;
            speed.y = -speed.y;
        }

        Rectangle ballRect = new Rectangle((int)newX, (int)newY, ball.diameter, ball.diameter);
        if (ballRect.intersects(pl1) || ballRect.intersects(pl2)) {
            newX -= relativeSpeedX;
            speed.x = -speed.x;
            speed.x = (speed.x < 0) ? speed.x - 0.5f : speed.x + 0.5f;
        }

        ball.x = newX;
        ball.y = newY;
    }

}
