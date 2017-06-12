package Protocol;

import java.io.Serializable;

public enum MsgType implements Serializable {
    SET_ID,
    ID_IS_SET,
    PLACE_SHIPS,
    SHIPS_PLACED,
    WAIT_FOR_MOVE,
    MAKE_MOVE,
    SHOT_PERFORMED,
    HIT_MAKE_MOVE,
    HIT_WAIT_FOR_MOVE,
    MISS_MAKE_MOVE,
    MISS_WAIT_FOR_MOVE,
    WIN,
    LOSE
}
