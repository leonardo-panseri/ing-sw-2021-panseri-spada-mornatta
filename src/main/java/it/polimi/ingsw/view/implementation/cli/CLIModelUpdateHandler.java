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

import java.util.ArrayList;
import java.util.Arrays;
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
            if(getView().getGameState() == GameState.PLAYING) {
                if(ownedLeaders.size() == getView().getModel().getLeaderCards().size())
                    getView().getRenderer().showGameMessage("Leader card successfully activated");
                else
                    getView().getRenderer().showGameMessage("Leader card discarded (+1 Faith)");
            }
            getView().getModel().setLeaderCards(ownedLeaders);
        } else getView().getModel().setOthersLeaderCards(playerName, ownedLeaders);
    }

    @Override
    public void updateDevelopmentCards(String playerName, DevelopmentCard card, int slot) {
        if (playerName.equals(getView().getPlayerName())) {
            getView().getModel().setNewDevelopmentCard(card, slot);
        } else {
            getView().getModel().setOtherNewDevelopment(playerName, card, slot);
        }
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
    public void updateDeposit(String playerName, Map<Integer, List<Resource>> changes, Map<Integer, List<Resource>> leadersDeposit) {
        List<List<Resource>> deposit;
        if(playerName.equals(getView().getPlayerName())){
            deposit = getView().getModel().getDeposit();

            for (Integer i : changes.keySet()) {
                deposit.set(i - 1, changes.get(i));
            }

            getView().getModel().setLeadersDeposit(leadersDeposit);
        } else {
            if(!getView().getModel().getOtherDeposit().containsKey(playerName)){
                getView().getModel().getOtherDeposit().put(playerName, new ArrayList<>(
                        Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>())));
            }
            deposit = getView().getModel().getOtherDeposit().get(playerName);

            for (Integer i : changes.keySet()) {
                deposit.set(i - 1, changes.get(i));
            }

            getView().getModel().getOtherLeadersDeposit().put(playerName, leadersDeposit);
        }

    }

    @Override
    public void updateStrongbox(String playerName, Map<Resource, Integer> strongbox) {
        if(playerName.equals(getView().getPlayerName())) {
            getView().getModel().setStrongbox(strongbox);
        }
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
            if(result.size() > 0)
                getView().getRenderer().printMarketResult();
        }
    }
}