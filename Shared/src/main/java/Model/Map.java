package Model;

import java.util.Arrays;

/**
 * Map
 * Represents one Player's map
 */
public class Map {
    private FieldState[][] grid;

    public Map() {
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

    public void updateMap(int[] coordinates) {
        int x = coordinates[0];
        int y = coordinates[1];

        FieldState fs = getFieldState(x, y);
        if( fs == FieldState.EMPTY ) {
            setFieldState(x, y, FieldState.SHOTED);
        }

        if( fs == FieldState.SHIP ) {
            setFieldState(x, y, FieldState.HIT);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;

        return Arrays.deepEquals(getGrid(), map.getGrid());
    }
}
