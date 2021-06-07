package it.polimi.ingsw.constant;

import it.polimi.ingsw.model.Resource;

public abstract class AnsiColor {
    private static final String START_ITALICIZE = "\033[3m";
    private static final String START_BOLD = "\033[1m";

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String GREY = "\u001B[90m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_MAGENTA = "\u001B[95m";

    public static String getResourceColor(Resource resource) {
        if (resource == null) return WHITE;
        return switch (resource) {
            case STONE -> GREY;
            case SERVANT -> PURPLE;
            case SHIELD -> BRIGHT_BLUE;
            case COIN -> YELLOW;
            case FAITH -> RED;
        };
    }

    public static String italicize(String message) {
        return START_ITALICIZE + message + RESET;
    }

    public static String bold(String message) {
        return START_BOLD + message + RESET;
    }
}
