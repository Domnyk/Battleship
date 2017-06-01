package Protocol;

public enum MessageType {
    /**
     * C_* means messages sent by the client
     * S_* means messages sent by the server
     * SB_* means messages sent by the server to all clients
     */

    /**
     * Send when client tries to connect to the server
     */
    C_CONNECT,

    /**
     * Send when server accepted the connection from the client
     */
    S_CONNECTION_OK,


    // Propably will need to get rid of this
    /**
     * Send when something went wrong
     */
    S_CONNECTION_FAIL,

    /**
     * Send to inform player that other player has connected
     */
    S_OTHER_CONNECTION,

    /**
     * Send when players can place their ships
     */
    SB_PLACE_SHIPS,

    /**
     *  Send when player finished placing his ships
     */
    C_READY,

    /**
     * Send when it's player's turn
     */
    S_YOUR_MOVE,

    /**
     * Send when it's enemy's turn
     */
    S_WAIT_FOR_MOVE,

    /**
     * Send when player is shooting
     */
    C_SHOT,

    /**
     * Send when server has computed result of a shot
     */
    SB_SHOT_RESULT,

    /**
     * Send to the player who has lost
     */
    S_LOSE,

    /**
     * Send to the player who has won
     */
    S_WIN
}
