package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.ActionSender;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.BuyPlayerActionEvent;
import it.polimi.ingsw.view.messages.MarketPlayerActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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
}
