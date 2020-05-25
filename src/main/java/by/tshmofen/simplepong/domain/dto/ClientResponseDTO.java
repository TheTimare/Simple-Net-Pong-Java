package by.tshmofen.simplepong.domain.dto;

import java.awt.*;
import java.io.Serializable;

public class ClientResponseDTO implements Serializable {
    public int x;
    public int y;
    public boolean toThrow;

    public ClientResponseDTO(Rectangle player) {
        x = player.x;
        y = player.y;
        toThrow = false;
    }

}
