package by.tshmofen.simplepong.domain.dto;

import by.tshmofen.simplepong.service.PongField;

import java.awt.*;
import java.io.Serializable;
import java.rmi.Remote;

public class PongFieldDTO implements Serializable {
    public float ballX;
    public float ballY;

    public float speedX;
    public float speedY;

    public int remotePlayerX;
    public int remotePlayerY;

    public int pl1Points;
    public int pl2Points;

    public boolean onPause;
    public int lastLoser;

    public PongFieldDTO(PongField field) {
        ballX = field.getBall().x;
        ballY = field.getBall().y;

        speedX = field.getSpeed().x;
        speedY = field.getSpeed().y;

        Rectangle pl = (field.getRemotePlayer() == 1) ? field.getPl2() : field.getPl1();
        remotePlayerX = pl.x;
        remotePlayerY = pl.y;

        pl1Points = field.getPl1Points();
        pl2Points = field.getPl2Points();

        onPause = field.isOnPause();
        lastLoser = field.getLastLoser();
    }
}
