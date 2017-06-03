package Network;

import Protocol.Msg;

import java.util.ArrayList;

public final class MessagesHandler {
    private MessagesHandler(){}

    public static Msg receiveMessage(Connection connection) {
        if( connection.messagesQueue.isEmpty() ) {
            return null;
        }
        else {
            String msgString = (String) connection.messagesQueue.getFirst();
            connection.messagesQueue.removeFirst();

            Msg msg = new Msg(msgString);
            return  msg;
        }
    }

    public static void sendMessage(Connection connection, Msg msg) {
        connection.sendData(msg.getMsgToSend());
    }

    public static void sendBroadcastMessage(ArrayList<Connection> connections, Msg msg) {
        for(Connection connection: connections) {
            msg.setPlayerID(connection.getConnectionId());
            connection.sendData(msg.getMsgToSend());
        }
    }
}
