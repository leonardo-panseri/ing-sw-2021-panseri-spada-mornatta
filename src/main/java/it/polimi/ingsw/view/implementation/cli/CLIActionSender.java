package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.model.messages.DepositUpdate;
import it.polimi.ingsw.view.ActionSender;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.*;
import it.polimi.ingsw.view.messages.production.BaseProduction;
import it.polimi.ingsw.view.messages.production.DevelopmentProduction;
import it.polimi.ingsw.view.messages.production.LeaderProduction;
import it.polimi.ingsw.view.messages.production.Production;

import java.util.*;

public class CLIActionSender extends ActionSender {
    public CLIActionSender(View view) {
        super(view);
    }

    @Override
    public void buyDevelopmentCard(int cardIndex) {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> deck = getView().getModel().getDevelopmentDeck();
        int mapIndex = cardIndex == 0 ? 0 : (cardIndex - 1) / 4;
        int stackIndex = cardIndex == 0 ? 0 : (cardIndex - 1) - 4 * mapIndex;

        ArrayList<Stack<DevelopmentCard>> stacks = new ArrayList<>(deck.get(mapIndex).values());
        getView().getClient().send(new BuyPlayerActionEvent(getView().getPlayerName(), stacks.get(stackIndex).peek().getUuid(), 1));
    }

    @Override
    public void draw(int marketIndex, List<Resource> whiteConversions) {
        getView().setAlreadyPlayed(true);
        getView().getClient().send(new MarketPlayerActionEvent(getView().getPlayerName(), marketIndex - 1, whiteConversions));
    }

    @Override
    public void discard(int cardIndex) {
        ArrayList<LeaderCard> leaderCards = new ArrayList<>(getView().getModel().getLeaderCards().keySet());
        LeaderCard cardToDiscard = leaderCards.get(cardIndex - 1);
        if (getView().getModel().getLeaderCards().get(cardToDiscard)) {
            getView().getRenderer().showErrorMessage(ViewString.ALREADY_ACTIVE);
            return;
        }

        getView().getClient().send(new DiscardLeaderPlayerActionEvent(getView().getPlayerName(), cardToDiscard.getUuid()));
    }

    @Override
    public void move(int row1, int row2) {
        List<Resource> newRow1;
        List<Resource> newRow2;
        Map<Integer, List<Resource>> changes = new HashMap<>();

        Map<Integer, List<Resource>> leadersDepositChanges = new HashMap<>();

        if(row1 < 4) {
            if(row2 < 4) {
                newRow1 = getView().getModel().getDeposit().get(row2 - 1);
                newRow2 = getView().getModel().getDeposit().get(row1 - 1);
                changes.put(row1, newRow1);
                changes.put(row2, newRow2);
            } else {
                newRow1 = getView().getModel().getDeposit().get(row1 - 1);
                newRow2 = getView().getModel().getLeadersDeposit().get(row2 - 3);
                if(!newRow1.isEmpty()) {
                    Resource res = newRow1.remove(0);
                    newRow2.add(res);
                }
                changes.put(row1, newRow1);
                leadersDepositChanges.put(row2 - 3, newRow2);
            }
        } else {
            if(row2 < 4) {
                newRow1 = getView().getModel().getLeadersDeposit().get(row1 - 3);
                newRow2 = getView().getModel().getDeposit().get(row2 - 1);
                if(!newRow1.isEmpty()) {
                    Resource res = newRow1.remove(0);
                    newRow2.add(res);
                }
                leadersDepositChanges.put(row1 - 3, newRow1);
                changes.put(row2, newRow2);
            } else {
                newRow1 = getView().getModel().getLeadersDeposit().get(row1 - 3);
                newRow2 = getView().getModel().getLeadersDeposit().get(row2 - 3);
                if(!newRow1.isEmpty()) {
                    Resource res = newRow1.remove(0);
                    newRow2.add(res);
                }
                leadersDepositChanges.put(row1 - 3, newRow1);
                leadersDepositChanges.put(row2 - 3, newRow2);
            }
        }

        getView().getClient().send(new DepositPlayerActionEvent(getView().getPlayerName(), changes, getView().getModel().getMarketResult(), leadersDepositChanges));
    }

