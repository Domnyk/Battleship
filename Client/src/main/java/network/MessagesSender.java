package network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.Msg;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class MessagesSender extends Thread {
    private static final Logger logger = LogManager.getLogger("Client");
    private ArrayBlockingQueue<Msg> messagesToSend;
    private ObjectOutputStream toServer;

    MessagesSender(ObjectOutputStream toServer) {
        this.messagesToSend = new ArrayBlockingQueue<Msg>(10);
        this.toServer = toServer;
    }

    public ArrayBlockingQueue<Msg> getMessagesToSend() {
        return messagesToSend;
    }

    private void send(Msg msgToServer) {
        try {
            toServer.writeObject(msgToServer);
            logger.info("Message sent: " + msgToServer.getMsgType());
        } catch (IOException e) {
            logger.info("Exception occurred while writing message to server");
        }
    }

    @Override
    public void run() {
        this.setName("MessagesSender");

        Msg msg;
        try {
            while ((msg = messagesToSend.take()) != null) {
                send(msg);
            }
        } catch (InterruptedException e) {
            logger.info("Thread interrupted");
        }
    }
}
