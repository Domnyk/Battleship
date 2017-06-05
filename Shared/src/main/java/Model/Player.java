package Model;

import Protocol.PlayerState;

public class Player {
    Map playerMap, enemyMap;
    Integer playerId;
    PlayerState playerState;

    public Player(Map playerMap, Map enemyMap, Integer playerId, PlayerState playerState) {
        this.playerMap = playerMap;
        this.enemyMap = enemyMap;
        this.playerId = playerId;
        this.playerState = playerState;
    }

    public Map getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(Map playerMap) {
        this.playerMap = playerMap;
    }

    public Map getEnemyMap() {
        return enemyMap;
    }

    public void setEnemyMap(Map enemyMap) {
        this.enemyMap = enemyMap;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }
}
