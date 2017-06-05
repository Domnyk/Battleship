package Model;

/**
 * Enum type which describes state of a field
 */
public enum FieldState {
    /**
     * Empty field
     */
    EMPTY,

    /**
     * Field2 on which a ship has been placed
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
