package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.List;
import java.util.Map;

public class PlayerController {
    private final GameController gameController;

    public PlayerController(GameController gameController) {
        this.gameController = gameController;
    }

    public void activateLeaderCard(String playerName, LeaderCard leader) {

    }

    public void buyDevelopmentCard(String playerName, DevelopmentCard leader, int slot) {

    }

    public void updatePlayerDeposit(String playerName, Map<Integer, List<Resource>> changes) {

    }

    public void discardLeader(String playerName, LeaderCard card) {

    }

    public void useMarket(String playerName, int selection, Resource whiteConversion) {

    }

    public void useBaseProduction(String playerName, Resource[] baseProductionInput, Resource baseProductionOutput) {

    }

    public void useProduction(String playerName, Card card) {

    }

    public void selectInitialLeaders(String playerName, List<LeaderCard> cards) {

    }
}
