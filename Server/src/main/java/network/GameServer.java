package network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Thread {
    private int port;
    private static final Logger logger = LogManager.getLogger("Server");
    private ConnectionsHandler connectionsHandler;
    private int numOfConnected;
    private ServerSocket serverSocket;

    public GameServer(int portNumber) {
        port = portNumber;
        connectionsHandler = new ConnectionsHandler();

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.warn("Exception during server creation");
        }
    }

    @Override
    public void run() {
        this.setName("GameServer Thread");
        logger.info("Thread is up");

        connectionsHandler.start();
        startServer();
    }


    public void startServer() {
        logger.info("Server listens on port: " + port);

        try {
            Socket clientSocket;
            while ((clientSocket = serverSocket.accept()) != null) {
                ConnectionThread connectionThread = new ConnectionThread(numOfConnected, clientSocket, connectionsHandler.getGameMessages());
                connectionThread.start();

                connectionsHandler.addConnection(numOfConnected, connectionThread);
                ++numOfConnected;
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public void closeServer() {
        try {
            connectionsHandler.stopConnectionsThreads();
            connectionsHandler.interrupt();
            serverSocket.close();
        } catch (IOException e) {
            logger.info("Server closed");
        }
    }
}
