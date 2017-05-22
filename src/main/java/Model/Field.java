package Model;


public class Field {
    private State state;

    Field() {
        setState(State.EMPTY);
    }

    Field(State state) {
        setState(state);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
