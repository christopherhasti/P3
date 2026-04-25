# Project 3: Turing Machine Simulator

* Author: Christopher Hastings and Daniel Trice
* Class: CS 361
* Semester: Spring 2026

## Overview

This Java application provides a highly optimized simulator for a deterministic Turing Machine equipped with a bi-infinite tape. The program strictly parses a configuration file determining the states, alphabet size, transition functions, and input string. It processes the instructions, moving a simulated read/write head, and halts when it reaches the final state, effectively printing the continuous sequence of all visited tape cells.

## Reflection

Developing this Turing Machine simulator required a significant pivot in how we handled data structures. Initially, a standard `HashMap` seemed like the most logical way to map a tape that expands infinitely into negative and positive directions. However, we realized that object wrapper overhead (autoboxing `Long` and `Integer` keys/values) would slow down execution significantly for millions of transitions, posing a real threat to the 5-minute timeout requirement.

To solve this, we pivoted to a primitive-based architecture. The bi-infinite tape was reimagined as two dynamically resizing `int[]` arrays (one for positive indices, one for negative). This completely eliminated garbage collection overhead in the execution loop. We also optimized `TMState` to use arrays instead of Maps for $O(1)$ instantaneous transition lookups. 

The main struggle was ensuring the file parser read *exactly* the correct number of transition lines so that it wouldn't accidentally consume the input string on the final line if the file lacked an `EOF` marker. If we could go back in time, we would remind ourselves that reading file specifications strictly mathematically (e.g., exactly `(States - 1) * Gamma` lines) is safer than relying on arbitrary file termination loops.

## Compiling and Using

To compile the application, navigate to the project directory containing the `tm` source folder, and execute:

$ javac tm/*.java

Run the compiled class by providing the input configuration file as a single command-line argument:

$ java tm.TMSimulator <input_file>

For example, to simulate `file0.txt`:

$ java tm.TMSimulator file0.txt

The program will execute the transitions and directly output the final visited tape segments to the standard console.

## Sources used

- Oracle Java Documentation (Arrays, String parsing, BufferedReader): https://docs.oracle.com/en/java/javase/