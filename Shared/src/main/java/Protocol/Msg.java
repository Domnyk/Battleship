package Protocol;

import Model.FieldState;

import java.io.Serializable;

public class Msg implements Serializable {
    private MsgType msgType;
    private Integer playerID, x, y;
    private Object dataObj;
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

    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
    }

    /**
     * @param msgType Type of message from MsgType
     * @param playerID
     * @param dataObj Contains information relevant to type of message
     */
    public Msg(MsgType msgType, Integer playerID, Object dataObj) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.dataObj = dataObj;
    }

    /**
     * Constructor for most messages
     * @param msgType
     * @param playerID
     */
    public Msg(MsgType msgType, int playerID) {
        this(msgType, playerID, null);
    }

    public Msg() {
        this(null, null, null);
    }
}