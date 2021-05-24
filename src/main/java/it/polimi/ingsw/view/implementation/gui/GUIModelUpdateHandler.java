package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.constant.AnsiColor;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.ModelUpdateHandler;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Map;

public class GUIModelUpdateHandler extends ModelUpdateHandler {
    protected GUIModelUpdateHandler(View view) {
        super(view);
    }

    @Override
    public void updateTurn(String playerName) {
        super.updateTurn(playerName);
    }

    @Override
    public void updateMarket(int index, List<Resource> changes, Resource slideResource) {

    }

    @Override
    public void updateDeposit(String playerName, Map<Integer, List<Resource>> changes, Map<Integer, List<Resource>> leadersDeposit) {

    }

    @Override
    public void updateStrongbox(String playerName, Map<Resource, Integer> strongbox) {

    }

    @Override
    public void updateFaith(String playerName, int faithPoints) {

    }

    @Override
    public void updatePopeFavours(String playerName, int popeFavours) {

    }

    @Override
    public void updateChat(String playerName, String message) {

    }

    @Override
    public void insertDrawnResources(String playerName, List<Resource> result) {

    }
}
