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

import java.util.*;

public class CLIRenderer extends Renderer {

    protected CLIRenderer(View view) {
        super(view);
    }

    @Override
    public void showGameMessage(String message) {
        System.out.println(AnsiColor.BLUE + "[" + AnsiColor.italicize("TO:You") + AnsiColor.BLUE + "] " + message + AnsiColor.RESET);
    }

    @Override
    public void showLobbyMessage(String message) {
        System.out.println(AnsiColor.GREEN + "[" + AnsiColor.italicize("TO:Lobby") + AnsiColor.GREEN + "] " + message + AnsiColor.RESET);
    }

    @Override
    public void showErrorMessage(String message) {
        System.out.println(AnsiColor.RED + "[" + AnsiColor.italicize("ERROR") + AnsiColor.RED + "] " + message + AnsiColor.RESET);
    }

    @Override
    public void printMarket() {
        System.out.println(Constants.buildMarket(getView().getModel().getMarket()));
    }

    @Override
    public void printOwnLeaders() {
        int index = 1;
        for (LeaderCard card : getView().getModel().getLeaderCards().keySet()) {
            getView().getRenderer().renderLeaderCard(card, index);
            index++;
        }
    }

    @Override
    public void printOwnDevelopmentCards() {
        if (!getView().getModel().hasOwnDevelopmentCard()) {
            System.out.println("You don't have any development card");
            return;
        }
        List<Stack<DevelopmentCard>> targetCards = getView().getModel().getDevelopmentCards();
        int index = 1;
        for (Stack<DevelopmentCard> stack : targetCards) {
            for (DevelopmentCard card : stack) {
                if (stack.peek() == card) {
                    System.out.println(AnsiColor.BRIGHT_BLUE + "USABLE CARD" + AnsiColor.RESET);
                }
                renderDevelopmentCard(card, index);
                index++;
            }
        }
    }

    @Override
    public void printOwnDeposit() {
        renderDeposit(getView().getModel().getDeposit());
        renderLeadersDeposit(getView().getModel().getLeadersDeposit());
    }

    @Override
    public void printOwnStrongbox() {
        renderStrongbox(getView().getModel().getStrongbox());
    }

    @Override
    public void printOthersLeaderCards(String playerName) {
        if (!getView().getModel().getOtherLeaderCards().containsKey(playerName)) {
            System.out.println(playerName + " does not have active cards!");
            return;
        }

        int numActive = 0;
        Map<LeaderCard, Boolean> targetCards = getView().getModel().getOtherLeaderCards().get(playerName);
        for (LeaderCard card : targetCards.keySet()) {
            if (targetCards.get(card)) {
                numActive++;
                renderLeaderCard(card, -1);
            }
        }
        if (numActive == 0) System.out.println("Player does not have active cards!");
    }

    @Override
    public void printOthersDevelopmentCards(String playerName) {
        if (!getView().getModel().getOtherDevelopmentCards().containsKey(playerName)) {
            System.out.println("Player does not have development cards!");
            return;
        }

        List<Stack<DevelopmentCard>> targetCards = getView().getModel().getOtherDevelopmentCards().get(playerName);
        for (Stack<DevelopmentCard> stack : targetCards) {
            for (DevelopmentCard card : stack) {
                if (stack.peek() == card) {
                    System.out.println(AnsiColor.BRIGHT_BLUE + "USABLE CARD" + AnsiColor.RESET);
                }
                renderDevelopmentCard(card, -1);
            }
        }
    }

    @Override
    public void printOthersDeposit(String playerName) {
        if (!getView().getModel().getOtherDeposit().containsKey(playerName)) {
            showErrorMessage(playerName + " does not have a deposit or it's empty");
            return;
        }
        renderDeposit(getView().getModel().getOtherDeposit().get(playerName));
        if (!getView().getModel().getOtherLeadersDeposit().containsKey(playerName)) {
            showErrorMessage(playerName + " does not have leaders deposits or they are empty");
            return;
        }
        renderLeadersDeposit(getView().getModel().getOtherLeadersDeposit().get(playerName));
    }

    @Override
    public void printOthersFaith(String playerName) {
        printFaith(getView().getModel().getOtherFaith(playerName));
    }

