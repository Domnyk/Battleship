import Model.Coordinates;
import Model.Player;
import Protocol.Msg;
import Protocol.MsgType;
import Protocol.PlayerState;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionHandler extends Thread {
    // TODO - possible problem with many thread editing same player object???
    private static final Logger logger = LogManager.getLogger("Client");
    private Player player;
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private ArrayBlockingQueue<Msg> gameMessages;

    private Msg msgFromServer;

    public ConnectionHandler(String address, int port, Player player, ArrayBlockingQueue<Msg> gameMessages) {
        this.player = player;
        this.gameMessages = gameMessages;

        try {
            socket = new Socket(address, port);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            logger.info("Error while creating socket");
        }
    }

    public void send(Msg msgToServer) {
        try {
            toServer.writeObject(msgToServer);
        } catch (IOException e) {
            logger.info("Exception occurred while writing message to server");
        }
        logger.info("Message sent: " + msgToServer.getMsgType());
    }

    public void sendInSeparateThread(Msg msgToServer) {
        new Thread(() -> {
            this.setName("Temporary sender thread");
            send(msgToServer);
        }).start();
    }

    /*private Task setIdTask = new Task<Void>() {
        @Override public Void call() throws Exception {
            player.setPlayerId(msgFromServer.getPlayerID());
            send(new Msg(MsgType.ID_IS_SET, msgFromServer.getPlayerID()));
            return null;
        }
    };

    private Task placeShipsTask = new Task<Void>() {
        @Override public Void call() throws Exception {
            player.setPlayerState(PlayerState.PLACING_SHIPS);
            return null;
        }
    };

    private Task makeMoveTask = new Task<Msg>() {
        @Override public Msg call() throws Exception {
            player.setPlayerState(PlayerState.MAKING_MOVE);
            Msg msg = new Msg(msgFromServer.getMsgType(), msgFromServer.getPlayerID());
            return msg;
        }
    };

    private Task<Msg> waitForMoveTask = new Task<Msg>() {
        @Override public Msg call() throws Exception {
            player.setPlayerState(PlayerState.WAITING_FOR_MOVE);
            Msg msg = new Msg(msgFromServer.getMsgType(), msgFromServer.getPlayerID());
            return msg;
        }
    };

    private Task<Msg> hitMakeMoveTask = new Task<Msg>() {
        @Override public Msg call() throws Exception {
            Msg msg = new Msg(msgFromServer.getMsgType(), msgFromServer.getPlayerID(),
                    (Coordinates) msgFromServer.getDataObj());
            return msg;
        }
    };

    private Task<Msg> hitWaitForMoveTask = new Task<Msg>() {
        @Override public Msg call() throws Exception {
            Msg msg = new Msg(msgFromServer.getMsgType(), msgFromServer.getPlayerID(),
                    (Coordinates) msgFromServer.getDataObj());
            return msg;
        }
    };

    private Task<Msg> missWaitForMoveTask = new Task<Msg>() {
        @Override public Msg call() throws Exception {
            Msg msg = new Msg(msgFromServer.getMsgType(), msgFromServer.getPlayerID(),
                    (Coordinates) msgFromServer.getDataObj());
            return msg;
        }
    };

    private Task<Msg> missMakeMoveTask = new Task<Msg>() {
        @Override public Msg call() throws Exception {
            Msg msg = new Msg(msgFromServer.getMsgType(), msgFromServer.getPlayerID(),
                    (Coordinates) msgFromServer.getDataObj());
            return msg;
        }
    };

    private Task<Msg> loseTask = new Task<Msg>() {
        @Override public Msg call() throws Exception {
            Msg msg = new Msg(msgFromServer.getMsgType(), msgFromServer.getPlayerID(),
                    (Coordinates) msgFromServer.getDataObj());
            return msg;
        }
    };

    private Task<Msg> winTask = new Task<Msg>() {
        @Override public Msg call() throws Exception {
            Msg msg = new Msg(msgFromServer.getMsgType(), msgFromServer.getPlayerID(),
                    (Coordinates) msgFromServer.getDataObj());
            return msg;
        }
    };

    Task getSetIdTask() { return setIdTask; }
    Task getPlaceShipsTask() { return placeShipsTask; }
    Task getMakeMoveTask() { return makeMoveTask; }
    Task getWaitForMoveTask() { return waitForMoveTask; }
    Task<Msg> getHitMakeMoveTask() { return hitMakeMoveTask; }
    Task<Msg> getHitWaitForMoveTask() { return  hitWaitForMoveTask; }
    Task<Msg> getMissWaitForMoveTask() { return missWaitForMoveTask; }
    Task<Msg> getMissMakeMoveTask() { return missMakeMoveTask; }
    Task<Msg> getLoseTask() { return loseTask; }
    Task<Msg> getWinTask() { return winTask; }
    */

    @Override
    public void run() {
        this.setName("ConnectionHandler");

        try {
            while((msgFromServer = (Msg) fromServer.readObject()) != null ) {
                logger.info("Received message: " + msgFromServer.getMsgType());
                gameMessages.add(msgFromServer);
                /*
                switch (msgFromServer.getMsgType()) {
                    case SET_ID:
                        Thread setIdTaskThread = new Thread(setIdTask);
                        setIdTaskThread.setName("setIdTask");
                        setIdTaskThread.start();
                        break;
                    case PLACE_SHIPS:
                        Thread placeShipsTaskThread = new Thread(placeShipsTask);
                        placeShipsTaskThread.setName("placeShipsTask");
                        placeShipsTaskThread.start();
                        break;
                    case MAKE_MOVE:
                        Thread makeMoveTaskThread = new Thread(makeMoveTask);
                        makeMoveTaskThread.setName("makeMoveTask");
                        makeMoveTaskThread.start();
                        break;
                    case WAIT_FOR_MOVE:
                        Thread waitForMoveTaskThread = new Thread(waitForMoveTask);
                        waitForMoveTaskThread.setName("waitForMoveTask");
                        waitForMoveTaskThread.start();
                        break;
                    case HIT_MAKE_MOVE:
                        Thread hitMakeMoveThread = new Thread(hitMakeMoveTask);
                        hitMakeMoveThread.setName("hitMakeMoveTask");
                        hitMakeMoveThread.start();
                        break;
                    case HIT_WAIT_FOR_MOVE:
                        Thread hitWaitForMoveThread = new Thread(hitWaitForMoveTask);
                        hitWaitForMoveThread.setName("hitWaitForMoveTask");
                        hitWaitForMoveThread.start();
                        break;
                    case MISS_MAKE_MOVE:
                        Thread shotMissMakeMoveThread = new Thread(missMakeMoveTask);
                        shotMissMakeMoveThread.setName("missMakeMoveTask");
                        shotMissMakeMoveThread.start();
                        break;
                    case MISS_WAIT_FOR_MOVE:
                        Thread missWaitForMoveThread = new Thread(missWaitForMoveTask);
                        missWaitForMoveThread.setName("missWaitForMoveTask");
                        missWaitForMoveThread.start();
                        break;
                    case LOSE:
                        Thread loseTaskThread = new Thread(loseTask);
                        loseTaskThread.setName("loseTask");
                        loseTaskThread.start();
                        break;
                    case WIN:
                        Thread winTaskThread = new Thread(winTask);
                        winTaskThread.setName("winTask");
                        winTaskThread.start();
                        break;
                }
                */
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

