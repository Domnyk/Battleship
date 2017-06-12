package Network;

import Protocol.Msg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionHandler extends Thread {
    private static final Logger logger = LogManager.getLogger("Client");
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private ArrayBlockingQueue<Msg> gameMessages;

    public ConnectionHandler(String address, int port, ArrayBlockingQueue<Msg> gameMessages) {
        this.gameMessages = gameMessages;

        try {
            socket = new Socket(address, port);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            logger.info("Error while creating socket");
        }
    }

    public void send(Msg msgToServer) {
        try {
            toServer.writeObject(msgToServer);
        } catch (IOException e) {
            logger.info("Exception occurred while writing message to server");
        }
        logger.info("Message sent: " + msgToServer.getMsgType());
    }

    @Override
    public void run() {
        this.setName("Network.ConnectionHandler");

        try {
            Msg msgFromServer;
            while((msgFromServer = (Msg) fromServer.readObject()) != null ) {
                logger.info("Received message: " + msgFromServer.getMsgType());
                gameMessages.add(msgFromServer);

            }
        }
        catch (Exception e) {
            logger.warn("IOException from readObject()");
        }
    }

    public void closeSocket() {
        try {
            socket.close();
        }
        catch (Exception e) {
            logger.info("Socket was closed");
        }
    }
}

