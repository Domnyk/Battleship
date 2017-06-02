package Network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class GameServer {
    private int port;
    private static final Logger logger = LogManager.getLogger("Server");
    public ServerSocket serverSocket;
    private ArrayList<Connection> connections;
    private MessagesListener messagesListener;
    private ConnectionsListener connectionsListener;


    public GameServer(int portNumber) {
        this.port = portNumber;
        logger.info("GameServer object created");
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Socket server has started");
        }
        catch (IOException e) {
            logger.fatal("IOException during server socket creation");
        }

        connections = new ArrayList<Connection>();
        messagesListener = new MessagesListener();
        // Rozpocznij watek messagesListener

        connectionsListener = new ConnectionsListener(this);
        connectionsListener.start();
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
