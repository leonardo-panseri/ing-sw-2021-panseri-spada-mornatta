package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Map;

public class PlayerController {
    private final GameController gameController;

    public PlayerController(GameController gameController) {
        this.gameController = gameController;
    }

    public void activateLeaderCard(Player player, LeaderCard leader) {

    }

    public void buyDevelopmentCard(Player player, DevelopmentCard leader, int slot) {

    }

    public void updatePlayerDeposit(Player player, Map<Integer, List<Resource>> changes) {

    }

    public void discardLeader(Player player, LeaderCard card) {

    }

    public void useMarket(Player player, int selection, Resource whiteConversion) {

    }

    public void useBaseProduction(Player player, Resource[] baseProductionInput, Resource baseProductionOutput) {

    }

    public void useProduction(Player player, Card card) {

    }

    public void selectInitialLeaders(Player player, List<LeaderCard> cards) {

    }
}
