package Protocol;

import Model.Coordinates;
import Model.FieldState;
import Model.Map;

import java.io.Serializable;

public class Msg implements Serializable {
    private MsgType msgType;
    private Integer playerID;
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

    public Object getDataObj() {
        return dataObj;
    }

    /**
     * @param msgType Type of message from MsgType
     * @param playerID
     * @param dataObj Contains information relevant to type of message
     */

    /*public Msg(MsgType msgType, Integer playerID, Object dataObj) {
        this.msgType = msgType;
        this.playerID = playerID;

        if (msgType == MsgType.SHIPS_PLACED ) {
            this.dataObj = new Model.Map((Model.Map)dataObj);
            return;
        }

        if(msgType == MsgType.SHOT_PERFORMED || msgType == MsgType.SHOT_HIT || msgType == MsgType.SHOT_MISS ||
           msgType == MsgType.WIN || msgType == MsgType.LOSE) {
            this.dataObj = new Coordinates((Coordinates)dataObj);
            return;
        }
    }*/

    public Msg(MsgType msgType, Integer playerID, Model.Map map) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.dataObj = new Map(map);
    }

    public Msg(MsgType msgType, Integer playerID, Coordinates coordinates) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.dataObj = new Coordinates(coordinates);
    }


    /**
     * Constructor for most messages
     * @param msgType
     * @param playerID
     */
    public Msg(MsgType msgType, int playerID) {
        this.msgType = msgType;
        this.playerID = playerID;
    }

    public Msg() {}

    /*public Msg(Msg otherMsg) {
        this.msgType = otherMsg.getMsgType();
        this.playerID = otherMsg.getPlayerID();

        if (msgType == MsgType.SHIPS_PLACED ) {
            this.dataObj = new Model.Map((Model.Map)otherMsg.getDataObj());
        }
        else {
            this.dataObj = new Coordinates((Coordinates)otherMsg.getDataObj());
        }
    }*/
}