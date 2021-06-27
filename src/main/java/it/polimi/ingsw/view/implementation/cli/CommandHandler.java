package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Handler for the cli commands, it has the task to recognize input from user and call the right function,
 * related to the player move.
 */

public class CommandHandler {
    private final CLI cli;

    /**
     * Creates a new CommandHandler for the given CLI.
     * @param cli the cli to be associated to this CommandHandler
     */
    public CommandHandler(CLI cli) {
        this.cli = cli;
    }

    /**
     * The main method. It parses the input, checking if it corresponds to a possible move, and in this case it calls it.
     * @param command user input in the CLI
     * @throws IllegalArgumentException if the input does not match with any possible player action
     */
    public void handle(String command) throws IllegalArgumentException {
        if (command == null)
            throw new IllegalArgumentException("Command can't be null");
        if (command.trim().equals(""))
            throw new IllegalArgumentException("Command can't be empty");

        String[] split = command.split(" ");
        String cmd = split[0];
        String[] args = new String[0];
        if (cmd.equals("view")) {
            split[1] = split[1].substring(0, 1).toUpperCase(Locale.ROOT) + split[1].substring(1);
            cmd = cmd.concat(split[1]);
            args = null;
        } else if (split.length > 1)
            args = Arrays.copyOfRange(split, 1, split.length);

        try {
            Method cmdHandler;
            if (args != null) {
                cmdHandler = getClass().getMethod(cmd, args.getClass());
                cmdHandler.invoke(this, (Object) args);
            } else {
                cmdHandler = getClass().getMethod(cmd);
                cmdHandler.invoke(this);
            }
        } catch (NoSuchMethodException | SecurityException |
                IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException("Command does not exists.");
        }
    }

    /**
     * Calls the method to make the player see his leader cards.
     */
    public void viewLeaders() {
        cli.getRenderer().printOwnLeaders();
    }

    /**
     * Calls the method to make the player see his development cards.
     */
    public void viewDevelopment() {
        cli.getRenderer().printOwnDevelopmentCards();
    }

    /**
     * Calls the method to make the player see the deck.
     */
    public void viewDeck() {
        cli.getRenderer().printDevelopmentDeck();
    }

    /**
     * Calls the method to make the player see his deposit.
     */
    public void viewDeposit() {
        cli.getRenderer().printOwnDeposit();
    }

    /**
     * Calls the method to make the player see his strongbox.
     */
    public void viewStrongbox() {
        cli.getRenderer().printOwnStrongbox();
    }

    /**
     * Calls the method to make the player see the market.
     */
    public void viewMarket() {
        cli.getRenderer().printMarket();
    }

    /**
     * Calls the method to make the player see what he drew from the market.
     */
    public void viewResult() {
        cli.getRenderer().printMarketResult();
    }

