package io.jasonyu.cambia.util;

public class PrettyPrinter {

    /**
     * Throw clear screen ANSI code followed by HOME, then flush the output stream.
     *
     * Designed to operate on a traditional bash terminal (will not work in eclipse, intellij, etc.)
     * For the purposes of this project it functions to remove sensitive information from the terminal when needed.
     */
    public static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
}
