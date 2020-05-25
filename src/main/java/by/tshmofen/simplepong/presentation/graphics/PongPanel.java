package by.tshmofen.simplepong.presentation.graphics;

import by.tshmofen.simplepong.domain.AppTabs;
import by.tshmofen.simplepong.domain.geometry.Ball;
import by.tshmofen.simplepong.service.MultiplayerFieldHandler;
import by.tshmofen.simplepong.service.PongField;
import static by.tshmofen.simplepong.domain.Config.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;

public class PongPanel extends JPanel implements ActionListener {
    private final int width;
    private final int height;
    private long prevFrameTime;

    private final PongField field;
    private final Timer timer;

    private MultiplayerFieldHandler multiplayer;
    private boolean isMultiplayer;

    public PongPanel() {
        width = AppTabs.frame.getContentPane().getWidth();
        height = AppTabs.frame.getContentPane().getHeight();

        field = new PongField(width, height);

        prevFrameTime = System.currentTimeMillis();
        setDoubleBuffered(true);

        timer = new Timer(MS_PER_FRAME, this);
        isMultiplayer = false;
        addListeners();
    }

    private void addListeners() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!timer.isRunning()) {
                    return;
                }
                if (isMultiplayer && multiplayer.isBadConnection()){
                    stopTheGame();
                    JOptionPane.showMessageDialog(AppTabs.frame
                            , "Bad connection. Returning to the menu.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    AppTabs.frame.setContentPane(AppTabs.menu);
                    AppTabs.frame.setVisible(true);
                }
                if (field.getRemotePlayer() == 1){
                    movePlayer(2);
                }
                else {
                    movePlayer(1);
                }
            }

            private void movePlayer(int player) {
                Point mousePos = PongPanel.super.getMousePosition();
                Rectangle pl = (player == 1) ? field.getPl1() : field.getPl2();
                int shift = pl.height / 2;
                if (mousePos != null && mousePos.y + shift < height && mousePos.y - shift > 0 ) {
                    pl.y = mousePos.y - pl.height / 2;
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mousePressed(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!field.isOnPause()){
                    return;
                }
                if (field.getRemotePlayer() == 1 && field.getLastLoser() == 2) {
                    multiplayer.throwRemote();
                }
                if (field.getRemotePlayer() != 1 && field.getLastLoser() == 1) {
                    throwBall(1);
                }
            }

            public void throwBall(int player) {
                Rectangle pl = (player == 1) ? field.getPl1() : field.getPl2();
                field.throwBall(pl);
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                keyPressed(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    stopTheGame();
                    AppTabs.frame.setContentPane(AppTabs.menu);
                    AppTabs.frame.setVisible(true);
                    return;
                }
                if (!isMultiplayer) {
                    handleKeys(e);
                }
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

    public boolean isBadConnection() {
        return multiplayer.isBadConnection();
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
        g2d.setColor(PONG_LEFT_PART_COLOR);
        g2d.fillRect(0, 0, width, height);

        // right part of field
        g2d.setColor(PONG_RIGHT_PART_COLOR);
        g2d.fillRect(width/2, 0, width, height);

        g2d.setFont(new Font(APP_FONT, Font.BOLD, 75));
        g2d.drawString(String.valueOf(field.getPl2Points()), width/5, height/2);
        g2d.setColor(PONG_LEFT_PART_COLOR);
        g2d.drawString(String.valueOf(field.getPl1Points()), width*7/10, height/2);

        // paint ball
        g2d.setColor(PONG_BALL_COLOR);
        Ball ball = field.getBall();
        g2d.fillOval((int)ball.x, (int)ball.y, ball.diameter, ball.diameter);

        //paint platform
        g2d.setColor(PONG_PLAYER1_COLOR);
        g2d.fill(field.getPl1());
        g2d.setColor(PONG_PLAYER2_COLOR);
        g2d.fill(field.getPl2());

        this.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        prevFrameTime = field.moveBall(prevFrameTime);
        repaint();
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

    public void startTheMultiplayerGame(int port, JDialog dialog) throws Exception {
        multiplayer = new MultiplayerFieldHandler(field, dialog, port);
        field.setRemotePlayer(2);
        isMultiplayer = true;
        startTheGame();
    }

    public void connectTheGame(InetAddress ip, int port) throws Exception {
        multiplayer = new MultiplayerFieldHandler(field, ip, port);
        field.setRemotePlayer(1);
        isMultiplayer = true;
        startTheGame();
    }

    public void stopTheGame() {
        if (!timer.isRunning()){
            return;
        }
        if (isMultiplayer) {
            multiplayer.close();
            isMultiplayer = false;
        }
        timer.stop();
        AppTabs.frame.setCursor(Cursor.getDefaultCursor());
    }
}
