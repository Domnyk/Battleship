package network;

import protocol.Msg;
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
    private ArrayBlockingQueue<Msg> messagesReceived, messagesToSent;

    public ConnectionHandler(String address, int port) {
        this.messagesReceived = new ArrayBlockingQueue<Msg>(10);
        this.messagesToSent = new ArrayBlockingQueue<Msg>(10);


        try {
            socket = new Socket(address, port);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            logger.info("Error while creating socket");
        }
    }

    public ArrayBlockingQueue<Msg> getMessagesReceived() {
        return messagesReceived;
    }

    private void send(Msg msgToServer) {
        try {
            toServer.writeObject(msgToServer);
        } catch (IOException e) {
            logger.info("Exception occurred while writing message to server");
        }
        logger.info("Message sent: " + msgToServer.getMsgType());
    }

    public void addMessageToSend(Msg msgToServer) {
        messagesToSent.add(msgToServer);
    }

    @Override
    public void run() {
        this.setName("network.ConnectionHandler");

        try {
            Msg msgFromServer, msgToServer;
            while((msgFromServer = (Msg) fromServer.readObject()) != null ) {
                logger.info("Received message: " + msgFromServer.getMsgType());
                messagesReceived.add(msgFromServer);

                while( (msgToServer = messagesToSent.take()) != null ) {
                    send(msgToServer);
                    break;
                }
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

