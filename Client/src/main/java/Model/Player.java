package Model;

import Protocol.PlayerState;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;

public class Player {
    Map playerMap, enemyMap;
    private Integer playerId;
    private PlayerState playerState;

    /*public Player(Map playerMap, Map enemyMap, Integer playerId, PlayerState playerState) {
        this.playerMap = playerMap;
        this.enemyMap = enemyMap;
        this.playerId = playerId;
        this.playerState = playerState;
    }*/

    public Player(Integer playerId) {
        this.playerMap = new Map();
        this.enemyMap = new Map();
        this.playerId = playerId;
        this.playerState = PlayerState.INIT_STATE;
        //this(new Map(), new Map(), id, PlayerState.INIT_STATE);
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