    @Override
    public void printDevelopmentDeck() {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> deck = getView().getModel().getDevelopmentDeck();
        int index = 1;
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : deck) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                renderDevelopmentCard(stack.peek(), index);
                index++;
            }
        }
    }

    @Override
    public void renderDeposit(List<List<Resource>> deposit) {
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

    @Override
    public void renderLeadersDeposit(Map<Integer, List<Resource>> leadersDeposit) {
        if(leadersDeposit.size() == 0) return;
        System.out.println(AnsiColor.CYAN + "-- LEADERS DEPOSITS --" + AnsiColor.RESET);
        for (List<Resource> deposit : leadersDeposit.values()) {
            if(deposit.size()>0){
                Resource res = deposit.get(0);
                System.out.println(renderResource(res) + " " + Constants.parseResource(res) + res + AnsiColor.RESET + ": " + deposit.size());
            } else System.out.println("Empty deposit slot");
        }
    }

    @Override
    public void renderStrongbox(Map<Resource, Integer> strongbox) {
        for (Resource res : strongbox.keySet()) {
            String strongboxRow = "";
            strongboxRow = strongboxRow.concat(renderResource(res) + " " + Constants.parseResource(res) + res + AnsiColor.RESET + ": " + strongbox.get(res));
            System.out.println(strongboxRow);
        }
    }

    @Override
    public String renderResource(Resource res) {
        switch (res) {
            case COIN -> {
                return Constants.parseResource(res) + AnsiSymbol.COIN + AnsiColor.RESET;
            }
            case SERVANT -> {
                return Constants.parseResource(res) + AnsiSymbol.SERVANT + AnsiColor.RESET;
            }
            case FAITH -> {
                return Constants.parseResource(res) + AnsiSymbol.FAITH + AnsiColor.RESET;
            }
            case SHIELD -> {
                return Constants.parseResource(res) + AnsiSymbol.SHIELD + AnsiColor.RESET;
            }
            case STONE -> {
                return Constants.parseResource(res) + AnsiSymbol.STONE + AnsiColor.RESET;
            }
        }
        return "";
    }

    @Override
    public void help() {
        int index = 1;
        for (String command : ViewString.getCommands()) {
            System.out.println(index + ") " + AnsiColor.GREEN + command + AnsiColor.RESET);
            index++;
        }
    }

    @Override
    public void printFaith(int faith) {
        StringBuilder str = new StringBuilder("[ ");
        StringBuilder popeReports = new StringBuilder("  ");
        StringBuilder points = new StringBuilder("  ");
        int pointsToWin = 24 - faith;
        float percentage = (float) faith / 24 * 100;

        for (int i = 0; i < faith; i++) {
            str.append(AnsiColor.PURPLE + AnsiSymbol.CROSS + Constants.ANSI_RESET);
            str.append(" ");
        }
        for (int i = 0; i < pointsToWin; i++) {
            str.append(AnsiColor.RED + "-" + Constants.ANSI_RESET);
            str.append(" ");
        }
        str.append("] ");
        for (int i = 0; i < 24; i++) {
            if (i == 7 || i == 15 || i == 23) {
                popeReports.append(AnsiColor.YELLOW + AnsiSymbol.BISHOP + Constants.ANSI_RESET);
            } else if (i == 22 || i == 21 || i == 20 || i == 19 ||
                    i == 18 || i == 14 || i == 13 || i == 12 ||
                    i == 11 || i == 6 || i == 5 || i == 4) {
                popeReports.append(AnsiColor.GREEN + "+" + Constants.ANSI_RESET);
            } else {
                popeReports.append("x");
            }
            popeReports.append(" ");
        }
        for (int i = 0; i < 24; i++) {
            if (i == 2) {
                points.append(AnsiColor.BRIGHT_BLUE +"1" + Constants.ANSI_RESET);
            } else if (i == 5) {
                points.append(AnsiColor.BRIGHT_BLUE +"2" + Constants.ANSI_RESET);
            } else if (i == 8) {
                points.append(AnsiColor.BRIGHT_BLUE +"4" + Constants.ANSI_RESET);
            } else if (i == 11) {
                points.append(AnsiColor.BRIGHT_BLUE +"6" + Constants.ANSI_RESET);
            } else if (i == 14) {
                points.append(AnsiColor.BRIGHT_BLUE +"9" + Constants.ANSI_RESET);
            } else if (i == 17) {
                points.append(AnsiColor.BRIGHT_BLUE +"12" + Constants.ANSI_RESET);
            } else if (i == 20) {
                points.append(AnsiColor.BRIGHT_BLUE +"16" + Constants.ANSI_RESET);
            } else if (i == 23) {
                points.append(AnsiColor.BRIGHT_BLUE +"20" + Constants.ANSI_RESET);
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

    @Override
    public void printMarketResult() {
        String printedResult = "";
        printedResult = printedResult.concat("The following resources are waiting to be stored: - ");

        int index = 1;
        for (Resource res : getView().getModel().getMarketResult()) {
            printedResult = printedResult.concat(" " + index + ") " + Constants.parseResource(res) + res + AnsiColor.RESET + " - ");
            index++;
        }

        System.out.println(printedResult);
    }

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

    @Override
    public void renderLeaderCard(LeaderCard card, int label) {
        Map<Resource, Integer> costRes = card.getCardRequirements().getResourceRequirements();
        Map<CardColor, Integer> costCol = card.getCardRequirements().getCardColorRequirements();
        Map<CardColor, Integer> costLev = card.getCardRequirements().getCardLevelRequirements();

        String prettyCard = "";

        if (label > 0) {
            prettyCard = prettyCard.concat(label + ") ");
        }

        if(getView().getModel().getLeaderCards().getOrDefault(card, false)) {
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
}
