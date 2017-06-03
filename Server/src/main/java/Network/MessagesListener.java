package Network;

import Protocol.Msg;
import Protocol.MsgType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MessagesListener extends Thread {
    private static final Logger logger = LogManager.getLogger("Server");

    private GameServer gameServer;

    public MessagesListener(GameServer gameServer) {
        this.setName("MessagesListener");
        this.gameServer = gameServer;

        logger.info("MessagesListener object created");
    }

    @Override
    public void run() {
        logger.info("Thread started");


    }
}
