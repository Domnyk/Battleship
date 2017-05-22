package Model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FieldTest {
    @Test
    public void getAndSetTest() {
        Field field1 = new Field();

        State state = State.HIT;
        Field field2 = new Field(state);

        assertEquals(field1.getState(), State.EMPTY);
        assertEquals(field2.getState(), state);
    }
}
