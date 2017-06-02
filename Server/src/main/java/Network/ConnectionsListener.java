package Network;

import java.io.IOException;
import java.net.Socket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ConnectionsListener extends Thread {
    private GameServer gameServer;
    private int numOfClientsConnected;
    private static final Logger logger = LogManager.getLogger("Server");

    public ConnectionsListener(GameServer gameServer) {
        this.setName("ConnectionsListener");
        this.gameServer = gameServer;
        numOfClientsConnected = 0;

        logger.info("ConnectionsListener object created");
    }

    @Override
    public void run() {
        logger.info("Thread started");
        while(true) {
            Socket clientSocket;
            try {
                clientSocket = gameServer.serverSocket.accept();
                logger.info("Client connected");

                Connection connection = new Connection(clientSocket);
                gameServer.getConnections().add(connection);
                connection.start();
                ++numOfClientsConnected;

                if(numOfClientsConnected == 2) {
                    logger.info("Thread is ending its execution");
                    break;
                }
            } catch (IOException e) {
                logger.error("IOException during client connection");
            }
        }
    }
}
