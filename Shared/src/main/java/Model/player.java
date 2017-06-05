package Model;

import Protocol.PlayerState;

public class player {
    Map player, enemy;
    Integer playerId;
    PlayerState playerState;

    public player(Map player, Map enemy, Integer playerId, PlayerState playerState) {
        this.player = player;
        this.enemy = enemy;
        this.playerId = playerId;
        this.playerState = playerState;
    }

    public Map getPlayer() {
        return player;
    }

    public void setPlayer(Map player) {
        this.player = player;
    }

    public Map getEnemy() {
        return enemy;
    }

    public void setEnemy(Map enemy) {
        this.enemy = enemy;
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
