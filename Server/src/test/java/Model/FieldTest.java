package Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class FieldTest {
    /**
     * Constructor implemented with usage of setField method.
     * That's why no separate tests for constructors
     */
    @Test
    public void getAndSetTest() {
        Field field1 = new Field();

        State state = State.HIT;
        Field field2 = new Field(state);

        assertEquals(field1.getState(), State.EMPTY);
        assertEquals(field2.getState(), state);
    }

    @Test
    public void equalsTest() {
        Field same1 = new Field(State.HIT);
        Field same2 = new Field(State.HIT);
        Field diff = new Field(State.SHOTED);

        assertEquals(same1, same1);
        assertEquals(same1, same2);
        assertNotEquals(same1, diff);
    }
}
