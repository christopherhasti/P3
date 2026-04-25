package tm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TMSimulator {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java tm.TMSimulator <input_file>");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line = reader.readLine();
            if (line == null) return;
            int numStates = Integer.parseInt(line.trim());

            line = reader.readLine();
            if (line == null) return;
            int numSymbols = Integer.parseInt(line.trim());
            int gammaSize = numSymbols + 1;

            //read all transitions until EOF
            List<String> transitionLines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("EOF")) break;
                if (!line.isEmpty()) {
                    transitionLines.add(line);
                }
            }

            //line immediately following EOF is input string
            String inputString = reader.readLine();
            TuringMachine tm = new TuringMachine(numStates, inputString != null ? inputString.trim() : "");

            for (int i = 0; i < transitionLines.size(); i++) {
                String rule = transitionLines.get(i);
                String[] parts = rule.split(",");
                if (parts.length < 3) continue;

                int state = i / gammaSize;
                int symbol = i % gammaSize;

                int nextSt = Integer.parseInt(parts[0].trim());
                int writeSy = Integer.parseInt(parts[1].trim());
                int move = (parts[2].trim().toUpperCase().contains("R")) ? 1 : -1;

                tm.addRule(state, symbol, nextSt, writeSy, move);
            }

            //run/display output
            tm.run();
            System.out.println(tm.getTapeOutput());

        } catch (IOException | NumberFormatException e) {
            System.err.println("Execution Error: " + e.getMessage());
        }
    }
}