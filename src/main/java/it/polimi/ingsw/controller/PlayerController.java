package it.polimi.ingsw.controller;

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

    //TODO Maybe check nullity for all params
    //TODO Send errors to player

    public void activateLeaderCard(Player player, LeaderCard leader) {
        if(leader.canPlayerAfford(player)) {
            try {
                player.setLeaderActive(leader);
            } catch (IllegalArgumentException e) {

            }
        } else {

        }
    }

    public void buyDevelopmentCard(Player player, DevelopmentCard developmentCard, int slot) {
        if(gameController.getGame().getDeck().getDevelopmentCardByUuid(developmentCard.getUuid()) != null) {
            if (developmentCard.canPlayerAfford(player)) {
                if (player.getBoard().canPlaceCardOfLevel(developmentCard.getLevel(), slot)) {
                    boolean endGame = false;
                    if(player.getBoard().getNumberOfDevelopmentCards() == 6)
                        endGame = true;

                    player.getBoard().getDeposit().removeResources(developmentCard.getCost());
                } else {

                }
            } else {

            }
        } else {

        }
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