    /**
     * Calls the methods to make the player see his faith and pope favours.
     */
    public void viewFaith() {
        cli.getRenderer().printFaith(cli.getModel().getLocalPlayer().getFaithPoints());
        cli.getRenderer().printFavours(cli.getModel().getLocalPlayer().getPopeFavours());
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to send a "buy" player action event.
     * @param args the decomposed user command
     */
    public void buy(String[] args) {
        int cardIndex;
        try {
            cardIndex = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.BUY_CARD);
            return;
        }
        int slotIndex;
        try {
            slotIndex = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.BUY_CARD);
            return;
        }
        if (cardIndex < 1 || cardIndex > 12 || slotIndex < 1 || slotIndex > 3) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.BUY_CARD);
            return;
        }
        cli.getActionSender().buyDevelopmentCard(cardIndex, slotIndex);
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to send a "draw" action event.
     * @param args the decomposed user command
     */
    public void draw(String[] args) {
        int marketIndex;
        try {
            marketIndex = Integer.parseInt(args[0]);
            if (marketIndex < 1 || marketIndex > 7) throw new IllegalArgumentException("incorrect_format");
        } catch (IllegalArgumentException e) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.DRAW_MARKET);
            return;
        }
        int whiteResources = cli.getModel().getMarket().countWhiteResources(marketIndex);
        List<Resource> whiteConversions = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            if (whiteConversions.size() <= whiteResources) {
                try {
                    Resource res = Resource.valueOf(args[i].toUpperCase());
                    whiteConversions.add(res);
                } catch (IllegalArgumentException e) {
                    cli.getRenderer().showErrorMessage(args[i] + " is not a valid resource");
                    return;
                }
            }
        }
        cli.getActionSender().draw(marketIndex, whiteConversions);
    }

    /**
     * Checks if the arguments are correct and then calls for the methods to spy other players' boards.
     * @param args the decomposed user command
     */
    public void spy(String[] args) {
        if (args.length < 2) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.SPY);
        }
        String playerName = args[0];
        String object = args[1];

        switch (object) {
            case "leaders" -> cli.getRenderer().printOthersLeaderCards(playerName);
            case "development" -> cli.getRenderer().printOthersDevelopmentCards(playerName);
            case "deposit" -> cli.getRenderer().printOthersDeposit(playerName);
            case "faith" -> {
                cli.getRenderer().printOthersFaith(playerName);
                cli.getRenderer().printOthersFavours(playerName);
            }
        }
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to send a "discard" action event.
     * @param args the decomposed user command
     */
    public void discard(String[] args) {
        if (args.length < 1) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.DISCARD);
        }
        int index;
        try {
            index = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.DISCARD);
            return;
        }
        if (index > cli.getModel().getLocalPlayer().getLeaderCards().size()) {
            cli.getRenderer().showErrorMessage("Index out of bound");
            return;
        }
        cli.getActionSender().discard(index);
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to send a "move" action event.
     * @param args the decomposed user command
     */
    public void move(String[] args) {
        if (args.length < 2) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.MOVE_DEPOSIT);
        }
        int[] index = new int[2];
        for (int j = 0; j < 2; j++) {
            try {
                index[j] = Integer.parseInt(args[j]);
            } catch (NumberFormatException e) {
                System.out.println(ViewString.INCORRECT_FORMAT + ViewString.MOVE_DEPOSIT);
                return;
            }
            if (index[j] < 1 || index[j] > 5) {
                cli.getRenderer().showErrorMessage("Index out of bound");
                return;
            } else if (index[j] == 5 && !cli.getModel().getLocalPlayer().hasTwoLeaderDeposits()) {
                cli.getRenderer().showErrorMessage(ViewString.TWO_LEADER_DEPOSITS_REQUIRED);
                return;
            }
        }
        cli.getActionSender().move(index[0], index[1]);
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to send a "store" action event.
     * @param args the decomposed user command
     */
    public void store(String[] args) {
        if (args.length < 2) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.STORE_DEPOSIT);
        }
        int[] index = new int[2];
        for (int j = 0; j < 2; j++) {
            try {
                index[j] = Integer.parseInt(args[j]);
            } catch (NumberFormatException e) {
                System.out.println(ViewString.INCORRECT_FORMAT + ViewString.STORE_DEPOSIT);
                return;
            }
        }
        if (index[0] < 1 || index[0] > cli.getModel().getLocalPlayer().getDeposit().getMarketResult().size()) {
            cli.getRenderer().showErrorMessage("Market result index out of bound");
            return;
        }
        if (index[1] < 1 || index[1] > 5) {
            cli.getRenderer().showErrorMessage("Row index out of bound");
            return;
        } else if (index[1] == 5 && !cli.getModel().getLocalPlayer().hasTwoLeaderDeposits()) {
            cli.getRenderer().showErrorMessage(ViewString.TWO_LEADER_DEPOSITS_REQUIRED);
            return;
        }

        cli.getActionSender().storeMarketResult(index[0], index[1]);
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to send an "end turn" action event.
     * @param args the decomposed user command
     */
    public void endturn(String[] args) {
        cli.getActionSender().endTurn();
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to send an "activate" action event.
     * @param args the decomposed user command
     */
    public void activate(String[] args) {
        if (args.length < 1) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.ACTIVATE_LEADER);
        }
        int index;
        try {
            index = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.ACTIVATE_LEADER);
            return;
        }
        if (index < 1 || index > 2) {
            cli.getRenderer().showErrorMessage("Index out of bound");
            return;
        }
        cli.getActionSender().setActive(index);
    }

    /**
     * Calls the method to make the player see all the possible commands.
     */
    public void help(String[] args) {
        cli.getRenderer().help();
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to add a pending production.
     * @param args the decomposed user command
     */
    public void production(String[] args) {
        if (args.length < 1) {
            cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_PRODUCTION);
            return;
        }
        switch (args[0]) {
            case "leader" -> {
                if (args.length != 3) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_LEADER_PRODUCTION);
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_LEADER_PRODUCTION);
                    return;
                }
                if (index > 2 || index < 1) {
                    cli.getRenderer().showErrorMessage("Index out of bounds");
                    return;
                }
                Resource toReceive;
                try {
                    toReceive = Resource.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    cli.getRenderer().showErrorMessage("Not a valid resource");
                    return;
                }

                LeaderCard leaderCard = cli.getModel().getLocalPlayer().getLeaderCardAt(index - 1);
                cli.getActionSender().useLeaderProduction(leaderCard, toReceive);
            }
            case "development" -> {
                if (args.length != 2) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_DEVELOPMENT_PRODUCTION);
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_DEVELOPMENT_PRODUCTION);
                    return;
                }
                if (index > 3 || index < 1) {
                    cli.getRenderer().showErrorMessage("Index out of bounds");
                    return;
                }

                DevelopmentCard developmentCard = cli.getModel().getLocalPlayer().getPlayerBoard()
                        .getTopDevelopmentCardAt(index - 1);
                cli.getActionSender().useDevelopmentProduction(developmentCard);
            }
            case "base" -> {
                List<Resource> expectedInput = cli.getModel().getGameConfig().getBaseProductionPower().getInput();
                List<Resource> expectedOutput = cli.getModel().getGameConfig().getBaseProductionPower().getOutput();
                int numOfArgs = 1 + expectedInput.size() + expectedOutput.size();
                if (args.length != numOfArgs) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_BASE_PRODUCTION);
                    return;
                }
                List<Resource> inputResources = new ArrayList<>();
                for (int i = 1; i <= expectedInput.size(); i++) {
                    try {
                        Resource resource = Resource.valueOf(args[i].toUpperCase());
                        inputResources.add(resource);
                    } catch (IllegalArgumentException e) {
                        cli.getRenderer().showErrorMessage("Input resource " + i + " is not a valid resource");
                        return;
                    }
                }
                List<Resource> outputResources = new ArrayList<>();
                for (int i = expectedInput.size() + 1; i < numOfArgs; i++) {
                    try {
                        outputResources.add(Resource.valueOf(args[i].toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        cli.getRenderer().showErrorMessage("Output resource " + i + " is not a valid resource");
                        return;
                    }
                }

                cli.getActionSender().useBaseProduction(inputResources, outputResources);
            }
        }
    }

    /**
     * Checks if the arguments are correct and then calls for the action sender to send a "production" action event.
     * @param args the decomposed user command
     */
    public void execute(String[] args) {
        if (!cli.isUsingProductions()) {
            cli.getRenderer().showErrorMessage("You are not using productions");
            return;
        }
        cli.getActionSender().executeProductions();
    }

    /**
     * Calls for the action sender to issue a new chat from the player.
     * @param splitMessage the split message
     */
    public void chat(String[] splitMessage) {
        String message = String.join(" ", splitMessage);
        cli.getActionSender().sendChatMessage(message);
    }
}
