package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.AnsiColor;
import it.polimi.ingsw.constant.AnsiSymbol;
import it.polimi.ingsw.constant.Constants;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.Renderer;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.beans.MockPlayer;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the Renderer for the CLI.
 */

public class CLIRenderer extends Renderer {

    /**
     * Creates a new CLIRenderer for the given View.
     * @param view the cli to be associated with this CLIRenderer
     */
    protected CLIRenderer(View view) {
        super(view);
    }

    /**
     * Shows the game's message.
     *
     * @param message the game's message to show
     */
    @Override
    public void showGameMessage(String message) {
        System.out.println(AnsiColor.BLUE + "[" + AnsiColor.italicize("TO:You") + AnsiColor.BLUE + "] " + message + AnsiColor.RESET);
    }

    /**
     * Shows the lobby's message.
     *
     * @param message the lobby's message to show
     */
    @Override
    public void showLobbyMessage(String message) {
        System.out.println(AnsiColor.GREEN + "[" + AnsiColor.italicize("TO:Lobby") + AnsiColor.GREEN + "] " + message + AnsiColor.RESET);
    }

    /**
     * Shows the error message.
     *
     * @param message the error message to show.
     */
    @Override
    public void showErrorMessage(String message) {
        System.out.println(AnsiColor.RED + "[" + AnsiColor.italicize("ERROR") + AnsiColor.RED + "] " + message + AnsiColor.RESET);
    }

    /**
     * Prints the market.
     */
    @Override
    public void printMarket() {
        renderMarket(getView().getModel().getMarket().getGrid(), getView().getModel().getMarket().getSlideResource());
    }

    /**
     * Prints the LeaderCards of the player.
     */
    @Override
    public void printOwnLeaders() {
        int index = 1;
        for (LeaderCard card : getView().getModel().getLocalPlayer().getLeaderCards().keySet()) {
            getView().getRenderer().renderLeaderCard(card, index);
            index++;
        }
    }

    /**
     * Prints the DevelopmentCards of the player.
     */
    @Override
    public void printOwnDevelopmentCards() {
        if (!getView().getModel().getLocalPlayer().getPlayerBoard().hasOwnDevelopmentCard()) {
            System.out.println("You don't have any development card");
            return;
        }
        List<ObservableList<DevelopmentCard>> targetCards = getView().getModel().getLocalPlayer().getPlayerBoard().getDevelopmentCards();
        int index = 1;
        for (ObservableList<DevelopmentCard> stack : targetCards) {
            for (DevelopmentCard card : stack) {
                if (stack.get(stack.size() - 1) == card) {
                    System.out.println(AnsiColor.BRIGHT_BLUE + "USABLE CARD" + AnsiColor.RESET);
                }
                renderDevelopmentCard(card, index);
                index++;
            }
        }
    }

    /**
     * Prints the player's deposit.
     */
    @Override
    public void printOwnDeposit() {
        renderDeposit(getView().getModel().getLocalPlayer());
        renderLeadersDeposit(getView().getModel().getLocalPlayer());
    }

    /**
     * Prints the player's StrongBox.
     */
    @Override
    public void printOwnStrongbox() {
        renderStrongbox(getView().getModel().getLocalPlayer());
    }

    @Override
    public void printOthersLeaderCards(String playerName) {
        MockPlayer otherPlayer = getView().getModel().getPlayer(playerName);
        if (otherPlayer == null) {
            System.err.println(playerName + " not found in local model!");
            return;
        }

        int numActive = 0;
        Map<LeaderCard, Boolean> targetCards = otherPlayer.getLeaderCards();
        for (LeaderCard card : targetCards.keySet()) {
            if (targetCards.get(card)) {
                numActive++;
                renderLeaderCard(card, -1);
            }
        }
        if (numActive == 0) getView().getRenderer().showGameMessage("Player does not have active cards!");
    }

    /**
     * Prints the development cards of the given player.
     *
     * @param playerName the name of the player whose development cards to print
     */
    @Override
    public void printOthersDevelopmentCards(String playerName) {
        MockPlayer otherPlayer = getView().getModel().getPlayer(playerName);
        if (otherPlayer == null) {
            System.err.println(playerName + " not found in local model!");
            return;
        }

        List<ObservableList<DevelopmentCard>> targetCards = otherPlayer.getPlayerBoard().getDevelopmentCards();
        for (ObservableList<DevelopmentCard> stack : targetCards) {
            for (DevelopmentCard card : stack) {
                if (stack.get(stack.size() - 1) == card) {
                    System.out.println(AnsiColor.BRIGHT_BLUE + "USABLE CARD" + AnsiColor.RESET);
                }
                renderDevelopmentCard(card, -1);
            }
        }
    }

