package Network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class GameServer {
    private int port;
    public ServerSocket serverSocket;
    private ArrayList<Connection> connections;
    private ConnectionsListener connectionsListener;
    private ClientsListener clientListener;

    public GameServer(int portNumber) {
        this.port = portNumber;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        connections = new ArrayList<Connection>();
        connectionsListener = new ConnectionsListener();
        // Rozpocznij watek connectionsListener

        clientListener = new ClientsListener(this);
        clientListener.start();
    }

    public void stop() {
        connectionsListener.interrupt();

        try {
            for (Connection connection : connections) {
                // Zamknij socket
                // Przerwij watek connection
            }
            connections.clear();
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }
}