    @Override
    public void setActive(int cardIndex) {
        ArrayList<LeaderCard> activeLeaderCard = new ArrayList<>(getView().getModel().getLeaderCards().keySet());
        LeaderCard setActive = activeLeaderCard.get(cardIndex - 1);
        getView().getClient().send(new ActivateLeaderPlayerActionEvent(getView().getPlayerName(), setActive.getUuid()));

    }

    @Override
    public void storeMarketResult(int resourceIndex, int rowIndex) {
        Map<Integer, List<Resource>> changes = new HashMap<>();
        Map<Integer, List<Resource>> leadersDepositChanges = new HashMap<>();
        List<Resource> toBeStored = new ArrayList<>(getView().getModel().getMarketResult());
        Resource movedResource = toBeStored.get(resourceIndex - 1);
        toBeStored.remove(movedResource);

        if(rowIndex < 4){
            changes.put(rowIndex, new ArrayList<>(getView().getModel().getDeposit().get(rowIndex-1)));
            changes.get(rowIndex).add(movedResource);
        } else if(rowIndex == 4 || rowIndex == 5) {
            List<Resource> newResources = new ArrayList<>(getView().getModel().getLeadersDeposit().get(rowIndex - 3));
            newResources.add(movedResource);
            leadersDepositChanges.put(rowIndex - 3, newResources);
        }

        getView().getClient().send(new DepositPlayerActionEvent(getView().getPlayerName(), changes, toBeStored, leadersDepositChanges));
    }

    @Override
    public void useLeaderProduction(int cardIndex, Resource desiredResource) {
        List<LeaderCard> leaderCards = new ArrayList<>(getView().getModel().getLeaderCards().keySet());
        LeaderCard leaderCard = leaderCards.get(cardIndex - 1);
        if(!getView().getModel().getLeaderCards().get(leaderCard)) {
            getView().getRenderer().showErrorMessage("This leader card is not active");
            return;
        }
        if(leaderCard.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION) {
            getView().getRenderer().showErrorMessage("This leader card does not have a production ability");
            return;
        }
        getView().setAlreadyPlayed(true);
        getView().setUsingProductions(true);
        addPendingProduction(new LeaderProduction(leaderCard.getUuid(), desiredResource));
        getView().getRenderer().showGameMessage(ViewString.PRODUCTION_QUEUED);
    }

    @Override
    public void useDevelopmentProduction(int cardIndex) {
        DevelopmentCard developmentCard;
        try {
            developmentCard = getView().getModel().getDevelopmentCards().get(cardIndex - 1).peek();
        } catch (EmptyStackException e) {
            getView().getRenderer().showErrorMessage("This development card slot is empty");
            return;
        }
        getView().setAlreadyPlayed(true);
        getView().setUsingProductions(true);
        addPendingProduction(new DevelopmentProduction(developmentCard.getUuid()));
        getView().getRenderer().showGameMessage(ViewString.PRODUCTION_QUEUED);
    }

    @Override
    public void useBaseProduction(List<Resource> inputResource, Resource outputResource) {
        getView().setAlreadyPlayed(true);
        getView().setUsingProductions(true);
        addPendingProduction(new BaseProduction(inputResource, outputResource));
        getView().getRenderer().showGameMessage(ViewString.PRODUCTION_QUEUED);
    }

    @Override
    public void executeProductions() {
        if(!getView().isUsingProductions() || getPendingProductions().isEmpty()) {
            getView().getRenderer().showErrorMessage("There are no productions in the queue");
            return;
        }
        getView().getClient().send(new ProductionPlayerActionEvent(getView().getPlayerName(), getPendingProductions()));
        clearPendingProductions();
    }
}
