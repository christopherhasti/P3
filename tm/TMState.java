package tm;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single state within the Turing Machine.
 * Maps symbols read from the tape to their corresponding transition rules.
 * * @author Christopher Hastings and Daniel Trice
 */
public class TMState {
    
    // Maps a read symbol to the specific Transition instruction
    private final Map<Integer, Transition> transitions = new HashMap<>();

    /**
     * Adds a new transition instruction to this state.
     * * @param readSymbol The symbol that must be read to trigger the transition.
     * @param nextState The state to transition into.
     * @param writeSymbol The symbol to write to the current tape cell.
     * @param move The direction to move the head (1 for Right, -1 for Left).
     */
    public void addTransition(int readSymbol, int nextState, int writeSymbol, int move) {
        transitions.put(readSymbol, new Transition(nextState, writeSymbol, move));
    }

    /**
     * Retrieves the transition instruction associated with a specific read symbol.
     * * @param symbol The symbol currently read from the tape.
     * @return The corresponding Transition object, or null if no transition exists for the symbol.
     */
    public Transition getTransition(int symbol) {
        return transitions.get(symbol);
    }

    /**
     * A nested data class representing the instructions for a single transition.
     */
    public static class Transition {
        
        // The index of the target state
        public final int nextState;
        
        // The symbol to be written to the tape
        public final int writeSymbol;
        
        // The direction the tape head should move (1 for Right, -1 for Left)
        public final int move; 

        /**
         * Constructs a Transition object.
         * * @param nextState The state to transition into.
         * @param writeSymbol The symbol to write to the tape.
         * @param move The direction to move the tape head.
         */
        public Transition(int nextState, int writeSymbol, int move) {
            this.nextState = nextState;
            this.writeSymbol = writeSymbol;
            this.move = move;
        }
    }
}