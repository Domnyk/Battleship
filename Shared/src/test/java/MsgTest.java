
import Model.Coordinates;
import Model.Map;
import Protocol.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MsgTest {
    @Test
    public void constructorTest() {
        Integer playerId = 5;
        MsgType msgType = MsgType.MAKE_MOVE;

        Msg msg = new Msg(msgType, playerId);

        assertEquals(playerId, msg.getPlayerID());
        assertEquals(msgType, msg.getMsgType());
    }

    @Test
    public void copyConstructorTestCoordinatesVersion() {
        Integer playerId = 5;
        MsgType msgType = MsgType.SHIPS_PLACED;
        Coordinates coordinates = new Coordinates(5, 4);

        Msg msg = new Msg(msgType, playerId, coordinates);

        assertEquals(playerId, msg.getPlayerID());
        assertEquals(msgType, msg.getMsgType());
        assertEquals(false, coordinates == msg.getDataObj());
    }

    @Test
    public void copyConstructorTestMapVersion() {
        Integer playerId = 5;
        MsgType msgType = MsgType.SHIPS_PLACED;
        Map map = new Map();

        Msg msg = new Msg(msgType, playerId, map);

        assertEquals(playerId, msg.getPlayerID());
        assertEquals(msgType, msg.getMsgType());
        assertEquals(false, map == msg.getDataObj());
    }

}
