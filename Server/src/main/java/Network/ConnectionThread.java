package Network;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import Protocol.MsgType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import Protocol.Msg;

public class ConnectionThread extends Thread {
    private ObjectOutputStream toClient;
    private ObjectInputStream fromClient;
    private ConcurrentLinkedQueue<Msg> gameMessages;

    private static final Logger logger = LogManager.getLogger("Server");

    public ConnectionThread(Socket clientSocket, ConcurrentLinkedQueue<Msg> gameMessages) {
        //this.clientSocket = clientSocket;
        this.gameMessages = gameMessages;

        try {
            toClient = new ObjectOutputStream(clientSocket.getOutputStream());
            fromClient = new ObjectInputStream(clientSocket.getInputStream());
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void write(Msg msg) {
        try {
            toClient.writeObject(msg);
        }
        catch (IOException e) {
            logger.error("Exception during writing to client stream");
        }
    }

    @Override
    public void run() {
        logger.info("Thread started");

        write(new Msg(MsgType.SET_ID, (int)this.getId()));

        try {
            Msg msgFromClient, msgToClient;
            while((msgFromClient = (Msg) fromClient.readObject()) != null) {
                logger.info("Message from client has been received");
                gameMessages.add(msgFromClient);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
