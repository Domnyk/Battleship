package Model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Map
 * Represents one Player's map
 */
public class Map implements Serializable {
    private FieldState[][] grid;

    public Map() {
        grid = new FieldState[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = FieldState.EMPTY;
            }
        }
    }

    public Map(Map otherMap) {
        grid = new FieldState[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = otherMap.getFieldState(i, j);
            }
        }
    }

    public FieldState[][] getGrid() {
        return grid;
    }

    public void setGrid(FieldState[][] newGrid) {
        this.grid = newGrid;
    }

    public FieldState getFieldState(int row, int col) {
        return grid[row][col];
    }

    public void setFieldState(int row, int col, FieldState newFieldState) {
        grid[row][col] = newFieldState;
    }

    public int countFields(FieldState fieldState) {
        int result = 0;
        for (FieldState[] fsRow: grid) {
            for (FieldState fs: fsRow) {
                if( fs == fieldState )
                    ++result;
            }
        }

        return result;
    }

    public Boolean updateMapWithShot(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();

        FieldState fs = getFieldState(row, col);
        if( fs == FieldState.EMPTY ) {
            setFieldState(row, col, FieldState.SHOT);
            return false;
        }

        if( fs == FieldState.SHIP ) {
            setFieldState(row, col, FieldState.HIT);
            return true;
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;

        return Arrays.deepEquals(getGrid(), map.getGrid());
    }
}
