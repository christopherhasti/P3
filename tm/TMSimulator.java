package tm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The main driver class for the Turing Machine simulator.
 * Parses the configuration file, instantiates the TuringMachine, and runs the simulation.
 * * @author Christopher Hastings and Daniel Trice
 */
public class TMSimulator {

    /**
     * The main entry point for the simulator.
     * * @param args Command line arguments. Expects a single argument for the input file path.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java tm.TMSimulator <input_file>");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            // 1. Read States and Symbols
            String line = reader.readLine();
            if (line == null) return;
            int numStates = Integer.parseInt(line.trim());

            line = reader.readLine();
            if (line == null) return;
            int numSymbols = Integer.parseInt(line.trim());
            int gammaSize = numSymbols + 1; // Includes blank symbol (0)

            // 2. Instantiate Machine
            TuringMachine tm = new TuringMachine(numStates, gammaSize);

            // 3. Read Exact Number of Transition Lines
            // According to the spec, there are exactly |Gamma| * (|Q| - 1) transition lines
            int expectedTransitions = gammaSize * (numStates - 1);
            for (int i = 0; i < expectedTransitions; i++) {
                line = reader.readLine();
                if (line == null) break;
                
                String rule = line.trim();
                if (rule.isEmpty() || rule.equalsIgnoreCase("EOF")) continue;

                String[] parts = rule.split(",");
                if (parts.length < 3) continue;

                // Calculate state and read symbol based on line index as per PDF spec
                int state = i / gammaSize;
                int symbol = i % gammaSize;

                int nextSt = Integer.parseInt(parts[0].trim());
                int writeSy = Integer.parseInt(parts[1].trim());
                int move = (parts[2].trim().toUpperCase().contains("R")) ? 1 : -1;

                tm.addRule(state, symbol, nextSt, writeSy, move);
            }

            // 4. Read Input String (Last Line)
            String inputString = reader.readLine();
            if (inputString != null) {
                inputString = inputString.trim();
                // Treat literal "EOF" or blank lines as an empty tape
                if (inputString.equalsIgnoreCase("EOF")) {
                    inputString = "";
                }
            } else {
                inputString = "";
            }

            // 5. Initialize and Run
            tm.initializeTape(inputString);
            tm.run();
            
            // 6. Output Final Visited Tape
            System.out.println(tm.getTapeOutput());

        } catch (IOException | NumberFormatException e) {
            System.err.println("Execution Error: " + e.getMessage());
        }
    }
}