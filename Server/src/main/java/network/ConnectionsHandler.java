package network;

import model.Coordinates;
import model.FieldState;
import model.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.Msg;
import protocol.MsgType;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public final class ConnectionsHandler extends Thread {
    private ArrayBlockingQueue<Msg> gameMessages;
    private ConcurrentHashMap<Integer, ConnectionThread> connections;
    private GameServerState gameServerState;
    private HashMap<Integer, Map> playersMaps;
    private static final Logger logger = LogManager.getLogger("Server");

    ArrayBlockingQueue<Msg> getGameMessages() {
        return gameMessages;
    }

    ConnectionsHandler() {
        this.setName("ConnectionsHandler");

        this.gameMessages = new ArrayBlockingQueue<Msg>(10);
        this.connections = new ConcurrentHashMap<>();
        this.gameServerState = GameServerState.INIT_STATE;
        this.playersMaps = new HashMap<>();
    }


    void addConnection(int id, ConnectionThread connectionThread) {
        if (!connections.contains(id))
            connections.put(id, connectionThread);
    }

    void stopConnectionsThreads() {
        connections.forEach((id, connection) -> {
            connection.closeSocket();
            connection.interrupt();
        });
    }

    @Override
    public void run() {
        logger.info("Thread started");

        try {
            Msg msg;
            while ((msg = gameMessages.take()) != null) {
                logger.info("Received " + msg.getMsgType() + " from Player with ID " + msg.getPlayerID());
                handleMessage(msg);
                gameMessages.poll();
            }
        } catch (InterruptedException e) {
            logger.info("Thread was interrupted while waiting for message to appear in blocking queue");
        }
    }

    /**
     * Handles message received from server
     *
     * @param clientMsg WAITING message doesn't need to be handled. It is here to completeness of all messages received by the server
     */
    public void handleMessage(Msg clientMsg) {
        switch (clientMsg.getMsgType()) {
            case ID_IS_SET:
                handle_id_is_set(clientMsg);
                break;

            case SHIPS_PLACED:
                handle_ships_placed(clientMsg);
                break;

            case WAITING:
                break;

            case SHOT_PERFORMED:
                handle_shot_performed(clientMsg);
                break;
        }
    }

    private void send(Msg answer) {
        logger.info("Sending " + answer.getMsgType() + " to player with ID: " + answer.getPlayerID());
        connections.get(answer.getPlayerID()).write(answer);
    }

    private void sendBroadcast(Msg answer) {
        logger.info("Sending broadcast:  " + answer.getMsgType());
        for (int i = 0; i < connections.size(); ++i)
            connections.get(i).write(answer);
    }

    private void handle_id_is_set(Msg clientMsg) {
        Msg answer = new Msg();
        int id = clientMsg.getPlayerID();

        if (gameServerState == GameServerState.INIT_STATE) {
            gameServerState = GameServerState.WAIT_FOR_SECOND_PLAYER;
        } else {
            gameServerState = GameServerState.WAIT_FOR_FIRST_READY;

            answer.setMsgType(MsgType.PLACE_SHIPS);
            sendBroadcast(answer);
        }
    }

    private void handle_ships_placed(Msg clientMsg) {
        Msg answer = new Msg();

        int id = clientMsg.getPlayerID();
        Map clientMap = (Map) clientMsg.getDataObj();
        playersMaps.put(id, clientMap);

        if (gameServerState == GameServerState.WAIT_FOR_FIRST_READY) {
            gameServerState = GameServerState.WAIT_FOR_SECOND_READY;
        } else {
            gameServerState = GameServerState.WAIT_FOR_MOVE;

            // Random choose of first player
            int firstPlayerId = new Random().nextInt(2);
            int secondPlayerId = (firstPlayerId + 1) % 2;

            answer.setPlayerID(firstPlayerId);
            answer.setMsgType(MsgType.MAKE_MOVE);
            send(answer);

            answer.setPlayerID(secondPlayerId);
            answer.setMsgType(MsgType.WAIT_FOR_MOVE);
            send(answer);
        }
    }

    private void handle_shot_performed(Msg clientMsg) {
        Msg answer;

        int activePlayerId = clientMsg.getPlayerID();
        int waitingPlayerId = (activePlayerId + 1) % 2;
        Coordinates coordinates = new Coordinates((Coordinates) clientMsg.getDataObj());

        logger.info("ROW: " + coordinates.getRow() + ", COL: " + coordinates.getCol());
        Boolean isHit = playersMaps.get(waitingPlayerId).updateMapWithShot(coordinates);

        boolean isLoser = (playersMaps.get(waitingPlayerId).countFields(FieldState.SHIP) == 0);
        if (isLoser) {
            gameServerState = GameServerState.END;

            answer = new Msg(MsgType.WIN, activePlayerId, coordinates);
            send(answer);

            answer = new Msg(MsgType.LOSE, waitingPlayerId, coordinates);
            send(answer);

            return;
        }

        // GameServerState is WAIT_FOR_MOVE and it stays that way
        if (isHit) {
            answer = new Msg(MsgType.HIT_WAIT_FOR_MOVE, activePlayerId, coordinates);
            send(answer);

            answer = new Msg(MsgType.HIT_MAKE_MOVE, waitingPlayerId, coordinates);
            send(answer);

            return;
        }

        answer = new Msg(MsgType.MISS_WAIT_FOR_MOVE, activePlayerId, coordinates);
        send(answer);

        answer = new Msg(MsgType.MISS_MAKE_MOVE, waitingPlayerId, coordinates);
        send(answer);
    }

}
