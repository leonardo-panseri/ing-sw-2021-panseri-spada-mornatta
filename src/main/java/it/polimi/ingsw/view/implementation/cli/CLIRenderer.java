package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.AnsiColor;
import it.polimi.ingsw.constant.AnsiSymbol;
import it.polimi.ingsw.constant.Constants;
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
        System.out.println(AnsiColor.BLUE + "[" + AnsiColor.italicize("TO:You") + "]: " + message + AnsiColor.RESET);
    }

    @Override
    public void showLobbyMessage(String message) {
        System.out.println(AnsiColor.GREEN + "[" + AnsiColor.italicize("TO:Lobby") + "]: " + message + AnsiColor.RESET);
    }

    @Override
    public void showErrorMessage(String message) {
        System.out.println(AnsiColor.RED + "[" + AnsiColor.italicize("ERROR") + "]: " + message + AnsiColor.RESET);
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
        System.out.println(getView().getModel().getDevelopmentCards());
    }

    @Override
    public void printOthersDevelopmentCards(String playerName) {
        if (!getView().getModel().getOtherLeaderCards().containsKey(playerName)) {
            System.out.println("Player does not have active cards!");
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
        if(numActive == 0) System.out.println("Player does not have active cards!");
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
    public void printDeposit() {
        System.out.println("----------");

        if (getView().getModel().getDeposit().get(0).contains(Resource.COIN)) {
            System.out.println(AnsiSymbol.COIN);
            System.out.println("----------");
        }

        if (getView().getModel().getDeposit().get(1).contains(Resource.STONE)) {
            for (int i = 0; i < getView().getModel().getDeposit().get(1).size(); i++) System.out.println(AnsiSymbol.STONE);
            System.out.println("----------");
        }

        if (getView().getModel().getDeposit().get(2).contains(Resource.FAITH)) {
            for (int i = 0; i < getView().getModel().getDeposit().get(2).size(); i++) {
                System.out.println(AnsiColor.PURPLE + AnsiSymbol.CROSS + Constants.ANSI_RESET);
            }
        }
        System.out.println("----------");

    }

    @Override
    public void printFaith() {
        StringBuilder str = new StringBuilder("[ ");
        int faithPoints = getView().getModel().getFaithPoints();
        int pointsToWin = 24 - faithPoints;
        float percentage = (float) faithPoints / 24 * 100;

        for (int i = 0; i < faithPoints; i++) {
            str.append(AnsiColor.PURPLE + AnsiSymbol.CROSS + Constants.ANSI_RESET);
            str.append(" ");
        }
        for (int i = 0; i < pointsToWin; i++) {
            str.append(AnsiColor.RED + "-" + Constants.ANSI_RESET);
            str.append(" ");
        }
        str.append("] ");
        System.out.println("You have " + faithPoints + " faith points.");
        System.out.println(str);
        System.out.printf(("Completion %.2f%% %n"), percentage);
        System.out.println("You need " + pointsToWin + " to win.");


    }

    @Override
    public void renderDevelopmentCard(DevelopmentCard card, int label) {
        ArrayList<Resource> cost = new ArrayList<>(card.getCost().keySet());
        ArrayList<Resource> input = new ArrayList<>(card.getProductionInput().keySet());
        ArrayList<Resource> output = new ArrayList<>(card.getProductionOutput().keySet());

        String prettyCard = "";

        if(label > 0) {
            prettyCard = prettyCard.concat(label + ") ");
        }

        prettyCard = prettyCard.concat("Color: " + card.getColor() + "\n" +
                "Points: " + card.getVictoryPoints() + "\n" +
                "Level: " + card.getLevel() + "\n" +
                "Cost: " + card.getCost().get(cost.get(0)) + " " + cost.get(0) + "\n");

        if(cost.size() > 1){
            for (int i = 1; i < cost.size(); i++) {
                Resource res = cost.get(i);
                prettyCard = prettyCard.concat( "      "+ card.getCost().get(res) + " " + res +"\n");
            }
        }

        prettyCard = prettyCard.concat("Production input: " + card.getProductionInput().get(input.get(0)) + " " + input.get(0) + "\n");

        if (input.size() > 1) {
            for (int i = 1; i < input.size(); i++) {
                Resource res = input.get(i);
                prettyCard = prettyCard.concat( "      "+ card.getProductionInput().get(res) + " " + res +"\n");
            }
        }

        prettyCard = prettyCard.concat("Production output: " + card.getProductionOutput().get(output.get(0)) + " " + output.get(0) + "\n");

        if (output.size() > 1) {
            for (int i = 1; i < output.size(); i++) {
                Resource res = output.get(i);
                prettyCard = prettyCard.concat( "      "+ card.getProductionOutput().get(res) + " " + res +"\n");
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

        if(label > 0) {
            prettyCard = prettyCard.concat(label + ") ");
        }

        prettyCard = prettyCard.concat("Points: " + card.getVictoryPoints() + "\n");

        if(costRes != null){
            for (Resource res : costRes.keySet()) {
                prettyCard = prettyCard.concat("Resource needed: " + costRes.get(res) + " " + res + "\n");
            }
        }

        if(costCol != null){
            for (CardColor color : costCol.keySet()) {
                prettyCard = prettyCard.concat("Card color needed: " + costCol.get(color) + " " + color + "\n");
            }
        }

        if(costLev != null){
            for (CardColor color : costLev.keySet()) {
                prettyCard = prettyCard.concat("Card level needed: " + costLev.get(color) + " " + color + "\n");
            }
        }

        prettyCard = prettyCard.concat("Special ability: " + card.getSpecialAbility().getType() + "\n");

        prettyCard = prettyCard.concat("Targeted resource: " + card.getSpecialAbility().getTargetResource() + "\n");

        System.out.println(prettyCard);
    }
}
