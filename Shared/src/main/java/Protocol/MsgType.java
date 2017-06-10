package Protocol;

import java.io.Serializable;

public enum MsgType implements Serializable {
    SET_ID,
    ID_IS_SET,
    WAIT_FOR_SECOND_PLAYER,
    WAIT_FOR_SECOND_READY,
    PLACE_SHIPS,
    SHIPS_PLACED,
    WAIT_FOR_MOVE,
    MAKE_MOVE,
    SHOT_PERFORMED,
    SHOT_HIT,
    SHOT_MISS,
    WIN,
    LOSE
}
