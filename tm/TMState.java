package tm;

/**
 * Represents a single state within the Turing Machine.
 * Manages transition rules using an array structure for O(1) lookup efficiency.
 * * @author Christopher Hastings and Daniel Trice
 */
public class TMState {
    
    // Array guarantees O(1) instantaneous lookup without hashing overhead
    private final Transition[] transitions;

    /**
     * Constructs a TMState.
     * * @param gammaSize The total size of the tape alphabet, used to size the array.
     */
    public TMState(int gammaSize) {
        transitions = new Transition[gammaSize];
    }

    /**
     * Maps a read symbol to a specific transition rule.
     * * @param readSymbol The symbol triggering the transition.
     * @param nextState The state to transition into.
     * @param writeSymbol The symbol to write to the tape.
     * @param move The direction to move the head (1 for Right, -1 for Left).
     */
    public void addTransition(int readSymbol, int nextState, int writeSymbol, int move) {
        if (readSymbol >= 0 && readSymbol < transitions.length) {
            transitions[readSymbol] = new Transition(nextState, writeSymbol, move);
        }
    }

    /**
     * Retrieves the transition associated with a tape symbol.
     * * @param symbol The symbol currently read from the tape.
     * @return The corresponding Transition object, or null.
     */
    public Transition getTransition(int symbol) {
        if (symbol >= 0 && symbol < transitions.length) {
            return transitions[symbol];
        }
        return null;
    }

    /**
     * A nested data structure defining the instructions for a single transition.
     */
    public static class Transition {
        public final int nextState;
        public final int writeSymbol;
        public final int move; 

        /**
         * Constructs a Transition instruction.
         * * @param nextState Target state.
         * @param writeSymbol Symbol to write.
         * @param move Direction to move.
         */
        public Transition(int nextState, int writeSymbol, int move) {
            this.nextState = nextState;
            this.writeSymbol = writeSymbol;
            this.move = move;
        }
    }
}