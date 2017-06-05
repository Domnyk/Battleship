package Protocol;

public enum PlayerState {
    NOT_CONNECTED,
    WAIT_FOR_SECOND_PLAYER,
    PLACING_SHIPS,
    READY,
    MAKING_MOVE,
    WAIT_FOR_MOVE,
    WAIT_FOR_RESULT,
    WINNER,
    LOSER
}
