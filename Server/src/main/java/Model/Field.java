package Model;


public class Field {
    private State state;

    Field() {
        setState(State.EMPTY);
    }

    Field(State newState) {
        setState(newState);
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        return getState() == field.getState();
    }
}
