import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import Model.*;
import org.junit.Test;

public class MapTest {
    @Test
    public void constructorTest() {
        Map m = new Map();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(m.getFieldState(i, j), FieldState.EMPTY);
            }
        }
    }

    @Test
    public void getFieldStateTest() {
        Map m = new Map();

        assertEquals(m.getFieldState(0, 0), FieldState.EMPTY);
    }

    @Test
    public void setFieldStateTest() {
        Map m = new Map();
        FieldState newFieldState = FieldState.SHIP;

        m.setFieldState(0, 0, newFieldState);
        assertEquals(m.getFieldState(0, 0), newFieldState);
    }

    @Test
    public void getGridTest() {
        Map m1 = new Map();
        FieldState[][] correctGrid = new FieldState[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                correctGrid[i][j] = FieldState.EMPTY;
            }
        }

        assertArrayEquals(m1.getGrid(), correctGrid);
    }

    @Test
    public void setGridTest() {
        Map m = new Map();
        FieldState newFieldState = FieldState.SHIP;
        FieldState[][] newGrid = new FieldState[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                newGrid[i][j] = FieldState.EMPTY;
            }
        }
        newGrid[0][0] = newFieldState;

        m.setGrid(newGrid);
        assertArrayEquals(m.getGrid(), newGrid);
    }

    @Test
    public void equalsTest() {
        Map same1 = new Map();
        Map same2 = new Map();
        Map diff = new Map();
        diff.setFieldState(0, 0, FieldState.SHOTED);

        assertEquals(same1, same1);
        assertEquals(same1, same2);
        assertNotEquals(same1, diff);
    }

    @Test
    public void countFieldsTest() {
        Map map1 = new Map();

        map1.setFieldState(0, 0, FieldState.SHIP);
        map1.setFieldState(0, 1, FieldState.SHIP);
        map1.setFieldState(0, 2, FieldState.SHOTED);

        int countShip = map1.countFields(FieldState.SHIP);
        int countShoted = map1.countFields(FieldState.SHOTED);
        int countEmpty = map1.countFields(FieldState.EMPTY);

        assertEquals(countShip, 2);
        assertEquals(countShoted, 1);
        assertEquals(countEmpty, 97);
    }
}
