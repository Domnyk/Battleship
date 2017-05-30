package Model;

/**
 * Enum type which describes state of a field
 */
public enum State {
    /**
     * Empty field
     */
    EMPTY,

    /**
     * Field on which a ship has been placed
     */
    SHIP,

    /**
     * Empty field which has been shoted
     */
    SHOTED,

    /**
     * Ship field which has been shoted
     */
    HIT
}
