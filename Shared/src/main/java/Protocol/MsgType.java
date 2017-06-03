package Protocol;

public class MsgType {
    /**
     * C_* means messages sent by the client
     * S_* means messages sent by the server
     * SB_* means messages sent by the server to all clients
     */

    /**
     * Send when client tries to connect to the server
     */
    public static final int C_CONNECT = 0;

    /**
     * Send when server accepted the connection from the client
     */
    public static final int S_CONNECTION_OK = 1;


    // Propably will need to get rid of this
    /**
     * Send when something went wrong
     */
    public static final int S_CONNECTION_FAIL = 2;

    /**
     * Send to inform player that other player has connected
     */
    public static final int S_OTHER_CONNECTION = 3;

    /**
     * Send when players can place their ships
     */
    public static final int SB_PLACE_SHIPS = 4;

    /**
     *  Send when player finished placing his ships
     */
    public static final int C_READY = 5;

    /**
     * Send when it's player's turn
     */
    public static final int S_YOUR_MOVE = 6;

    /**
     * Send when it's enemy's turn
     */
    public static final int S_WAIT_FOR_MOVE = 7;

    /**
     * Send when player is shooting
     */
    public static final int C_SHOT = 8;

    /**
     * Send when server has computed result of a shot
     */
    public static final int SB_SHOT_RESULT = 9;

    /**
     * Send to the player who has lost
     */
    public static final int S_LOSE = 10;

    /**
     * Send to the player who has won
     */
    public static final int S_WIN = 11;
}
