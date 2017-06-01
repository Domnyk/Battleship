package Network;

import java.io.IOException;
import java.net.Socket;

public class ClientsListener extends Thread {
    private GameServer gameServer;
    private int numOfClientsConnected;


    public ClientsListener(GameServer gameServer) {
        this.gameServer = gameServer;
        numOfClientsConnected = 0;
    }

    @Override
    public void run() {
        while(true) {
            Socket clientSocket;
            try {
                clientSocket = gameServer.serverSocket.accept();
                System.out.println("Client has connected");

                Connection connection = new Connection(clientSocket);
                gameServer.getConnections().add(connection);
                connection.start();
                ++numOfClientsConnected;

                if(numOfClientsConnected == 2) {
                    System.out.println("ClientListener thread is ending its execution");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
