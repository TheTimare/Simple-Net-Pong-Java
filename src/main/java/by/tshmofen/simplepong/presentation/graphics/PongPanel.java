package by.tshmofen.simplepong.presentation.graphics;

import by.tshmofen.simplepong.domain.AppTabs;
import by.tshmofen.simplepong.domain.geometry.Ball;
import by.tshmofen.simplepong.service.PongField;
import static by.tshmofen.simplepong.domain.Config.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class PongPanel extends JPanel implements ActionListener {
    private final int width;
    private final int height;

    private final PongField field;

    private long prevFrameTime;

    private final Timer timer;

    public PongPanel() {
        width = AppTabs.frame.getContentPane().getWidth();
        height = AppTabs.frame.getContentPane().getHeight();

        field = new PongField(width, height);
        // turn off the cursor

        prevFrameTime = System.currentTimeMillis();
        setDoubleBuffered(true);

        timer = new Timer(MS_PER_FRAME, this);
        addListeners();
    }

    private void addListeners() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mousePos = PongPanel.super.getMousePosition();
                Rectangle pl1 = field.getPl1();
                int shift = pl1.height / 2;
                if (mousePos != null && mousePos.y + shift < height && mousePos.y - shift > 0 ) {
                    pl1.y = mousePos.y - pl1.height / 2;
                }
                if (field.getLastLoser() == 1 && field.isOnPause()) {
                    field.putBallToPlayer(pl1);
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (field.getLastLoser() == 1 && field.isOnPause()) {
                    field.throwBall(field.getPl1());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (field.getLastLoser() == 1 && field.isOnPause()) {
                    field.throwBall(field.getPl1());
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                handleKeys(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                handleKeys(e);
            }

            void handleKeys(KeyEvent e) {
                if (!timer.isRunning()) {
                    return;
                }
                Rectangle pl2 = field.getPl2();
                switch (e.getKeyCode()) {
                    case KEY_PL2_UP:
                        if (pl2.y - KEY_SENSITIVITY > 0) {
                            pl2.y -= KEY_SENSITIVITY;
                        }
                        break;
                    case KEY_PL2_DOWN:
                        if (pl2.y + pl2.height + KEY_SENSITIVITY < height) {
                            pl2.y += KEY_SENSITIVITY;
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        stopTheGame();
                        AppTabs.frame.setContentPane(AppTabs.menu);
                        AppTabs.frame.setVisible(true);
                }
                if (field.getLastLoser() == 2 && field.isOnPause()){
                    field.putBallToPlayer(pl2);
                    if (e.getKeyCode() == KEY_PL2_THROW) {
                        field.throwBall(pl2);
                    }
                }
            }
        });
    }

    public void startTheGame() {
        if (timer.isRunning()) {
            return;
        }

        this.grabFocus();
        AppTabs.frame.setCursor( AppTabs.frame.getToolkit().createCustomCursor(
                new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),
                new Point(),
                null ) );
        timer.start();
        field.reset();
    }

    public void stopTheGame() {
        if (!timer.isRunning()){
            return;
        }

        AppTabs.frame.setCursor(Cursor.getDefaultCursor());
        timer.stop();
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
        g2d.drawString(String.valueOf(field.getPl2Points()), width/5, height/2);
        g2d.setColor(BACKGROUND_COLOR);
        g2d.drawString(String.valueOf(field.getPl1Points()), width*7/10, height/2);

        // paint ball
        g2d.setColor(Color.ORANGE);
        Ball ball = field.getBall();
        g2d.fillOval((int)ball.x, (int)ball.y, ball.diameter, ball.diameter);

        //paint platform
        g2d.setColor(Color.RED);
        g2d.fill(field.getPl1());
        g2d.setColor(Color.BLUE);
        g2d.fill(field.getPl2());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        prevFrameTime = field.moveBall(prevFrameTime);

        repaint();
    }
}
