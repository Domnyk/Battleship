package Network;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import Model.GameServerState;
import Protocol.Msg;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class GameServer extends Thread {
        private int port;
        private static final Logger logger = LogManager.getLogger("Server");
        private ServerSocket serverSocket;
        private ConcurrentLinkedQueue<Msg> gameMessages;
        private ConcurrentLinkedQueue<ConnectionThread> connections;
        private GameServerState gameServerState;

    public GameServerState getGameServerState() {
        return gameServerState;
    }

    public void setGameServerState(GameServerState gameServerState) {
        this.gameServerState = gameServerState;
    }

    public GameServer(int portNumber) {
        this.port = portNumber;
        gameMessages = new ConcurrentLinkedQueue<Msg>();
        connections = new ConcurrentLinkedQueue<ConnectionThread>();
        gameServerState = GameServerState.INIT_STATE;

        logger.info("GameServer object created");
    }

    public void start() {
        this.setName("GameServer");
        this.startConnectionsListener();
        this.startServer();
    }

    public void startConnectionsListener() {
        new ConnectionsHandler(gameMessages, connections, gameServerState).start();
    }

    public void startServer() {
        // Klasa do zarzadzania wiadomosciami
        logger.info("Server listens on port: " + port);


        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket clientSocket;
            this.serverSocket  = serverSocket;
            while((clientSocket = serverSocket.accept()) != null ) {
                ConnectionThread connectionThread = new ConnectionThread(clientSocket, gameMessages);
                connectionThread.start();
                connections.add(connectionThread);
            }
        }
        catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}
