package it.polimi.ingsw.constant;

import it.polimi.ingsw.model.Resource;

import java.util.List;

public class Constants {
    public static final String MASTER = """
             __       __                        __                                                     ______         _______                                 __                                                           \s
            /  \\     /  |                      /  |                                                   /      \\       /       \\                               /  |                                                          \s
            $$  \\   /$$ |  ______    _______  _$$ |_     ______    ______    _______         ______  /$$$$$$  |      $$$$$$$  |  ______   _______    ______  $$/   _______  _______   ______   _______    _______   ______ \s
            $$$  \\ /$$$ | /      \\  /       |/ $$   |   /      \\  /      \\  /       |       /      \\ $$ |_ $$/       $$ |__$$ | /      \\ /       \\  /      \\ /  | /       |/       | /      \\ /       \\  /       | /      \\\s
            $$$$  /$$$$ | $$$$$$  |/$$$$$$$/ $$$$$$/   /$$$$$$  |/$$$$$$  |/$$$$$$$/       /$$$$$$  |$$   |          $$    $$< /$$$$$$  |$$$$$$$  | $$$$$$  |$$ |/$$$$$$$//$$$$$$$/  $$$$$$  |$$$$$$$  |/$$$$$$$/ /$$$$$$  |
            $$ $$ $$/$$ | /    $$ |$$      \\   $$ | __ $$    $$ |$$ |  $$/ $$      \\       $$ |  $$ |$$$$/           $$$$$$$  |$$    $$ |$$ |  $$ | /    $$ |$$ |$$      \\$$      \\  /    $$ |$$ |  $$ |$$ |      $$    $$ |
            $$ |$$$/ $$ |/$$$$$$$ | $$$$$$  |  $$ |/  |$$$$$$$$/ $$ |       $$$$$$  |      $$ \\__$$ |$$ |            $$ |  $$ |$$$$$$$$/ $$ |  $$ |/$$$$$$$ |$$ | $$$$$$  |$$$$$$  |/$$$$$$$ |$$ |  $$ |$$ \\_____ $$$$$$$$/\s
            $$ | $/  $$ |$$    $$ |/     $$/   $$  $$/ $$       |$$ |      /     $$/       $$    $$/ $$ |            $$ |  $$ |$$       |$$ |  $$ |$$    $$ |$$ |/     $$//     $$/ $$    $$ |$$ |  $$ |$$       |$$       |
            $$/      $$/  $$$$$$$/ $$$$$$$/     $$$$/   $$$$$$$/ $$/       $$$$$$$/         $$$$$$/  $$/             $$/   $$/  $$$$$$$/ $$/   $$/  $$$$$$$/ $$/ $$$$$$$/ $$$$$$$/   $$$$$$$/ $$/   $$/  $$$$$$$/  $$$$$$$/\s
                                                                                                                                                                                                                           \s
                                                                                                                                                                                                                           \s
                                                                                                                                                                                                                           \s""";

    public static final String MARKET = """
            -----------------------------------------
            |  / * \\  |  / * \\  |  / * \\  |  / * \\  |
            | *     * | *     * | *     * | *     * |
            |  \\ * /  |  \\ * /  |  \\ * /  |  \\ * /  |
            -----------------------------------------
            |  / * \\  |  / * \\  |  / * \\  |  / * \\  |
            | *     * | *     * | *     * | *     * |
            |  \\ * /  |  \\ * /  |  \\ * /  |  \\ * /  |
            -----------------------------------------
            |  / * \\  |  / * \\  |  / * \\  |  / * \\  |
            | *     * | *     * | *     * | *     * |
            |  \\ * /  |  \\ * /  |  \\ * /  |  \\ * /  |
            -----------------------------------------""";

    public static final String SIDEARROW = " <---";

    public static final String BOTTOMARROWS = """
            ^ ^ ^     ^ ^ ^     ^ ^ ^     ^ ^ ^  \s
            | | |     | | |     | | |     | | |  \s
            | | |     | | |     | | |     | | |  \s""".indent(3);

    public static final String BOTTOMINDEX = "     1         2         3         4";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GREY = "\u001B[90m";
    public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";

    public static String parseResource(Resource res) {
        if (res == null) return ANSI_WHITE;
        return switch (res) {
            case STONE -> ANSI_GREY;
            case SERVANT -> ANSI_PURPLE;
            case SHIELD -> ANSI_BRIGHT_BLUE;
            case COIN -> ANSI_YELLOW;
            case FAITH -> ANSI_RED;
        };
    }

    public static String buildMarket(List<List<Resource>> rows) {
        String market = "";
        market = market.concat("-----------------------------------------\n");
        for (int r = 0; r < 3; r++) {
            for (int i = 0; i < 4; i++) {
                String ANSIcolor = parseResource(rows.get(r).get(i));
                market = market.concat("|  ");
                market = market.concat(ANSIcolor + "/ * \\  " + Constants.ANSI_RESET);
            }
            market = market.concat("|");
            market = market.concat(SIDEARROW + "\n");
            for (int i = 0; i < 4; i++) {
                String ANSIcolor = parseResource(rows.get(r).get(i));
                market = market.concat("| ");
                market = market.concat(ANSIcolor + "*     * " + Constants.ANSI_RESET);
            }
            market = market.concat("|");
            market = market.concat(SIDEARROW + (r+5) + "\n");
            for (int i = 0; i < 4; i++) {
                String ANSIcolor = parseResource(rows.get(r).get(i));
                market = market.concat("|  ");
                market = market.concat(ANSIcolor + "\\ * /  " + Constants.ANSI_RESET);
            }
            market = market.concat("|");
            market = market.concat(SIDEARROW + "\n");
            market = market.concat("-----------------------------------------\n");
        }
        market = market.concat(BOTTOMARROWS);
        market = market.concat(BOTTOMINDEX);
        return market;
    }
}
