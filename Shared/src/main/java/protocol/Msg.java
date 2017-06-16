package protocol;

import model.Coordinates;
import model.Map;

import java.io.Serializable;

public class Msg implements Serializable {
    private MsgType msgType;
    private Integer playerID;
    private Object dataObj;

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

    public Object getDataObj() {
        return dataObj;
    }

    public Msg(MsgType msgType, Integer playerID, model.Map map) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.dataObj = new Map(map);
    }

    public Msg(MsgType msgType, Integer playerID, Coordinates coordinates) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.dataObj = new Coordinates(coordinates);
    }

    public Msg(MsgType msgType, int playerID) {
        this.msgType = msgType;
        this.playerID = playerID;
    }

    public Msg() {
    }
}