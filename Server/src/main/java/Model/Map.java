package Model;

import java.util.Arrays;

/**
 * Map
 * Represents one player's map
 */
public class Map {
    private Field[][] grid;

    Map() {
        grid = new Field[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = new Field();
            }
        }
    }

    public Field[][] getGrid() {
        return grid;
    }

    public void setGrid(Field[][] newGrid) {
        this.grid = newGrid;
    }

    public Field getField(int a, int b) {
        return grid[a][b];
    }

    public void setField(int a, int b, State newState) {
        grid[a][b].setState(newState);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;

        return Arrays.deepEquals(getGrid(), map.getGrid());
    }
}
