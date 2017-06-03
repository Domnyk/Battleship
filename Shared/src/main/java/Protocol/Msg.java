package Protocol;

public class Msg {
    private int msgType;
    private int playerID;
    private String msgContent;

    /**
     * Constructor for creating Msg objects from received messages. Useful in MessagesListener class
     *
     * @param receivedMsg
     */
    public Msg(String receivedMsg) {
        int delimiter1 = receivedMsg.indexOf('|');
        int delimiter2 = receivedMsg.indexOf('|', delimiter1);

        int msgType = Integer.parseInt(receivedMsg.substring(0, delimiter1));
        int playerID = Integer.parseInt(receivedMsg.substring(delimiter1 + 1, delimiter2));
        String msgContent = receivedMsg.substring(delimiter2 + 1);

        setMsgType(msgType);
        setPlayerID(playerID);
        setMsgContent(msgContent);

    }

    /**
     * Constructor for creating direct messages i.e sent with sendMessage method from MessagesHandler class
     *
     * @param msgType
     * @param playerID
     * @param msgContent
     */
    public Msg(int msgType, int playerID, String msgContent) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.msgContent = msgContent;
    }

    /**
     * Constructor for creating broadcast messages hence no playerID in argument list
     *
     * @param msgType
     * @param msgContent
     */
    public Msg(int msgType, String msgContent) {
        this.msgType = msgType;
        this.msgContent = msgContent;

        this.playerID = -1;
    }

    public String getMsgToSend() {
        return (msgType + '|' + playerID + '|' + msgContent);
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }


}
