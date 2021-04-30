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
import it.polimi.ingsw.view.beans.MockPlayer;

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
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        if (player.isLocalPlayer()) {
            if(getView().getGameState() == GameState.PLAYING) {
                if(ownedLeaders.size() == player.getLeaderCards().size())
                    getView().getRenderer().showGameMessage("Leader card successfully activated");
                else
                    getView().getRenderer().showGameMessage("Leader card discarded (+1 Faith)");
            }
        }

        player.setLeaderCards(ownedLeaders);
    }

    @Override
    public void updateDevelopmentCards(String playerName, DevelopmentCard card, int slot) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.getPlayerBoard().setNewDevelopmentCard(card, slot);
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
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        for (Integer i : changes.keySet()) {
            player.getDeposit().setRow(i - 1, changes.get(i));
        }
    }

    @Override
    public void updateStrongbox(String playerName, Map<Resource, Integer> strongbox) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.getDeposit().setStrongbox(strongbox);
    }

    @Override
    public void updateFaith(String playerName, int faithPoints) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.setFaithPoints(faithPoints);
    }

    @Override
    public void updatePopeFavours(String playerName, int popeFavours) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.setPopeFavours(popeFavours);
    }

    @Override
    public void updateChat(String sender, String message) {
        getView().getRenderer().printChatMessage(sender, message);
    }

    @Override
    public void insertDrawnResources(String playerName, List<Resource> result) {
        MockPlayer player = getView().getModel().getPlayer(playerName);
        if (player == null) {
            player = getView().getModel().addPlayer(playerName, false);
        }

        player.getDeposit().setMarketResult(result);

        if (player.isLocalPlayer()) {
            getView().getRenderer().printMarketResult();
        }
    }
}
