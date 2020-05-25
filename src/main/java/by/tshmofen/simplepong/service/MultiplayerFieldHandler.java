package by.tshmofen.simplepong.service;

import by.tshmofen.simplepong.domain.dto.ClientResponseDTO;
import by.tshmofen.simplepong.domain.dto.PongFieldDTO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplayerFieldHandler implements ActionListener {
    private final PongField field;
    private final Socket connection;
    private final boolean isServer;
    private boolean toThrowRemote;

    ObjectOutputStream out;
    ObjectInputStream in;

    private final Timer timer;

    public MultiplayerFieldHandler(PongField field, int port) throws Exception {
        this.field = field;
        isServer = true;
        toThrowRemote = false;

        ServerSocket server = new ServerSocket(port);
        connection = server.accept();

        timer = new Timer(20, this);
        timer.start();

        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }
    public MultiplayerFieldHandler(PongField field, InetAddress ip, int port) throws Exception {
        this.field = field;
        isServer = false;
        toThrowRemote = false;

        connection = new Socket(ip, port);
        timer = new Timer(20, this);
        timer.start();

        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }

    public Timer getTimer() {
        return timer;
    }

    public void throwRemote(){
        toThrowRemote = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (isServer) {
                synchronizeWithClient();
            } else {
                synchronizeWithServer();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            timer.stop();
            try {
                out.close();
                in.close();
            } catch (IOException ignored) { }
        }
    }

    private void synchronizeWithServer() throws Exception {
        ClientResponseDTO response = new ClientResponseDTO(field.getPl2());
        if (toThrowRemote) {
            response.toThrow = true;
            toThrowRemote = false;
            System.out.println("it works");
        }
        out.writeObject(response);
        out.flush();

        PongFieldDTO data = (PongFieldDTO) in.readObject();
        field.update(data);
    }

    private void synchronizeWithClient() throws Exception {
        ClientResponseDTO response = (ClientResponseDTO)in.readObject();
        field.updateRemotePlayer(response);

        PongFieldDTO data = new PongFieldDTO(field);
        out.writeObject(data);
        out.flush();
    }
}
