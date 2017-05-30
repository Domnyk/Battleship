package Network;

import java.io.IOException;
import java.net.*;

public class Server extends Thread {
    private int portNumber;
    private  boolean isRunning;
    private ServerSocket serverSocket;

    Server(int portNumber) {
        this.portNumber = portNumber;

        try {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        isRunning = true;
        while (true) {
            // New Thread
            // Print to thread input
        }

    }
}
