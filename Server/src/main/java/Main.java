import Network.GameServer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Entry point for server applications
 */
public class Main {
    /**
     * Initiates server
     *
     * @param args  First argument is number of the port to be used by server
     */
    private static final Logger logger = LogManager.getLogger("Server");

    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);

        GameServer gameServer = new GameServer(portNumber);
        gameServer.start();
    }
}
