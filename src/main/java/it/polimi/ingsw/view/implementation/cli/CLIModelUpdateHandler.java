package it.polimi.ingsw.view.implementation.cli;

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
                    getView().getRenderer().printOwnLeaders();
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
            getView().getRenderer().showGameMessage(ViewString.OWN_TURN);
            if (getView().getGameState() == GameState.WAIT_SELECT_LEADERS) {
                getView().setGameState(GameState.SELECT_LEADERS);
                getView().getRenderer().showGameMessage(ViewString.SELECT_LEADERS);
                getView().getRenderer().printOwnLeaders();
            } else if (getView().getGameState() == GameState.PLAYING) {
                getView().getRenderer().showGameMessage(ViewString.CHOOSE_ACTION);
            }
        } else {
            getView().setOwnTurn(false);
            String italicizedPlayerName = AnsiColor.italicize(playerName) + AnsiColor.BLUE;
            getView().getRenderer().showGameMessage(ViewString.OTHER_TURN.formatted(italicizedPlayerName));
        }
    }

    @Override
    public void updateMarket(int index, List<Resource> changes) {
        if (index >= 4) {
            getView().getModel().updateMarketRow(index - 4, changes);
        } else getView().getModel().updateMarketColumn(index, changes);
    }

    @Override
    public void updateFaith(String playerName, int faithPoints, int popeFavours) {
        if (playerName.equals(getView().getPlayerName())) {
            getView().getModel().setFaithPoints(faithPoints);
            getView().getModel().setPopeFavours(popeFavours);
        }
        else {
            getView().getModel().setOtherFaith(playerName, faithPoints);
            getView().getModel().setOtherFavours(playerName, popeFavours);
        }
    }

    @Override
    public void insertDrawnResources(String playerName, List<Resource> result) {
        if (playerName.equals((getView().getPlayerName()))) {
            getView().getModel().setMarketResult(result);
            getView().getRenderer().printMarketResult();
        }
    }
}
