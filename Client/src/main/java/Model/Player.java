package Model;

public class Player {
    private Map playerMap;
    private Integer playerId;

    public Player(Integer playerId) {
        this.playerMap = new Map();
        this.playerId = playerId;
    }

    public Map getPlayerMap() {
        return playerMap;
    }
    public Integer getPlayerId() {
        return playerId;
    }
    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }
}
