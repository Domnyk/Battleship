package Model;

import Protocol.FieldState;

import java.util.Arrays;

/**
 * Map
 * Represents one player's map
 */
public class Map {
    private FieldState[][] grid;

    Map() {
        grid = new FieldState[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = FieldState.EMPTY;
            }
        }
    }

    public FieldState[][] getGrid() {
        return grid;
    }

    public void setGrid(FieldState[][] newGrid) {
        this.grid = newGrid;
    }

    public FieldState getFieldState(int a, int b) {
        return grid[a][b];
    }

    public void setFieldState(int a, int b, FieldState newFieldState) {
        grid[a][b] = newFieldState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;

        return Arrays.deepEquals(getGrid(), map.getGrid());
    }
}
