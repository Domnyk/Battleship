package Network;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Protocol.MsgType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import Protocol.Msg;

public class ConnectionThread extends Thread {
    private int id;
    private ObjectOutputStream toClient;
    private ObjectInputStream fromClient;
    private Socket clientSocket;
    private ArrayBlockingQueue<Msg> gameMessages;

    private static final Logger logger = LogManager.getLogger("Server");

    public ConnectionThread(int id, Socket clientSocket, ArrayBlockingQueue<Msg> gameMessages) {
        this.id = id;
        this.gameMessages = gameMessages;
        this.clientSocket = clientSocket;

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

    Socket getClientSocket() {
        return clientSocket;
    }

    void closeSocket() {
        try {
            clientSocket.close();
        }
        catch (IOException e) {
            logger.info("Client socket closed");
        }
    }

    @Override
    public void run() {
        logger.info("Thread started");

        write(new Msg(MsgType.SET_ID, id));

        try {
            Msg msgFromClient, msgToClient;
            while((msgFromClient = (Msg) fromClient.readObject()) != null) {
                gameMessages.add(msgFromClient);
            }
        }
        catch (Exception e) {
            logger.info("Exception occurred during thread execution");
        }
    }
}
