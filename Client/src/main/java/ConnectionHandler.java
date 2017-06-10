import Model.Player;
import Protocol.Msg;
import Protocol.MsgType;
import Protocol.PlayerState;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler extends Task {
    private static final Logger logger = LogManager.getLogger("Client");
    private Player player;
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    public ConnectionHandler(String address, int port) {
        try {
            socket = new Socket(address, port);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            logger.info("Error while creating socket");
        }
    }


    @Override
    public Void call() throws Exception {

        logger.info("Thread is up");

        /*try {
            Msg msgFromServer;
            while((msgFromServer = (Msg) fromServer.readObject()) != null ) {
                logger.info("Received message of type " + msgFromServer.getMsgType());

                switch (msgFromServer.getMsgType()) {
                    case SET_ID:
                        handle_set_id(msgFromServer);
                        break;
                    case WAIT_FOR_SECOND_PLAYER:
                        break;
                    case PLACE_SHIPS:
                        break;
                    case WAIT_FOR_SECOND_READY:
                        break;
                    case MAKE_MOVE:
                        break;
                    case WAIT_FOR_MOVE:
                        break;
                    case SHOT_RESULT:
                        break;
                    case LOSE:
                        break;
                    case WIN:
                        break;
                }
            }
        }
        catch (Exception e) {
            logger.warn("Exception during socket opening");
        }*/

        return null;
    }
}
