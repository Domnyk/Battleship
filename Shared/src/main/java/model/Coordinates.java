package model;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Integer row;
    private Integer col;

    public Coordinates(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    public Coordinates(Coordinates coordinates) {
        if (coordinates == null) return;

        this.row = coordinates.getRow();
        this.col = coordinates.getCol();
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }
}