    /**
     * Prints the deposit of the given player.
     *
     * @param playerName the name of the player whose deposit to print
     */
    @Override
    public void printOthersDeposit(String playerName) {
        MockPlayer otherPlayer = getView().getModel().getPlayer(playerName);
        if (otherPlayer == null) {
            System.err.println(playerName + " not found in local model!");
            return;
        }

        renderDeposit(otherPlayer);
        renderLeadersDeposit(otherPlayer);
    }

    /**
     * Prints the faith points of the given player.
     *
     * @param playerName the name of the player whose faith to print
     */
    @Override
    public void printOthersFaith(String playerName) {
        MockPlayer otherPlayer = getView().getModel().getPlayer(playerName);
        if (otherPlayer == null) {
            System.err.println(playerName + " not found in local model!");
            return;
        }

        printFaith(otherPlayer.getFaithPoints());
    }

    /**
     * Prints the cumulated pope favours of the given player.
     *
     * @param playerName the name of the player whose favours to print
     */
    @Override
    public void printOthersFavours(String playerName) {
        MockPlayer otherPlayer = getView().getModel().getPlayer(playerName);
        if (otherPlayer == null) {
            System.err.println(playerName + " not found in local model!");
            return;
        }

        printFavours(otherPlayer.getFaithPoints());
    }

    /**
     * Prints the development deck with all its development cards as a stack.
     */
    @Override
    public void printDevelopmentDeck() {
        List<HashMap<CardColor, ObservableList<DevelopmentCard>>> deck = getView().getModel().getDevelopmentDeck();
        int index = 1;
        for (HashMap<CardColor, ObservableList<DevelopmentCard>> map : deck) {
            for (ObservableList<DevelopmentCard> stack : map.values()) {
                if (!stack.isEmpty()) renderDevelopmentCard(stack.get(stack.size() - 1), index);
                index++;
            }
        }
    }

    /**
     * Prints the deposit of the player. The deposit is made up of: top row, middle row and bottom row.
     *
     * @param player the player whose deposit to print
     */
    @Override
    public void renderDeposit(MockPlayer player) {
        List<List<Resource>> deposit = player.getDeposit().getAllRows();
        Resource res;
        System.out.print("    ");
        for (int i = 0; i < deposit.size(); i++) {
            for (int j = 0; j < deposit.get(i).size(); j++) {
                res = deposit.get(i).get(0);
                System.out.print("|");
                System.out.print(renderResource(res));
                System.out.print("|");
            }
            for (int k = 0; k < i + 1 - deposit.get(i).size(); k++) {
                System.out.print("|");
                System.out.print(AnsiSymbol.EMPTY);
                System.out.print("|");
            }
            System.out.println();
            if (i == 0) System.out.print("  ");
        }
    }

    /**
     * Shows the leader deposits if the player has an active leader with such special ability.
     *
     * @param player the player whose leader deposits to print
     */

    @Override
    public void renderLeadersDeposit(MockPlayer player) {
        Map<Integer, List<Resource>> leadersDeposit = player.getDeposit().getActiveLeadersDeposit();
        if (leadersDeposit.size() == 0) return;
        System.out.println(AnsiColor.CYAN + "-- LEADERS DEPOSITS --" + AnsiColor.RESET);
        for (List<Resource> deposit : leadersDeposit.values()) {
            if (deposit.size() > 0) {
                Resource res = deposit.get(0);
                System.out.println(renderResource(res) + " " + AnsiColor.getResourceColor(res) + res + AnsiColor.RESET + ": " + deposit.size());
            } else System.out.println("Empty deposit slot");
        }
    }

