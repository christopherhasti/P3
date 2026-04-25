package tm;

/**
 * Simulates a deterministic Turing Machine with an efficient bi-infinite tape.
 * Uses dynamically expanding primitive arrays for maximum speed to prevent timeouts.
 * * @author Christopher Hastings and Daniel Trice
 */
public class TuringMachine {
    
    // Primitive arrays ensure maximum performance over object-based collections
    private int[] posTape;
    private int[] negTape;
    
    private final TMState[] states;
    private final int haltingState;
    
    private int currentState = 0;
    private long head = 0;
    
    private long minVisited = 0;
    private long maxVisited = 0;

    /**
     * Constructs a Turing Machine simulator.
     * * @param numStates The total number of states in this machine.
     * @param gammaSize The total size of the tape alphabet.
     */
    public TuringMachine(int numStates, int gammaSize) {
        this.states = new TMState[numStates];
        for (int i = 0; i < numStates; i++) {
            states[i] = new TMState(gammaSize);
        }
        this.haltingState = numStates - 1;
        
        // Initialize tape halves with a generous default capacity to minimize re-sizing
        this.posTape = new int[10000];
        this.negTape = new int[10000];
    }

    /**
     * Initializes the tape with the provided input string starting at index 0.
     * * @param input The initial string to write to the tape.
     */
    public void initializeTape(String input) {
        if (input == null || input.isEmpty()) return;
        
        for (int i = 0; i < input.length(); i++) {
            int val = input.charAt(i) - '0';
            writeTape(i, val);
        }
        // Ensure initial input length is captured in the visited bounds
        maxVisited = Math.max(0, input.length() - 1);
    }

    /**
     * Adds a transition rule to a specific state.
     * * @param state The originating state.
     * @param readSym The symbol read from the tape.
     * @param nextState The next state to transition to.
     * @param writeSym The symbol to write.
     * @param move The direction to move (1 for Right, -1 for Left).
     */
    public void addRule(int state, int readSym, int nextState, int writeSym, int move) {
        if (state >= 0 && state < states.length) {
            states[state].addTransition(readSym, nextState, writeSym, move);
        }
    }

    /**
     * Executes the simulation loop until the halting state is reached.
     */
    public void run() {
        while (currentState != haltingState) {
            updateBounds();
            
            int currentSymbol = readTape(head);
            TMState.Transition t = states[currentState].getTransition(currentSymbol);

            // Safely break if no transition exists (prevents infinite loop on bad input)
            if (t == null) break;

            writeTape(head, t.writeSymbol);
            head += t.move;
            currentState = t.nextState;
        }
        updateBounds();
    }

    /**
     * Updates the minimum and maximum boundaries of visited tape cells.
     */
    private void updateBounds() {
        if (head < minVisited) minVisited = head;
        if (head > maxVisited) maxVisited = head;
    }

    /**
     * Reads a symbol from the bi-infinite tape.
     * * @param index The tape index to read from.
     * @return The symbol at the index, or 0 (blank) if empty.
     */
    private int readTape(long index) {
        if (index >= 0) {
            if (index < posTape.length) return posTape[(int) index];
            return 0; // Blank symbol
        } else {
            long posIdx = -index;
            if (posIdx < negTape.length) return negTape[(int) posIdx];
            return 0;
        }
    }

    /**
     * Writes a symbol to the bi-infinite tape, expanding arrays dynamically if needed.
     * * @param index The tape index to write to.
     * @param symbol The symbol to write.
     */
    private void writeTape(long index, int symbol) {
        if (index >= 0) {
            int posIdx = (int) index;
            if (posIdx >= posTape.length) {
                int[] newTape = new int[Math.max(posTape.length * 2, posIdx + 1)];
                System.arraycopy(posTape, 0, newTape, 0, posTape.length);
                posTape = newTape;
            }
            posTape[posIdx] = symbol;
        } else {
            int posIdx = (int) -index;
            if (posIdx >= negTape.length) {
                int[] newTape = new int[Math.max(negTape.length * 2, posIdx + 1)];
                System.arraycopy(negTape, 0, newTape, 0, negTape.length);
                negTape = newTape;
            }
            negTape[posIdx] = symbol;
        }
    }

    /**
     * Retrieves the contents of all visited tape cells.
     * * @return A continuous string representing the visited tape segment.
     */
    public String getTapeOutput() {
        StringBuilder sb = new StringBuilder();
        for (long i = minVisited; i <= maxVisited; i++) {
            sb.append(readTape(i));
        }
        return sb.toString();
    }
}