package tm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The main driver class for the Turing Machine simulator.
 * This class reads an input file to parse the states, alphabet size, transitions,
 * and the initial input string. It then instantiates and runs the TuringMachine.
 * * @author Christopher Hastings and Daniel Trice
 */
public class TMSimulator {
    
    /**
     * The main method that serves as the entry point for the simulator.
     * * @param args Command line arguments. Expects a single argument specifying the input file path.
     */
    public static void main(String[] args) {
        // Ensure the user provides exactly one command line argument for the input file
        if (args.length < 1) {
            System.err.println("Usage: java tm.TMSimulator <input_file>");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            // Parse the total number of states in the TM
            String line = reader.readLine();
            if (line == null) return;
            int numStates = Integer.parseInt(line.trim());

            // Parse the number of symbols in the alphabet (Sigma)
            line = reader.readLine();
            if (line == null) return;
            int numSymbols = Integer.parseInt(line.trim());
            // Gamma size includes the input alphabet plus the blank symbol (0)
            int gammaSize = numSymbols + 1;

            // Read all transition rules until "EOF" or the end of the transition block is reached
            List<String> transitionLines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("EOF")) break;
                if (!line.isEmpty()) {
                    transitionLines.add(line);
                }
            }

            // The line immediately following the transitions (or EOF) represents the input string
            String inputString = reader.readLine();
            TuringMachine tm = new TuringMachine(numStates, inputString != null ? inputString.trim() : "");

            // Iterate through the collected transition lines to populate the TM's rules
            for (int i = 0; i < transitionLines.size(); i++) {
                String rule = transitionLines.get(i);
                String[] parts = rule.split(",");
                // Skip improperly formatted rules
                if (parts.length < 3) continue;

                // Calculate the originating state and read symbol based on the line index
                int state = i / gammaSize;
                int symbol = i % gammaSize;

                // Parse the target state, the symbol to write, and the direction to move
                int nextSt = Integer.parseInt(parts[0].trim());
                int writeSy = Integer.parseInt(parts[1].trim());
                // Convert string direction to integer: 1 for Right, -1 for Left
                int move = (parts[2].trim().toUpperCase().contains("R")) ? 1 : -1;

                // Add the parsed rule to the Turing Machine
                tm.addRule(state, symbol, nextSt, writeSy, move);
            }

            // Execute the simulation and display the final tape output
            tm.run();
            System.out.println(tm.getTapeOutput());

        } catch (IOException | NumberFormatException e) {
            System.err.println("Execution Error: " + e.getMessage());
        }
    }
}