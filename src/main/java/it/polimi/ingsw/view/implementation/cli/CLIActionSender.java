package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.messages.DepositUpdate;
import it.polimi.ingsw.view.ActionSender;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.BuyPlayerActionEvent;
import it.polimi.ingsw.view.messages.DepositPlayerActionEvent;
import it.polimi.ingsw.view.messages.DiscardLeaderPlayerActionEvent;
import it.polimi.ingsw.view.messages.MarketPlayerActionEvent;

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
    public void draw(int marketIndex, Resource whiteConversion) {
        getView().getClient().send(new MarketPlayerActionEvent(getView().getPlayerName(), marketIndex - 1, whiteConversion));
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
        List<Resource> newRow1 = getView().getModel().getDeposit().get(row2 - 1);
        List<Resource> newRow2 = getView().getModel().getDeposit().get(row1 - 1);

        Map<Integer, List<Resource>> changes = new HashMap<>();
        changes.put(row1, newRow1);
        changes.put(row2, newRow2);

        getView().getClient().send(new DepositPlayerActionEvent(getView().getPlayerName(), changes, getView().getModel().getMarketResult()));
    }

    @Override
    public void storeMarketResult(int resourceIndex, int rowIndex) {
        Map<Integer, List<Resource>> changes = new HashMap<>();
        List<Resource> toBeStored = new ArrayList<>(getView().getModel().getMarketResult());
        Resource movedResource = toBeStored.get(resourceIndex - 1);
        toBeStored.remove(movedResource);

        changes.put(rowIndex, new ArrayList<>(getView().getModel().getDeposit().get(rowIndex-1)));
        changes.get(rowIndex).add(movedResource);

        getView().getClient().send(new DepositPlayerActionEvent(getView().getPlayerName(), changes, toBeStored));
    }
}
