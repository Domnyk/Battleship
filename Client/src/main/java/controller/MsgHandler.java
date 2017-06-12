package controller;

import model.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.Msg;

import java.util.concurrent.ArrayBlockingQueue;

public class MsgHandler extends Thread {
    private static final Logger logger = LogManager.getLogger("Client");

    private Controller controller;
    private ArrayBlockingQueue<Msg> messagesReceived;

    MsgHandler(Controller controller) {
        this.controller = controller;
        this.messagesReceived = controller.getConnectionHandler().getMessagesReceived();
    }


    @Override
    public void run() {
        this.setName("MsgHanlder");
        Msg msg;
        try {
            while ((msg = messagesReceived.take()) != null) {
                Coordinates coordinates = (Coordinates)msg.getDataObj();
                Integer row = (coordinates != null) ? coordinates.getRow() : null;
                Integer col = (coordinates != null) ? coordinates.getCol() : null;

                switch(msg.getMsgType()) {
                    case SET_ID:
                        controller.handleSetId(msg);
                        break;
                    case PLACE_SHIPS:
                        controller.handlePlaceShips();
                        break;
                    case MAKE_MOVE:
                        controller.handleMakeMove();
                        break;
                    case WAIT_FOR_MOVE:
                        controller.handleWaitForMove();
                        break;
                    case HIT_MAKE_MOVE:
                        controller.handleHitMakeMove(row, col);
                        break;
                    case HIT_WAIT_FOR_MOVE:
                        controller.handleHitWaitForMove(row, col);
                        break;
                    case MISS_MAKE_MOVE:
                        controller.handleMissMakeMove(row, col);
                        break;
                    case MISS_WAIT_FOR_MOVE:
                        controller.handleMissWaitForMove(row, col);
                        break;
                    case LOSE:
                        controller.handleLose(row, col);
                        break;
                    case WIN:
                        controller.handleWin(row, col);
                        break;
                }
            }
        }
        catch (InterruptedException e) {
            logger.info("Interruption signal arrived while queue was waiting for message");
        }
    }
}
