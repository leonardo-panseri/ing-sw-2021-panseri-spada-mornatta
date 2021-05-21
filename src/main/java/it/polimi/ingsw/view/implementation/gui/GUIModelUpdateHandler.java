package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.ModelUpdateHandler;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Map;

public class GUIModelUpdateHandler extends ModelUpdateHandler {
    protected GUIModelUpdateHandler(View view) {
        super(view);
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {

    }

    @Override
    public void handleInitialTurn(String playerName, Map<LeaderCard, Boolean> leaderCards, int resourceToChoose) {

    }

    @Override
    public void updateLeaderCards(String playerName, Map<LeaderCard, Boolean> ownedLeaders) {

    }

    @Override
    public void updateDevelopmentCards(String playerName, DevelopmentCard card, int slot) {

    }

    @Override
    public void updateTurn(String playerName) {

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
