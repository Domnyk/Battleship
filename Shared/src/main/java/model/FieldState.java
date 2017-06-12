package model;

import java.io.Serializable;

public enum FieldState implements Serializable {
    /**
     * Empty field
     */
    EMPTY,

    /**
     * Field on which a ship has been placed
     */
    SHIP,

    /**
     * Empty field which has been shot
     */
    SHOT,

    /**
     * Ship field which has been shot
     */
    HIT
}
