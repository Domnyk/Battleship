package network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.Msg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionHandler {
    private static final Logger logger = LogManager.getLogger("Client");
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private MessagesReceiver messagesReceiver;
    private MessagesSender messagesSender;

    public ConnectionHandler(String address, int port) {
        try {
            socket = new Socket(address, port);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.info("Error while creating socket");
        }

        messagesReceiver = new MessagesReceiver(fromServer);
        messagesSender = new MessagesSender(toServer);

        messagesReceiver.start();
        messagesSender.start();
    }

    public ArrayBlockingQueue<Msg> getMessagesReceived() {
        return messagesReceiver.getMessagesReceived();
    }

    public ArrayBlockingQueue<Msg> getMessagesToSend() {
        return messagesSender.getMessagesToSend();
    }

    public void closeConnection() {
        if (messagesReceiver.isAlive()) {
            messagesReceiver.interrupt();
        }

        if (messagesSender.isAlive()) {
            messagesSender.interrupt();
        }

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            logger.info("Socket closed");
        }
    }
}

