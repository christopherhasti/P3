package tm;

import java.util.HashMap;
import java.util.Map;

public class TMState {
    private final Map<Integer, Transition> transitions = new HashMap<>();

    public void addTransition(int readSymbol, int nextState, int writeSymbol, int move) {
        transitions.put(readSymbol, new Transition(nextState, writeSymbol, move));
    }

    public Transition getTransition(int symbol) {
        return transitions.get(symbol);
    }

    public static class Transition {
        public final int nextState;
        public final int writeSymbol;
        public final int move; //1 for R, -1 for L

        public Transition(int nextState, int writeSymbol, int move) {
            this.nextState = nextState;
            this.writeSymbol = writeSymbol;
            this.move = move;
        }
    }
}