    /**
     * Prints the strongbox of the player.
     *
     * @param player the player whose strongbox to print.
     */
    @Override
    public void renderStrongbox(MockPlayer player) {
        ObservableMap<Resource, Integer> strongbox = player.getDeposit().strongBoxProperty();
        for (Resource res : strongbox.keySet()) {
            String strongboxRow = "";
            strongboxRow = strongboxRow.concat(renderResource(res) + " " + AnsiColor.getResourceColor(res) + res + AnsiColor.RESET + ": " + strongbox.get(res));
            System.out.println(strongboxRow);
        }
    }

    /**
     * Prints the formatted version of th given resource.
     *
     * @param res the resource to print.
     * @return a String showing the resource given
     */
    @Override
    public String renderResource(Resource res) {
        if (res == null)
            return "WHITE " + AnsiSymbol.EMPTY;
        switch (res) {
            case COIN -> {
                return AnsiColor.getResourceColor(res) + AnsiSymbol.COIN + AnsiColor.RESET;
            }
            case SERVANT -> {
                return AnsiColor.getResourceColor(res) + AnsiSymbol.SERVANT + AnsiColor.RESET;
            }
            case FAITH -> {
                return AnsiColor.getResourceColor(res) + AnsiSymbol.FAITH + AnsiColor.RESET;
            }
            case SHIELD -> {
                return AnsiColor.getResourceColor(res) + AnsiSymbol.SHIELD + AnsiColor.RESET;
            }
            case STONE -> {
                return AnsiColor.getResourceColor(res) + AnsiSymbol.STONE + AnsiColor.RESET;
            }
        }
        return "";
    }

    /**
     * Prints all the possible commands that the player can type in the CLI.
     */
    @Override
    public void help() {
        int index = 1;
        for (String command : ViewString.getCommands()) {
            System.out.println(index + ") " + AnsiColor.GREEN + command + AnsiColor.RESET);
            index++;
        }
    }

    /**
     * Prints the Faith Track.
     *
     * @param faith the number of faith points of the player
     */
    @Override
    public void printFaith(int faith) {
        StringBuilder str = new StringBuilder("[ ");
        StringBuilder popeReports = new StringBuilder("  ");
        StringBuilder points = new StringBuilder("  ");
        int pointsToWin = 24 - faith;
        float percentage = (float) faith / 24 * 100;

        for (int i = 0; i < faith; i++) {
            str.append(AnsiColor.PURPLE + AnsiSymbol.CROSS + AnsiColor.RESET);
            str.append(" ");
        }
        for (int i = 0; i < pointsToWin; i++) {
            str.append(AnsiColor.RED + "-" + AnsiColor.RESET);
            str.append(" ");
        }
        str.append("] ");
        for (int i = 0; i < 24; i++) {
            if (i == 7 || i == 15 || i == 23) {
                popeReports.append(AnsiColor.YELLOW + AnsiSymbol.BISHOP + AnsiColor.RESET);
            } else if (i == 22 || i == 21 || i == 20 || i == 19 ||
                    i == 18 || i == 14 || i == 13 || i == 12 ||
                    i == 11 || i == 6 || i == 5 || i == 4) {
                popeReports.append(AnsiColor.GREEN + "+" + AnsiColor.RESET);
            } else {
                popeReports.append("x");
            }
            popeReports.append(" ");
        }
        for (int i = 0; i < 24; i++) {
            if (i == 2) {
                points.append(AnsiColor.BRIGHT_BLUE + "1" + AnsiColor.RESET);
            } else if (i == 5) {
                points.append(AnsiColor.BRIGHT_BLUE + "2" + AnsiColor.RESET);
            } else if (i == 8) {
                points.append(AnsiColor.BRIGHT_BLUE + "4" + AnsiColor.RESET);
            } else if (i == 11) {
                points.append(AnsiColor.BRIGHT_BLUE + "6" + AnsiColor.RESET);
            } else if (i == 14) {
                points.append(AnsiColor.BRIGHT_BLUE + "9" + AnsiColor.RESET);
            } else if (i == 17) {
                points.append(AnsiColor.BRIGHT_BLUE + "12" + AnsiColor.RESET);
            } else if (i == 20) {
                points.append(AnsiColor.BRIGHT_BLUE + "16" + AnsiColor.RESET);
            } else if (i == 23) {
                points.append(AnsiColor.BRIGHT_BLUE + "20" + AnsiColor.RESET);
            } else {
                points.append(" ");
            }
            points.append(" ");
        }

        System.out.println("Collected " + faith + " faith points.");
        System.out.println(str);
        System.out.println(popeReports);
        System.out.println(points);
        System.out.printf(("Completion %.2f%% %n"), percentage);
        System.out.println("Other " + pointsToWin + " needed to win.");


    }

