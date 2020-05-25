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

import static by.tshmofen.simplepong.domain.Config.*;

public class MultiplayerFieldHandler implements ActionListener {
    private final PongField field;

    ServerSocket server;
    private Socket connection;

    private final boolean isServer;
    private boolean toThrowBallRemote;
    private boolean badConnection;

    ObjectOutputStream out;
    ObjectInputStream in;

    private final Timer timer;

    public MultiplayerFieldHandler(PongField field, JDialog dialog, int port) {
        this.field = field;
        isServer = true;
        toThrowBallRemote = false;
        timer = new Timer(TARGET_PING, this);
        badConnection = true;
        Runnable listening = () -> {
            try {
                server = new ServerSocket(port);
                connection = server.accept();

                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());

                dialog.dispose();
                badConnection = false;
                timer.start();
            } catch (Exception ex) {
                badConnection = true;
            }
        };
        new Thread(listening).start();
    }
    public MultiplayerFieldHandler(PongField field, InetAddress ip, int port) throws Exception {
        this.field = field;
        isServer = false;
        toThrowBallRemote = false;

        badConnection = true;
        connection = new Socket(ip, port);
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());

        badConnection = false;
        timer = new Timer(TARGET_PING, this);
        timer.start();
    }

    public boolean isBadConnection() {
        return badConnection;
    }
    public void throwRemote(){
        toThrowBallRemote = true;
    }
    public void close() {
        try {
            if (server != null) {
                server.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException ignored) { }
        badConnection = true;
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
            badConnection = true;
        }
    }

    private void synchronizeWithServer() throws Exception {
        ClientResponseDTO response = new ClientResponseDTO(field.getPl2());
        if (toThrowBallRemote) {
            response.toThrow = true;
            toThrowBallRemote = false;
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
