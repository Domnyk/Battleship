import Network.GameServer;

/**
 * Entry point for server applications
 */
public class Main {
    /**
     * Initiates server
     *
     * @param args  First argument is number of the port to be used by server
     */
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);

        GameServer gameServer = new GameServer(portNumber);
        gameServer.start();
    }
}
