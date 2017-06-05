package Network;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import Model.GameServerState;
import Protocol.Msg;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class GameServer extends Thread {
        private int port;
        private static final Logger logger = LogManager.getLogger("Server");
        private ConnectionsHandler connectionsHandler;
        private int numOfConnected;

    public GameServer(int portNumber) {
        port = portNumber;
        connectionsHandler = new ConnectionsHandler();
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

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket clientSocket;
            while((clientSocket = serverSocket.accept()) != null ) {
                ConnectionThread connectionThread = new ConnectionThread(numOfConnected, clientSocket, connectionsHandler.getGameMessages());
                connectionThread.start();

                ConcurrentHashMap<Integer, ConnectionThread> connections = connectionsHandler.getConnections();
                if( !connections.contains(numOfConnected) )
                    connections.put(numOfConnected, connectionThread);

                ++numOfConnected;
            }
        }
        catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}
