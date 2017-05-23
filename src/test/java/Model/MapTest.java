package Model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MapTest {
    @Test
    public void constructorTest() {
        Map m = new Map();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(m.getField(i, j).getState(), State.EMPTY);
            }
        }
    }

    @Test
    public void getFieldTest() {
        Map m = new Map();

        assertEquals(m.getField(0, 0).getState(), State.EMPTY);
    }

    @Test
    public void setFieldTest() {
        Map m = new Map();
        State newState = State.SHIP;

        m.setField(0, 0, newState);

        assertEquals(m.getField(0, 0).getState(), newState);
    }

    @Test
    public void getGridTest() {
        Map m1 = new Map();
        Field[][] correctGrid = new Field[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                correctGrid[i][j] = new Field();
            }
        }

        assertArrayEquals(m1.getGrid(), correctGrid);
    }

    @Test
    public void setGridTest() {
        Map m1 = new Map();
        State newState = State.SHIP;
        Field[][] newGrid = new Field[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                newGrid[i][j] = new Field();
            }
        }
        newGrid[0][0].setState(newState);

        m1.setGrid(newGrid);

        assertArrayEquals(m1.getGrid(), newGrid);
    }

    @Test
    public void equalsTest() {
        Map same1 = new Map();
        Map same2 = new Map();
        Map diff = new Map();
        diff.setField(0, 0, State.SHOTED);

        assertEquals(same1, same1);
        assertEquals(same1, same2);
        assertNotEquals(same1, diff);
    }
}