    /**
     * Prints the cumulated favours of a player.
     *
     * @param popeFavours the number of pope favours of the player
     */
    @Override
    public void printFavours(int popeFavours) {
        System.out.println("Amount of cumulated pope favours: " + AnsiColor.YELLOW + popeFavours + AnsiColor.RESET);
    }

    /**
     * Prints a message showing the resources to store after a draw.
     */

    @Override
    public void printMarketResult() {
        List<Resource> marketResult = getView().getModel().getLocalPlayer().getDeposit().getMarketResult();
        if (marketResult.size() > 0) {
            String printedResult = "";
            printedResult = printedResult.concat("The following resources are waiting to be stored: - ");

            int index = 1;
            for (Resource res : marketResult) {
                printedResult = printedResult.concat(" " + index + ") " + AnsiColor.getResourceColor(res) + res + AnsiColor.RESET + " - ");
                index++;
            }

            System.out.println(printedResult);
        } else System.out.println("No resources are waiting to be stored");
    }

    /**
     * Prints a chat message.
     *
     * @param sender  a string showing the name of the sender
     * @param message a string representing the message
     */
    @Override
    public void printChatMessage(String sender, String message) {
        System.out.println(AnsiColor.BRIGHT_MAGENTA + "[" + AnsiColor.italicize("FROM:") + AnsiColor.BRIGHT_MAGENTA + sender + "] " + message + AnsiColor.RESET);
    }

    /**
     * Prints the final scores of the game.
     *
     * @param scores     a Map showing the name of each player and its points
     * @param winnerName a String showing the name of the winner
     */
    @Override
    public void printFinalScores(Map<String, Integer> scores, String winnerName) {
        System.out.println(AnsiColor.YELLOW + AnsiColor.bold(winnerName + " is the true Master of Renaissance!"));

        int index = 0;
        for (String nick : scores.keySet()) {
            System.out.println(index + ") " + AnsiColor.YELLOW + nick + " : " + AnsiColor.RESET + scores.get(nick) + " victory points");
        }
    }

    /**
     * Prints the final scores of a single player game.
     *
     * @param lorenzoWin  a Boolean true if Lorenzo wins, else false
     * @param loseReason  a String showing the reason of the loss
     * @param playerScore the number representing the score of the player
     */
    @Override
    public void printSingleplayerFinalScore(boolean lorenzoWin, String loseReason, int playerScore) {
        if (lorenzoWin) {
            System.out.println(AnsiColor.YELLOW + AnsiColor.bold(loseReason));
            System.out.println(AnsiColor.YELLOW + AnsiColor.bold("You have lost! Lorenzo is still the true Master of Renaissance!"));
        } else {
            System.out.println(AnsiColor.YELLOW + AnsiColor.bold("You are the true Master of Renaissance!"));
            System.out.println(AnsiColor.YELLOW + AnsiColor.bold("Your score: " + playerScore + " victory points"));
        }
    }

    /**
     * Prints the DevelopmentCard with its attributes such as its production input and its production output.
     *
     * @param card  the development card to print
     * @param label the label of the card
     */
    @Override
    public void renderDevelopmentCard(DevelopmentCard card, int label) {
        ArrayList<Resource> cost = new ArrayList<>(card.getCost().keySet());
        ArrayList<Resource> input = new ArrayList<>(card.getProductionInput().keySet());
        ArrayList<Resource> output = new ArrayList<>(card.getProductionOutput().keySet());

        String prettyCard = "";

        if (label > 0) {
            prettyCard = prettyCard.concat(label + ") ");
        }

        prettyCard = prettyCard.concat("Color: " + card.getColor() + "\n" +
                "Points: " + card.getVictoryPoints() + "\n" +
                "Level: " + card.getLevel() + "\n" +
                "Cost: " + card.getCost().get(cost.get(0)) + " " + cost.get(0) + "\n");

        if (cost.size() > 1) {
            for (int i = 1; i < cost.size(); i++) {
                Resource res = cost.get(i);
                prettyCard = prettyCard.concat("      " + card.getCost().get(res) + " " + res + "\n");
            }
        }

        prettyCard = prettyCard.concat("Production input: " + card.getProductionInput().get(input.get(0)) + " " + input.get(0) + "\n");

        if (input.size() > 1) {
            for (int i = 1; i < input.size(); i++) {
                Resource res = input.get(i);
                prettyCard = prettyCard.concat("      " + card.getProductionInput().get(res) + " " + res + "\n");
            }
        }

        prettyCard = prettyCard.concat("Production output: " + card.getProductionOutput().get(output.get(0)) + " " + output.get(0) + "\n");

        if (output.size() > 1) {
            for (int i = 1; i < output.size(); i++) {
                Resource res = output.get(i);
                prettyCard = prettyCard.concat("      " + card.getProductionOutput().get(res) + " " + res + "\n");
            }
        }

        System.out.println(prettyCard);
    }

