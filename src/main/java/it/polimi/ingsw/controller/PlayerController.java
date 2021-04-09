package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
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
        if(!gameController.isPlaying(player)) {

            return;
        }
        if(!leader.canPlayerAfford(player)) {

            return;
        }

        try {
            player.setLeaderActive(leader);
        } catch (IllegalArgumentException e) {

        }
    }

    public void buyDevelopmentCard(Player player, DevelopmentCard developmentCard, int slot) {
        if(gameController.getGame().getDeck().getDevelopmentCardByUuid(developmentCard.getUuid()) == null) {

            return;
        }
        if(!developmentCard.canPlayerAfford(player)) {

            return;
        }
        if(!player.getBoard().canPlaceCardOfLevel(developmentCard.getLevel(), slot)) {

            return;
        }

        boolean endGame = false;
        if(player.getBoard().getNumberOfDevelopmentCards() == 6)
            endGame = true;

        player.getBoard().getDeposit().removeResources(developmentCard.getCost()); //Maybe let player decide from where to remove resources
        gameController.getGame().getDeck().removeBoughtCard(developmentCard);
        player.getBoard().addCard(slot, developmentCard);

        if(endGame)
            gameController.endGame();
    }

    public void updatePlayerDeposit(Player player, Map<Integer, List<Resource>> changes) {
        if(!gameController.isPlaying(player)) {

            return;
        }

        try {
            player.getBoard().getDeposit().applyChanges(changes);
        } catch (IllegalArgumentException e) {

        }

    }

    public void discardLeader(Player player, LeaderCard card) {
        if(!gameController.isPlaying(player)) {

            return;
        }

        try {
            player.discardLeader(card);
        } catch (IllegalArgumentException e) {

        }
    }

    public void useMarket(Player player, int selection, Resource whiteConversion) {
        if(!gameController.isPlaying(player)) {

            return;
        }

        List<Resource> marketResults = new ArrayList<>();
        if(selection >= 0 && selection < 4) {
            int column = selection;
            marketResults.addAll(gameController.getGame().getMarket().getColumn(column));
            gameController.getGame().getMarket().shiftColumn(column);
        } else if(selection >= 4 && selection < 7) {
            int row = selection - 4;
            marketResults.addAll(gameController.getGame().getMarket().getRow(row));
            gameController.getGame().getMarket().shiftRow(row);
        }

        for(int i = marketResults.size() - 1; i >= 0; i--) {
            if(marketResults.get(i) == null) {
                if(whiteConversion != null)
                    marketResults.set(i, whiteConversion);
                else
                    marketResults.remove(i);
            }
        }
        player.getBoard().setMarketResults(marketResults);
    }

    public void useBaseProduction(Player player, Resource[] baseProductionInput, Resource baseProductionOutput) {
        if(!gameController.isPlaying(player)) {

            return;
        }
        if(baseProductionInput.length != 2) {

            return;
        }

        Map<Resource, Integer> cost = new HashMap<>();
        if(baseProductionInput[0] == baseProductionInput[1]) {
            Resource resource = baseProductionInput[0];
            if(player.getBoard().getDeposit().getAmountOfResource(resource) < 2) {

                return;
            }

            cost.put(resource, 2);
        } else {
            Resource resource1 = baseProductionInput[0];
            Resource resource2 = baseProductionInput[1];
            if(player.getBoard().getDeposit().getAmountOfResource(resource1) < 1) {

                return;
            }
            if(player.getBoard().getDeposit().getAmountOfResource(resource2) < 1) {

                return;
            }

            cost.put(resource1, 1);
            cost.put(resource2, 1);
        }

        player.getBoard().getDeposit().removeResources(cost); //Maybe let player decide from where to remove resources
        player.getBoard().getDeposit().addToStrongbox(baseProductionOutput);
    }

    public void useProduction(Player player, Card card) {
        if(!gameController.isPlaying(player)) {

            return;
        }

    }

    public void selectInitialLeaders(Player player, List<LeaderCard> cards) {

    }
}
