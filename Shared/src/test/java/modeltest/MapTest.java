package modeltest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import model.*;
import model.Coordinates;
import org.junit.Test;

public class MapTest {
    @Test
    public void constructorTest() {
        Map map = new Map();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(map.getFieldState(i, j), FieldState.EMPTY);
            }
        }
    }

    @Test
    public void copyConstructorTest() {
        Map original = new Map();

        original.setFieldState(0,0, FieldState.SHIP);
        Map copy = new Map(original);

        assertEquals(false, original == copy);
        assertEquals(true, original.equals(copy));
    }

    @Test
    public void getFieldStateTest() {
        Map map = new Map();

        assertEquals(map.getFieldState(0, 0), FieldState.EMPTY);
    }

    @Test
    public void setFieldStateTest() {
        Map map = new Map();
        FieldState newFieldState = FieldState.SHIP;

        map.setFieldState(0, 0, newFieldState);
        assertEquals(map.getFieldState(0, 0), newFieldState);
    }

    @Test
    public void getGridTest() {
        Map map = new Map();
        FieldState[][] correctGrid = new FieldState[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                correctGrid[i][j] = FieldState.EMPTY;
            }
        }

        assertArrayEquals(map.getGrid(), correctGrid);
    }

    @Test
    public void setGridTest() {
        Map map = new Map();
        FieldState newFieldState = FieldState.SHIP;
        FieldState[][] newGrid = new FieldState[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                newGrid[i][j] = FieldState.EMPTY;
            }
        }
        newGrid[0][0] = newFieldState;

        map.setGrid(newGrid);
        assertArrayEquals(map.getGrid(), newGrid);
    }

    @Test
    public void equalsTest() {
        Map same1 = new Map();
        Map same2 = new Map();
        Map diff = new Map();
        diff.setFieldState(0, 0, FieldState.SHOT);

        assertEquals(same1, same1);
        assertEquals(same1, same2);
        assertNotEquals(same1, diff);
    }

    @Test
    public void countFieldsTest() {
        Map map = new Map();

        map.setFieldState(0, 0, FieldState.SHIP);
        map.setFieldState(0, 1, FieldState.SHIP);
        map.setFieldState(0, 2, FieldState.SHOT);

        int countShip = map.countFields(FieldState.SHIP);
        int countShoted = map.countFields(FieldState.SHOT);
        int countEmpty = map.countFields(FieldState.EMPTY);

        assertEquals(2, countShip);
        assertEquals(1, countShoted);
        assertEquals(97, countEmpty);
    }

    @Test
    public void updateMapWithShotTestWithEmptyToShoted() {
        Map map = new Map();

        Coordinates coordinates = new Coordinates(0, 0);

        assertEquals(false, map.updateMapWithShot(coordinates));
        assertEquals(1, map.countFields(FieldState.SHOT));
        assertEquals(99, map.countFields(FieldState.EMPTY));
        assertEquals(0, map.countFields(FieldState.HIT));
        assertEquals(0, map.countFields(FieldState.SHIP));
    }

    @Test
    public void updateMapWithShotTestWithShipToHit() {
        Map map = new Map();
        map.setFieldState(0, 0, FieldState.SHIP);

        Coordinates coordinates = new Coordinates(0, 0);

        assertEquals(true, map.updateMapWithShot(coordinates));
        assertEquals(0, map.countFields(FieldState.SHOT));
        assertEquals(99, map.countFields(FieldState.EMPTY));
        assertEquals(1, map.countFields(FieldState.HIT));
        assertEquals(0, map.countFields(FieldState.SHIP));
    }
}