    /**
     * Prints the LeaderCard with its attributes such as its resource requirements, card color requirements and its card level requirements.
     *
     * @param card  the leader card to print
     * @param label the label of the card
     */
    @Override
    public void renderLeaderCard(LeaderCard card, int label) {
        Map<Resource, Integer> costRes = card.getCardRequirements().getResourceRequirements();
        Map<CardColor, Integer> costCol = card.getCardRequirements().getCardColorRequirements();
        Map<CardColor, Integer> costLev = card.getCardRequirements().getCardLevelRequirements();

        String prettyCard = "";

        if (label > 0) {
            prettyCard = prettyCard.concat(label + ") ");
        }

        if (getView().getModel().getLocalPlayer().isLeaderCardActive(card)) {
            prettyCard += AnsiColor.BRIGHT_BLUE + "ACTIVE\n" + AnsiColor.RESET;
        }

        prettyCard = prettyCard.concat("Points: " + card.getVictoryPoints() + "\n");

        if (costRes != null) {
            for (Resource res : costRes.keySet()) {
                prettyCard = prettyCard.concat("Resource needed: " + costRes.get(res) + " " + res + "\n");
            }
        }

        if (costCol != null) {
            for (CardColor color : costCol.keySet()) {
                prettyCard = prettyCard.concat("Card color needed: " + costCol.get(color) + " " + color + "\n");
            }
        }

        if (costLev != null) {
            for (CardColor color : costLev.keySet()) {
                prettyCard = prettyCard.concat("Card level needed: " + costLev.get(color) + " " + color + "\n");
            }
        }

        prettyCard = prettyCard.concat("Special ability: " + card.getSpecialAbility().getType() + "\n");

        prettyCard = prettyCard.concat("Targeted resource: " + card.getSpecialAbility().getTargetResource() + "\n");

        System.out.println(prettyCard);
    }

    /**
     * Prints the market.
     *
     * @param grid          a list of a list representing the resources which make up the market
     * @param slideResource a Resource representing the Resource in the slide
     */
    @Override
    public void renderMarket(List<List<Resource>> grid, Resource slideResource) {
        String market = "";
        market = market.concat("-----------------------------------------\n");
        for (int r = 0; r < 3; r++) {
            for (int i = 0; i < 4; i++) {
                String color = AnsiColor.getResourceColor(grid.get(r).get(i));
                market = market.concat("|  ");
                market = market.concat(color + "/ * \\  " + AnsiColor.RESET);
            }
            market = market.concat("|");
            market = market.concat(Constants.SIDEARROW + "\n");
            for (int i = 0; i < 4; i++) {
                String color = AnsiColor.getResourceColor(grid.get(r).get(i));
                market = market.concat("| ");
                market = market.concat(color + "*     * " + AnsiColor.RESET);
            }
            market = market.concat("|");
            market = market.concat(Constants.SIDEARROW + (r + 5) + "\n");
            for (int i = 0; i < 4; i++) {
                String color = AnsiColor.getResourceColor(grid.get(r).get(i));
                market = market.concat("|  ");
                market = market.concat(color + "\\ * /  " + AnsiColor.RESET);
            }
            market = market.concat("|");
            market = market.concat(Constants.SIDEARROW + "\n");
            market = market.concat("-----------------------------------------\n");
        }
        market = market.concat(Constants.BOTTOMARROWS);
        market = market.concat(Constants.BOTTOMINDEX);

        System.out.println(market);
        System.out.println("Slide resource: " + renderResource(slideResource));
    }
}
