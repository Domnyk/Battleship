package Protocol;

import java.io.Serializable;

public class Msg implements Serializable {
    private MsgType msgType;
    private Integer playerID, x, y;
    private FieldState fieldState;

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public FieldState getFieldState() {
        return fieldState;
    }

    public void setFieldState(FieldState fieldState) {
        this.fieldState = fieldState;
    }

    /**
     * @param msgType Type of message from MsgType
     * @param playerID
     * @param x x coordinate of the field on the map
     * @param y y coordinate of the field on the map
     * @param fieldState new state of the field
     */
    public Msg(MsgType msgType, Integer playerID, Integer x, Integer y, FieldState fieldState) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        this.fieldState = fieldState;
    }

    /**
     * Constructor for most messages
     * @param msgType
     * @param playerID
     */
    public Msg(MsgType msgType, int playerID) {
        this(msgType, playerID, null, null, null);
    }

    /**
     * Constructor for message from client of type SHOT_PERFORMED
     * @param msgType
     * @param playerID
     * @param x
     * @param y
     */
    public Msg(MsgType msgType, Integer playerID, Integer x, Integer y) {
        this(msgType, playerID, x, y, null);
    }

    public Msg() {
        this(null, null, null, null, null);
    }
}