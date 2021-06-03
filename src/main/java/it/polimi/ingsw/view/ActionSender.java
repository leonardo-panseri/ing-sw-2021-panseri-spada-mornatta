package it.polimi.ingsw.view;

import it.polimi.ingsw.client.messages.GameConfigMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.view.beans.MockDeposit;
import it.polimi.ingsw.view.messages.*;
import it.polimi.ingsw.view.messages.production.BaseProduction;
import it.polimi.ingsw.view.messages.production.DevelopmentProduction;
import it.polimi.ingsw.view.messages.production.LeaderProduction;
import it.polimi.ingsw.view.messages.production.Production;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class ActionSender {
    private final View view;

    private final ObservableList<Production> pendingProductions;

    public ActionSender(View view) {
        this.view = view;
        this.pendingProductions = FXCollections.observableArrayList();
    }

    public View getView() {
        return view;
    }

    public List<Production> getPendingProductions() {
        return pendingProductions;
    }

    public ObservableList<Production> pendingProductionsProperty() {
        return pendingProductions;
    }

    protected void addPendingProduction(Production production) {
        pendingProductions.add(production);
    }

    protected void clearPendingProductions() {
        pendingProductions.clear();
    }

    public void setPlayersToStart(int playersToStart) {
        getView().getClient().send(new PlayersToStartMessage(playersToStart));
    }

    public void setGameConfig(File gameConfig) {
        String serializedGameConfig = null;
        if(gameConfig != null) {
            try {
                FileInputStream is = new FileInputStream(gameConfig);
                byte[] encoded = is.readAllBytes();
                serializedGameConfig = new String(encoded, StandardCharsets.UTF_8);
            } catch (IOException e) {
                getView().getRenderer().showErrorMessage("The given path is not valid!");
            }
        }

        getView().getClient().send(new GameConfigMessage(serializedGameConfig));
    }

    public void selectLeaders(List<UUID> leaderCards, Map<Integer, List<Resource>> selectedResources) {
        getView().getClient().send(new InitialSelectionPlayerActionEvent(leaderCards, selectedResources));
    }

    /**
     *  Buys the DevelopmentCard selected from the deck.
     *
     * @param cardIndex the index of the card to buy
     * @param slotIndex the slot in which the card will be put in
     */

    public void buyDevelopmentCard(int cardIndex, int slotIndex) {
        List<HashMap<CardColor, ObservableList<DevelopmentCard>>> deck = getView().getModel().getDevelopmentDeck();
        int mapIndex = cardIndex == 0 ? 0 : (cardIndex - 1) / 4;
        int stackIndex = cardIndex == 0 ? 0 : (cardIndex - 1) - 4 * mapIndex;

        ArrayList<ObservableList<DevelopmentCard>> stacks = new ArrayList<>(deck.get(mapIndex).values());
        getView().getClient().send(new BuyPlayerActionEvent(stacks.get(stackIndex).get(stacks.get(stackIndex).size()-1).getUuid(), slotIndex));
    }

    /**
     * Choose a column or row of Marbles in the Market and take all the Resources displayed in the chosen column
     * or row, each Market Marble indicates a Resource.
     *
     * @param marketIndex the index representing the row or column to drawn
     * @param whiteConversions special leader ability that when you take Resources from the market, each
     *                         white Marble in the chosen row or column gives you the indicated Resource
     */

    public void draw(int marketIndex, List<Resource> whiteConversions) {
        getView().getClient().send(new MarketPlayerActionEvent(marketIndex - 1, whiteConversions));
    }

    /**
     * Discards a Leader : you can discard a Leader Card from your hand to receive one Faith Point.
     *
     * @param cardIndex the index of the card to discard
     */

    public void discard(int cardIndex) {
        LeaderCard cardToDiscard = getView().getModel().getLocalPlayer().getLeaderCardAt(cardIndex - 1);
        if (getView().getModel().getLocalPlayer().isLeaderCardActive(cardToDiscard)) {
            getView().getRenderer().showErrorMessage(ViewString.ALREADY_ACTIVE);
            return;
        }

        getView().getClient().send(new DiscardLeaderPlayerActionEvent(cardToDiscard.getUuid()));
    }

    /**
     * Moves the Resources in your deposit: you can move Resources in your depots in any way during your turn.
     * Swaps the resources in {@param row1} and {@param row2}.
     *
     * @param row1 the index of the row of your deposit to be moved
     * @param row2 the index of the row of your deposit where {@param row2} will be moved
     */

    public void move(int row1, int row2) {
        MockDeposit deposit = getView().getModel().getLocalPlayer().getDeposit();

        List<Resource> newRow1;
        List<Resource> newRow2;
        Map<Integer, List<Resource>> changes = new HashMap<>();
        Map<Integer, List<Resource>> leadersDepositChanges = new HashMap<>();

        if(row1 < 4) {
            if(row2 < 4) {
                newRow1 = deposit.getRow(row2 - 1);
                newRow2 = deposit.getRow(row1 - 1);
                changes.put(row1, newRow1);
                changes.put(row2, newRow2);
            } else {
                newRow1 = new ArrayList<>(deposit.getRow(row1 - 1));
                newRow2 = new ArrayList<>(deposit.getLeadersDeposit(row2 - 3));
                if(!newRow1.isEmpty()) {
                    Resource res = newRow1.remove(0);
                    newRow2.add(res);
                }
                changes.put(row1, newRow1);
                leadersDepositChanges.put(row2 - 3, newRow2);
            }
        } else {
            if(row2 < 4) {
                newRow1 = new ArrayList<>(deposit.getLeadersDeposit(row1 - 3));
                newRow2 = new ArrayList<>(deposit.getRow(row2 - 1));
                if(!newRow1.isEmpty()) {
                    Resource res = newRow1.remove(0);
                    newRow2.add(res);
                }
                leadersDepositChanges.put(row1 - 3, newRow1);
                changes.put(row2, newRow2);
            } else {
                newRow1 = new ArrayList<>(deposit.getLeadersDeposit(row1 - 3));
                newRow2 = new ArrayList<>(deposit.getLeadersDeposit(row2 - 3));
                if(!newRow1.isEmpty()) {
                    Resource res = newRow1.remove(0);
                    newRow2.add(res);
                }
                leadersDepositChanges.put(row1 - 3, newRow1);
                leadersDepositChanges.put(row2 - 3, newRow2);
            }
        }

        getView().getClient().send(new DepositPlayerActionEvent(changes, new ArrayList<>(deposit.getMarketResult()), leadersDepositChanges));
    }

    /**
     * Places the Resources taken from the Market  in the deposit with the exception of Faith Points,
     * which are registered on the faith Track. The deposit is divided in three depots of 1,2, or 3 slot each.
     * You can place only one type of Resource in a single depot.
     * You can't place the same type of Resource in two different depots.
     * In other words each depot must have the same Resource and all depots must have different Resource.
     *
     * @param resourceIndex thh index representing the Resource to store
     * @param rowIndex the index representing the row of the deposit where the Resource will be stored
     */

    public void storeMarketResult(int resourceIndex, int rowIndex) {
        MockDeposit deposit = getView().getModel().getLocalPlayer().getDeposit();

        Map<Integer, List<Resource>> changes = new HashMap<>();
        Map<Integer, List<Resource>> leadersDepositChanges = new HashMap<>();
        List<Resource> toBeStored = new ArrayList<>(deposit.getMarketResult());
        Resource movedResource = toBeStored.get(resourceIndex - 1);
        toBeStored.remove(movedResource);

        if(rowIndex < 4){
            changes.put(rowIndex, new ArrayList<>(deposit.getRow(rowIndex - 1)));
            changes.get(rowIndex).add(movedResource);
        } else if(rowIndex == 4 || rowIndex == 5) {
            List<Resource> newResources = new ArrayList<>(deposit.getLeadersDeposit(rowIndex - 3));
            newResources.add(movedResource);
            leadersDepositChanges.put(rowIndex - 3, newResources);
        }

        getView().getClient().send(new DepositPlayerActionEvent(changes, toBeStored, leadersDepositChanges));
    }

    public void endTurn() {
        if(!view.isOwnTurn()) {
            view.getRenderer().showErrorMessage(ViewString.NOT_YOUR_TURN);
            return;
        }

        view.getClient().send(new EndTurnPlayerActionEvent());
    }

    /**
     * Activates a LeaderCard:  If you satisfy the requirement of a Leader Card in your hand, you can play
     that LeaderCard. It will give you a special ability for the rest of the game.
     *
     * @param cardIndex the index of the LeaderCard to set active
     */

    public void setActive(int cardIndex) {
        LeaderCard setActive = getView().getModel().getLocalPlayer().getLeaderCardAt(cardIndex - 1);
        getView().getClient().send(new ActivateLeaderPlayerActionEvent(setActive.getUuid()));
    }

    /**
     * Adds the the production ability of a LeaderCard to the queue. This ability gives you an additional production power.
     * When you activate the production, you can freely use this power as usual.
     * You will receive a Resource of your choosing and 1 Faith Point.
     *
     * @param cardIndex the index of the card to be used
     * @param desiredResource the desired Resource of your choosing to receive
     */

    public void useLeaderProduction(int cardIndex, Resource desiredResource) throws IllegalArgumentException {
        LeaderCard leaderCard = getView().getModel().getLocalPlayer().getLeaderCardAt(cardIndex - 1);
        if(!getView().getModel().getLocalPlayer().isLeaderCardActive(leaderCard)) {
            throw new IllegalArgumentException("not_active");
        }
        if(leaderCard.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION) {
            throw new IllegalArgumentException("no_ability");
        }
        getView().setUsingProductions(true);
        addPendingProduction(new LeaderProduction(leaderCard.getUuid(), desiredResource));

        getView().getModel().getLocalPlayer().getDeposit().removeResources(getView(), Map.of(leaderCard.getSpecialAbility().getTargetResource(), 1));
    }

    /**
     * Adds the production ability of a DevelopmentCard to the queue. Every Development Card has a production power.
     * When you activate the production, you can pay the Resources required to receive the Resources given by the
     * LeaderCard.
     *
     * @param cardIndex the index of the DevelopmentCard to use
     */

    public void useDevelopmentProduction(int cardIndex) throws IllegalArgumentException {
        DevelopmentCard developmentCard = getView().getModel().getLocalPlayer().getPlayerBoard()
                .getTopDevelopmentCardAt(cardIndex - 1);
        if(developmentCard == null) {
            throw new IllegalArgumentException();
        }

        getView().setUsingProductions(true);
        addPendingProduction(new DevelopmentProduction(developmentCard.getUuid()));

        getView().getModel().getLocalPlayer().getDeposit().removeResources(getView(), developmentCard.getCost());
    }

    /**
     * Adds the base production ability to the queue. The basic production power allows you to pay 2 Resources of
     * any type (even 2 different Resources) to receive 1 Resource of your choosing.
     *
     * @param inputResource a List representing the Resources needed to activate the base production.
     * @param outputResource a Resource representing the output of the base production.
     */

    public void useBaseProduction(List<Resource> inputResource, List<Resource> outputResource) {
        getView().setUsingProductions(true);
        addPendingProduction(new BaseProduction(inputResource, outputResource));

        Map<Resource, Integer> cost = new HashMap<>();
        for(Resource res : inputResource) {
            if(cost.containsKey(res))
                cost.put(res, cost.get(res) + 1);
            else
                cost.put(res, 1);
        }
        getView().getModel().getLocalPlayer().getDeposit().removeResources(getView(), cost);
    }

    /**
     * Checks if the development productions and the base productions in the queue can be executed and, if so,
     * executes them.
     */

    public void executeProductions() throws IllegalArgumentException {
        if(!getView().isUsingProductions() || getPendingProductions().isEmpty()) {
            throw new IllegalArgumentException();
        }
        getView().getClient().send(new ProductionPlayerActionEvent(getPendingProductions()));
        clearPendingProductions();
    }

    /**
     * Sends a given message to all the players.
     *
     * @param message the message to send
     */

    public void sendChatMessage(String message) {
        getView().getClient().send(new ChatPlayerActionEvent(message));
    }
}
