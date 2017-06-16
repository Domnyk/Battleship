package modeltest;

import model.Coordinates;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoordinatesTest {
    @Test
    public void constructorTest() {
        Integer row = 5;
        Integer col = 4;

        Coordinates coordinates = new Coordinates(5, 4);

        assertEquals(row, coordinates.getRow());
        assertEquals(col, coordinates.getCol());
    }

    @Test
    public void copyConstructorTest() {
        Coordinates original = new Coordinates(5, 4);
        Coordinates copy = new Coordinates(original);

        assertEquals(false, original == copy);
        assertEquals(original.getCol(), copy.getCol());
        assertEquals(original.getRow(), copy.getRow());
    }
}
