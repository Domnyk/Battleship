package Network;

import Model.GameServerState;
import Protocol.Msg;
import Protocol.MsgType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

public final class ConnectionsHandler extends Thread {
    private ConcurrentLinkedQueue<Msg> gameMessages;
    private ConcurrentLinkedQueue<ConnectionThread> connections;
    private GameServerState gameServerState;

    private static final Logger logger = LogManager.getLogger("Server");

    public ConnectionsHandler(ConcurrentLinkedQueue<Msg> gameMessages,
                              ConcurrentLinkedQueue<ConnectionThread> connections,
                              GameServerState gameServerState) {
        this.setName("ConnectionsHandler");

        this.gameMessages = gameMessages;
        this.connections = connections;
        this.gameServerState = gameServerState;
    }

    public ConcurrentLinkedQueue<ConnectionThread> getConnections() {
        return connections;
    }

    public void setConnections(ConcurrentLinkedQueue<ConnectionThread> connections) {
        this.connections = connections;
    }

    public void handleMessage(Msg clientMsg) {
        switch (clientMsg.getMsgType()) {
            case ESTABLISH_CONNECTION:
                handle_establish_connection(clientMsg);
                break;

            case SHIPS_PLACED:
                handle_ships_placed(clientMsg);
                break;

            case SHOT_PERFORMED:
                handle_shot_performed(clientMsg);
                break;

        }
    }

    private void send(Msg answer, int connectionId) {
        for (ConnectionThread connectionThread: connections) {
            if( connectionThread.getId() == connectionId )
                connectionThread.write(answer);
        }
    }

    private void sendBroadcast(Msg answer) {
        for (ConnectionThread connectionThread: connections) {
            answer.setPlayerID((int) connectionThread.getId());
            connectionThread.write(answer);
        }
    }

    private void handle_establish_connection(Msg clientMsg) {
        Msg answer = new Msg();

        if( gameServerState == GameServerState.INIT_STATE ) {
            logger.info("First player connected");

            gameServerState = GameServerState.WAIT_FOR_SECOND_PLAYER;
            answer.setMsgType(MsgType.WAIT_FOR_SECOND_PLAYER);
            answer.setPlayerID((int)this.getId());
            send(answer, clientMsg.getPlayerID());
        }
        else {
            logger.info("Second player connected");

            gameServerState = GameServerState.WAIT_FOR_FIRST_READY;
            answer.setMsgType(MsgType.PLACE_SHIPS);
            sendBroadcast(answer);
        }
        gameMessages.poll();
    }

    private void handle_shot_performed(Msg clientMsg) {
        if( /* porazka lub wygrana */ )
        {}
        else
        {
            // Wyslij uaktualnie plansz
            // Wyslij kto teraz ma ruch
        }
    }

    private void handle_ships_placed(Msg clientMsg) {
        Msg answerFirst = new Msg();
        Msg answerSecond = new Msg();

        if( gameServerState == GameServerState.WAIT_FOR_FIRST_READY ) {
            logger.info("First player finished placing his ships");
            // Zaktualizuj polozenie statkow po stronie serwera

            gameServerState = GameServerState.WAIT_FOR_SECOND_READY;
        }
        else {
            logger.info("Second player finished placing his ships");
            gameServerState = GameServerState.WAIT_FOR_MOVE;

            answerFirst.setMsgType(MsgType.MAKE_MOVE);
            answerSecond.setMsgType(MsgType.WAIT_FOR_MOVE);

            answerFirst.setPlayerID(clientMsg.getPlayerID());

            // UGLY - ConcurrentHashMap would be better
            int secondId = 0;
            for (ConnectionThread connectionThread: connections)
            {
                if( clientMsg.getPlayerID() != connectionThread.getId() ) {
                    secondId = (int) connectionThread.getId();
                }
            }
            answerSecond.setPlayerID(secondId);


            send(answerFirst, answerFirst.getPlayerID());
            send(answerSecond, secondId);
        }
        gameMessages.poll();
    }


    @Override
    public void run() {
        logger.info("Thread started");

        while(true) {
            for (Msg msg: gameMessages) {
                logger.info("Received " + msg.getMsgType() + "from player with ID " + msg.getPlayerID());
                handleMessage(msg);
                gameMessages.poll();
            }
        }
    }
}
