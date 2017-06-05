package Protocol;

public enum MsgType {
    SET_ID,
    ESTABLISH_CONNECTION,
    WAIT_FOR_SECOND_PLAYER,
    PLACE_SHIPS,
    SHIPS_PLACED,
    WAIT_FOR_MOVE,
    MAKE_MOVE,
    SHOT_PERFORMED,
    SHOT_RESULT,
    WIN,
    LOSE
}
