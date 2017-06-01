package Network;

import java.io.IOException;
import java.net.Socket;

public class ClientsListener extends Thread {
    private GameServer gameServer;

    public ClientsListener(GameServer gameServer) {
        this.gameServer = gameServer;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
