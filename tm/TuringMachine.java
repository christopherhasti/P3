package tm;

import java.util.HashMap;
import java.util.Map;

/**
 * Simulates a deterministic Turing Machine with a bi-infinite tape.
 * The machine processes transitions until it reaches the designated halting state.
 * * @author Christopher Hastings and Daniel Trice
 */
public class TuringMachine {
    // Represents the bi-infinite tape using a Map to allow negative and positive indices. 
    // Keys are tape positions, values are symbols.
    private final Map<Long, Integer> tape = new HashMap<>();
    
    // Array holding all states within the machine
    private final TMState[] states;
    
    // The designated state that causes the machine to halt, typically |Q| - 1
    private final int haltingState;
    
    // The machine's current active state, defaults to the start state (0)
    private int currentState = 0;
    
    // The current position of the read/write head on the tape
    private long head = 0;
    
    // Tracks the furthest left position visited on the tape for final output tracking
    private long minVisited = 0;
    
    // Tracks the furthest right position visited on the tape for final output tracking
    private long maxVisited = 0;

    /**
     * Constructs a new Turing Machine simulator.
     * * @param numStates The total number of states in this machine.
     * @param initialInput The starting string to be written onto the tape.
     */
    public TuringMachine(int numStates, String initialInput) {
        this.states = new TMState[numStates];
        for (int i = 0; i < numStates; i++) {
            states[i] = new TMState();
        }
        // The halting state is always the state with the highest index
        this.haltingState = numStates - 1;
        initializeTape(initialInput);
    }

    /**
     * Initializes the tape with the provided input string starting at index 0.
     * * @param input The initial string to populate on the tape.
     */
    private void initializeTape(String input) {
        if (input == null || input.isEmpty()) return;
        for (int i = 0; i < input.length(); i++) {
            int val = input.charAt(i) - '0';
            // Only explicitly store non-zero (non-blank) symbols to conserve memory
            if (val != 0) tape.put((long) i, val);
        }
    }

    /**
     * Adds a transition rule to a specific state in the machine.
     * * @param state The index of the state the transition originates from.
     * @param readSym The symbol read from the tape that triggers this transition.
     * @param nextState The state the machine will transition into.
     * @param writeSym The symbol the machine will write to the tape.
     * @param move The direction to move the head (1 for Right, -1 for Left).
     */
    public void addRule(int state, int readSym, int nextState, int writeSym, int move) {
        if (state >= 0 && state < states.length) {
            states[state].addTransition(readSym, nextState, writeSym, move);
        }
    }

    /**
     * Begins the simulation of the Turing Machine.
     * The simulation loop continues processing transitions until the halting state is reached.
     */
    public void run() {
        while (currentState != haltingState) {
            updateBounds();
            // Read the current symbol under the head, defaulting to 0 (blank) if empty
            int currentSymbol = tape.getOrDefault(head, 0);
            TMState.Transition t = states[currentState].getTransition(currentSymbol);

            // Halt execution safely if a missing transition is encountered (prevents infinite loops)
            if (t == null) break;

            // Write to the tape
            if (t.writeSymbol != 0) {
                tape.put(head, t.writeSymbol);
            } else {
                // If writing a blank (0), remove the entry to simulate an empty cell
                tape.remove(head);
            }

            // Move the read/write head and update the active state
            head += t.move;
            currentState = t.nextState;
        }
        // Ensure the bounds encompass the final position of the head after halting
        updateBounds();
    }

    /**
     * Updates the minimum and maximum boundaries of the visited tape cells.
     * Used to determine the span of the tape to print upon completion.
     */
    private void updateBounds() {
        minVisited = Math.min(minVisited, head);
        maxVisited = Math.max(maxVisited, head);
    }

    /**
     * Retrieves the contents of all visited tape cells as a continuous string.
     * * @return A string representing the final state of the visited portion of the tape.
     */
    public String getTapeOutput() {
        StringBuilder sb = new StringBuilder();
        // Iterate from the furthest left visited cell to the furthest right
        for (long i = minVisited; i <= maxVisited; i++) {
            sb.append(tape.getOrDefault(i, 0));
        }
        return sb.toString();
    }
}