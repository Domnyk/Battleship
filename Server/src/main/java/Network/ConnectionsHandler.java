package Network;

import Model.FieldState;
import Model.GameServerState;
import Protocol.Msg;
import Protocol.MsgType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Model.Map;

import java.io.IOException;
import java.util.Random;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public final class ConnectionsHandler extends Thread {
    private ArrayBlockingQueue<Msg> gameMessages;
    private ConcurrentHashMap<Integer, ConnectionThread> connections;
    private GameServerState gameServerState;
    private HashMap<Integer, Map> playersMaps;


    private static final Logger logger = LogManager.getLogger("Server");

    public ConcurrentHashMap<Integer, ConnectionThread> getConnections() {
        return connections;
    }

    public ArrayBlockingQueue<Msg> getGameMessages() {
        return gameMessages;
    }

    public ConnectionsHandler() {
        this.setName("ConnectionsHandler");

        this.gameMessages = new ArrayBlockingQueue<Msg>(10);
        this.connections = new ConcurrentHashMap<>();
        this.gameServerState = GameServerState.INIT_STATE;
        this.playersMaps = new HashMap<>();
    }


    void addConnection(int id, ConnectionThread connectionThread) {
        if( !connections.contains(id) )
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
        }
        catch (InterruptedException e) {
            logger.info("Thread was interrupted while waiting for message to appear in blocking queue");
        }
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

    private void send(Msg answer) {
        connections.get(answer.getPlayerID()).write(answer);
    }

    private void sendBroadcast(Msg answer) {
        for(int i = 0; i < connections.size(); ++i)
            connections.get(i).write(answer);
    }

    private void handle_establish_connection(Msg clientMsg) {
        Msg answer = new Msg();
        int id = clientMsg.getPlayerID();

        if( gameServerState == GameServerState.INIT_STATE ) {
            gameServerState = GameServerState.WAIT_FOR_SECOND_PLAYER;

            answer.setMsgType(MsgType.WAIT_FOR_SECOND_PLAYER);
            answer.setPlayerID(clientMsg.getPlayerID());
            send(answer);
        }
        else {
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

        if( gameServerState == GameServerState.WAIT_FOR_FIRST_READY ) {
            gameServerState = GameServerState.WAIT_FOR_SECOND_READY;


            answer.setMsgType(MsgType.WAIT_FOR_SECOND_READY);
            answer.setPlayerID(clientMsg.getPlayerID());
            send(answer);
        }
        else {
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
        Msg answer = new Msg();

        int playerId = clientMsg.getPlayerID();
        int enemyId = (playerId+1)%2;

        int[] coordinates = (int[]) clientMsg.getDataObj();
        playersMaps.get(enemyId).updateMap(coordinates);

        boolean isLoser = (playersMaps.get(enemyId).countFields(FieldState.SHIP) == 0);
        if( isLoser ) {
            gameServerState = GameServerState.END;

            answer.setMsgType(MsgType.WIN);
            answer.setPlayerID(playerId);
            send(answer);

            answer.setMsgType(MsgType.LOSE);
            answer.setPlayerID(enemyId);
            send(answer);
        }
        else {
            // GameServerState is WAIT_FOR_MOVE and it stays that way

            answer.setMsgType(MsgType.SHOT_RESULT);
            sendBroadcast(answer);

            answer.setMsgType(MsgType.MAKE_MOVE);
            answer.setPlayerID(enemyId);
            send(answer);

            answer.setMsgType(MsgType.WAIT_FOR_MOVE);
            answer.setPlayerID(playerId);
            send(answer);
        }
    }

}
