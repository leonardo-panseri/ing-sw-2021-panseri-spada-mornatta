package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.List;
import java.util.Map;

public abstract class ModelUpdateHandler {
    private final View view;

    protected ModelUpdateHandler(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public abstract void updateGamePhase(GamePhase gamePhase);

    public abstract void updateLeaderCards(String playerName, Map<LeaderCard, Boolean> ownedLeaders);

    public abstract void updateDevelopmentCards(String playerName, DevelopmentCard card, int slot);

    public abstract void updateTurn(String playerName);

    public abstract void updateMarket(int index, List<Resource> changes);

    public abstract void updateDeposit(String playerName, Map<Integer, List<Resource>> changes, Map<Resource, Integer> leadersDeposit);

    public abstract void updateStrongbox(String playerName, Map<Resource, Integer> strongbox);

    public abstract void updateFaith(String playerName, int faithPoints, int popeFavours);

    public abstract void insertDrawnResources(String playerName, List<Resource> result);
}
