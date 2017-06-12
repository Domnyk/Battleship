package network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.Msg;

import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class MessagesReceiver extends Thread {
    private static final Logger logger = LogManager.getLogger("Client");
    private ArrayBlockingQueue<Msg> messagesReceived;
    private ObjectInputStream fromServer;

    MessagesReceiver(ObjectInputStream fromServer) {
        this.messagesReceived = new ArrayBlockingQueue<>(10);
        this.fromServer = fromServer;
    }

    public ArrayBlockingQueue<Msg> getMessagesReceived() {
        return messagesReceived;
    }

    @Override
    public void run() {
        this.setName("MessagesReceiver");

        Msg msg;
        try {
            while( (msg = (Msg) fromServer.readObject()) != null ) {
                messagesReceived.add(msg);
            }
        }
        catch (Exception e) {
                logger.info("Exception in messages receiver");
        }
    }
}
