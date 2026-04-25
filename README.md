# Project 3: Turing Machine Simulator

* Author: Christopher Hastings and Daniel Trice
* Class: CS 361
* Semester: Spring 2026

## Overview

This Java application simulates a deterministic Turing Machine equipped with a bi-infinite tape. The program parses a configuration file containing the machine's total states, alphabet size, transition functions, and an initial input string. It then processes the input string according to the defined rules, moving the read/write head and modifying the tape contents until it reaches the final halting state, at which point it prints the contents of all visited tape cells.

## Reflection

Developing this Turing Machine simulator presented an interesting challenge, particularly when it came to conceptualizing and implementing the bi-infinite tape. Initially, mapping a tape that can expand infinitely in both the left and right directions using standard array structures seemed daunting. However, leveraging a `HashMap` where the keys represent the position indices on the tape and the values hold the symbols proved to be an effective solution, allowing the head to seamlessly transition into negative indices without encountering boundary errors. 

One of the main struggles during development was accurately parsing the transition rules and ensuring they mapped correctly to the intended states and symbols, especially calculating the origin state and symbol from the line index. Debugging this required a careful approach. We utilized multiple print statements during the parsing phase to verify that the internal `TMState` arrays were populating correctly before even attempting to run the simulation loop. 

If I could go back in time, I would spend more time manually tracing the logic of the provided test cases on paper before writing the simulation loop. Visualizing the state transitions and head movements outside of the code would have made it easier to implement the tracking for the furthest left and right visited boundaries. Going forward, I plan to incorporate more upfront conceptual design into my workflow before diving straight into the implementation to ensure clearer logic from the start.

## Compiling and Using

To compile, open a console, navigate to the main project directory containing the `tm` source folder, and execute the following command:

$ javac tm/*.java

Run the compiled class by providing the input configuration file as a single command-line argument:

$ java tm.TMSimulator <input_file.txt>

For example, to run the program using `file0.txt`:

$ java tm.TMSimulator file0.txt

The program requires no further user input while running. It will automatically process the provided file and output the final tape sequence to the standard output.

## Sources used

- Oracle Java Documentation (HashMap, String parsing, I/O handling): https://docs.oracle.com/en/java/javase/