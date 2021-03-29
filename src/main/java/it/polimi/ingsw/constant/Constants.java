package it.polimi.ingsw.constant;

import it.polimi.ingsw.model.Resource;

import java.util.List;

public class Constants {
    public static final String MASTER = "\n" +
            " __       __                        __                                           ______         _______                                 __                                                            \n" +
            "/  \\     /  |                      /  |                                         /      \\       /       \\                               /  |                                                           \n" +
            "$$  \\   /$$ |  ______    _______  _$$ |_     ______    ______          ______  /$$$$$$  |      $$$$$$$  |  ______   _______    ______  $$/   _______  _______   ______   _______    _______   ______  \n" +
            "$$$  \\ /$$$ | /      \\  /       |/ $$   |   /      \\  /      \\        /      \\ $$ |_ $$/       $$ |__$$ | /      \\ /       \\  /      \\ /  | /       |/       | /      \\ /       \\  /       | /      \\ \n" +
            "$$$$  /$$$$ | $$$$$$  |/$$$$$$$/ $$$$$$/   /$$$$$$  |/$$$$$$  |      /$$$$$$  |$$   |          $$    $$< /$$$$$$  |$$$$$$$  | $$$$$$  |$$ |/$$$$$$$//$$$$$$$/  $$$$$$  |$$$$$$$  |/$$$$$$$/ /$$$$$$  |\n" +
            "$$ $$ $$/$$ | /    $$ |$$      \\   $$ | __ $$    $$ |$$ |  $$/       $$ |  $$ |$$$$/           $$$$$$$  |$$    $$ |$$ |  $$ | /    $$ |$$ |$$      \\$$      \\  /    $$ |$$ |  $$ |$$ |      $$    $$ |\n" +
            "$$ |$$$/ $$ |/$$$$$$$ | $$$$$$  |  $$ |/  |$$$$$$$$/ $$ |            $$ \\__$$ |$$ |            $$ |  $$ |$$$$$$$$/ $$ |  $$ |/$$$$$$$ |$$ | $$$$$$  |$$$$$$  |/$$$$$$$ |$$ |  $$ |$$ \\_____ $$$$$$$$/ \n" +
            "$$ | $/  $$ |$$    $$ |/     $$/   $$  $$/ $$       |$$ |            $$    $$/ $$ |            $$ |  $$ |$$       |$$ |  $$ |$$    $$ |$$ |/     $$//     $$/ $$    $$ |$$ |  $$ |$$       |$$       |\n" +
            "$$/      $$/  $$$$$$$/ $$$$$$$/     $$$$/   $$$$$$$/ $$/              $$$$$$/  $$/             $$/   $$/  $$$$$$$/ $$/   $$/  $$$$$$$/ $$/ $$$$$$$/ $$$$$$$/   $$$$$$$/ $$/   $$/  $$$$$$$/  $$$$$$$/ \n" +
            "                                                                                                                                                                                                      \n" +
            "                                                                                                                                                                                                      \n" +
            "                                                                                                                                                                                                      \n";

    public static final String MARKET = "-----------------------------------------\n" +
            "|  / * \\  |  / * \\  |  / * \\  |  / * \\  |\n" +
            "| *     * | *     * | *     * | *     * |\n" +
            "|  \\ * /  |  \\ * /  |  \\ * /  |  \\ * /  |\n" +
            "-----------------------------------------\n" +
            "|  / * \\  |  / * \\  |  / * \\  |  / * \\  |\n" +
            "| *     * | *     * | *     * | *     * |\n" +
            "|  \\ * /  |  \\ * /  |  \\ * /  |  \\ * /  |\n" +
            "-----------------------------------------\n" +
            "|  / * \\  |  / * \\  |  / * \\  |  / * \\  |\n" +
            "| *     * | *     * | *     * | *     * |\n" +
            "|  \\ * /  |  \\ * /  |  \\ * /  |  \\ * /  |\n" +
            "-----------------------------------------";

    public static final String SIDEARROW = " <---";

    public static final String BOTTOMARROWS = "   ^ ^ ^     ^ ^ ^     ^ ^ ^     ^ ^ ^   \n" +
            "   | | |     | | |     | | |     | | |   \n" +
            "   | | |     | | |     | | |     | | |   ";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
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
        switch (res) {
            case STONE:
                return ANSI_GREY;
            case SERVANT:
                return ANSI_PURPLE;
            case SHIELD:
                return ANSI_BRIGHT_BLUE;
            case COIN:
                return ANSI_YELLOW;
            case FAITH:
                return ANSI_RED;
        }
        return "";
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
            market = market.concat(SIDEARROW + "\n");
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
        return market;
    }
}
