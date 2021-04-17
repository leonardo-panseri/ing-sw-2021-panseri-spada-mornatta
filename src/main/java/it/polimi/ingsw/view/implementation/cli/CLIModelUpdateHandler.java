package it.polimi.ingsw.view.implementation.cli;

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

public class CLIModelUpdateHandler extends ModelUpdateHandler {
    protected CLIModelUpdateHandler(View view) {
        super(view);
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        switch (gamePhase) {
            case SELECTING_LEADERS -> {
                if (getView().isOwnTurn()) {
                    getView().setGameState(GameState.SELECT_LEADERS);
                    getView().getRenderer().showGameMessage(ViewString.SELECT_LEADERS);
                    int index = 1;
                    for (LeaderCard card : getView().getModel().getLeaderCards().keySet()) {
                        getView().getRenderer().renderLeaderCard(card, index);
                        index++;
                    }
                } else {
                    getView().setGameState(GameState.WAIT_SELECT_LEADERS);
                }
            }
            case PLAYING -> getView().setGameState(GameState.PLAYING);
        }
    }

    @Override
    public void updateLeaderCards(String playerName, Map<LeaderCard, Boolean> ownedLeaders) {
        if (playerName.equals(getView().getPlayerName())) {
            getView().getModel().setLeaderCards(ownedLeaders);
        } else getView().getModel().setOthersLeaderCards(playerName, ownedLeaders);
    }

    @Override
    public void updateDevelopmentCards(DevelopmentCard card, int slot) {
        getView().getModel().setNewDevelopmentCard(card, slot);
    }

    @Override
    public void updateTurn(String playerName) {
        if (playerName.equals(getView().getPlayerName())) {
            getView().setOwnTurn(true);
            System.out.println("It's your turn");
            if (getView().getGameState() == GameState.WAIT_SELECT_LEADERS) {
                getView().setGameState(GameState.SELECT_LEADERS);
                System.out.println("Select the leader cards that you want to keep:\n");
                int index = 1;
                for (LeaderCard card : getView().getModel().getLeaderCards().keySet()) {
                    getView().getRenderer().renderLeaderCard(card, index);
                    index++;
                }
            } else if (getView().getGameState() == GameState.PLAYING) {
                System.out.println("Choose an action:");
            }
        } else {
            getView().setOwnTurn(false);
            System.out.println("It's " + playerName + " turn");
        }
    }

    @Override
    public void updateMarket(int index, List<Resource> changes) {
        if (index >= 4) {
            getView().getModel().updateMarketRow(index - 4, changes);
        } else getView().getModel().updateMarketColumn(index, changes);
    }

    @Override
    public void insertDrawnResources() {
        //TODO implementa metodo
        System.out.println("OK");
        System.out.flush();
    }
}
