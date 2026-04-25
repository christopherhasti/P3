package tm;

import java.util.HashMap;
import java.util.Map;

public class TuringMachine {
    private final Map<Long, Integer> tape = new HashMap<>();
    private final TMState[] states;
    private final int haltingState;
    private int currentState = 0;
    private long head = 0;
    private long minVisited = 0;
    private long maxVisited = 0;

    public TuringMachine(int numStates, String initialInput) {
        this.states = new TMState[numStates];
        for (int i = 0; i < numStates; i++) {
            states[i] = new TMState();
        }
        this.haltingState = numStates - 1;
        initializeTape(initialInput);
    }

    private void initializeTape(String input) {
        if (input == null || input.isEmpty()) return;
        for (int i = 0; i < input.length(); i++) {
            int val = input.charAt(i) - '0';
            if (val != 0) tape.put((long) i, val);
        }
    }

    public void addRule(int state, int readSym, int nextState, int writeSym, int move) {
        if (state >= 0 && state < states.length) {
            states[state].addTransition(readSym, nextState, writeSym, move);
        }
    }

    public void run() {
        while (currentState != haltingState) {
            updateBounds();
            int currentSymbol = tape.getOrDefault(head, 0);
            TMState.Transition t = states[currentState].getTransition(currentSymbol);

            if (t == null) break;

            //write to tape
            if (t.writeSymbol != 0) {
                tape.put(head, t.writeSymbol);
            } else {
                tape.remove(head);
            }

            //move head and update state
            head += t.move;
            currentState = t.nextState;
        }
        updateBounds();
    }

    private void updateBounds() {
        minVisited = Math.min(minVisited, head);
        maxVisited = Math.max(maxVisited, head);
    }

    public String getTapeOutput() {
        StringBuilder sb = new StringBuilder();
        for (long i = minVisited; i <= maxVisited; i++) {
            sb.append(tape.getOrDefault(i, 0));
        }
        return sb.toString();
    }
}