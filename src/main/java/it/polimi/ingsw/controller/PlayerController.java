package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
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
            System.out.println("Not " + player.getNick() + " turn!");
            return;
        }
        if(!leader.canPlayerAfford(player)) {
            System.out.println(player.getNick() + " cannot afford the leader card!");
            return;
        }

        try {
            player.setLeaderActive(leader);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void buyDevelopmentCard(Player player, DevelopmentCard developmentCard, int slot) {
        if(gameController.getGame().getDeck().getDevelopmentCardByUuid(developmentCard.getUuid()) == null) {
            System.out.println("The requested card is not in the deck!");
            return;
        }
        if(!developmentCard.canPlayerAfford(player)) {
            System.out.println(player.getNick() + " cannot afford the development card!");
            return;
        }
        if(!player.getBoard().canPlaceCardOfLevel(developmentCard.getLevel(), slot)) {
            System.out.println(player.getNick() + " does not have space for this kind of card!");
            return;
        }

        boolean endGame = player.getBoard().getNumberOfDevelopmentCards() == 6;

        player.getBoard().getDeposit().removeResources(developmentCard.getCost()); //Maybe let player decide from where to remove resources
        gameController.getGame().getDeck().removeBoughtCard(developmentCard);
        player.getBoard().addCard(slot, developmentCard);

        if(endGame)
            gameController.endGame();
    }

    public void updatePlayerDeposit(Player player, Map<Integer, List<Resource>> changes) {
        if(!gameController.isPlaying(player)) {
            System.out.println("Not " + player.getNick() + " turn!");
            return;
        }

        try {
            player.getBoard().getDeposit().applyChanges(changes);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    public void discardLeader(Player player, LeaderCard card) {
        if(!gameController.isPlaying(player)) {
            System.out.println("Not " + player.getNick() + " turn!");
            return;
        }

        try {
            player.discardLeader(card);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void useMarket(Player player, int selection, Resource whiteConversion) {
        if(!gameController.isPlaying(player)) {
            System.out.println("Not " + player.getNick() + " turn!");
            return;
        }

        List<Resource> marketResults = new ArrayList<>();
        if(selection >= 0 && selection < 4) {
            marketResults.addAll(gameController.getGame().getMarket().getColumn(selection));
            gameController.getGame().getMarket().shiftColumn(selection);
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

    public void useBaseProduction(Player player, List<Resource> baseProductionInput, Resource baseProductionOutput) {
        if(!gameController.isPlaying(player)) {
            System.out.println("Not " + player.getNick() + " turn!");
            return;
        }
        if(baseProductionInput.size() != 2) {
            System.out.println("Wrong base production format!");
            return;
        }

        Map<Resource, Integer> cost = new HashMap<>();
        if(baseProductionInput.get(0) == baseProductionInput.get(1)) {
            Resource resource = baseProductionInput.get(0);
            if(player.getBoard().getDeposit().getAmountOfResource(resource) < 2) {
                System.out.println(player.getNick() + " does not have enough " + resource);
                return;
            }

            cost.put(resource, 2);
        } else {
            Resource resource1 = baseProductionInput.get(0);
            Resource resource2 = baseProductionInput.get(1);
            if(player.getBoard().getDeposit().getAmountOfResource(resource1) < 1) {
                System.out.println(player.getNick() + " does not have enough " + resource1);
                return;
            }
            if(player.getBoard().getDeposit().getAmountOfResource(resource2) < 1) {
                System.out.println(player.getNick() + " does not have enough " + resource2);
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
            System.out.println("Not " + player.getNick() + " turn!");
            return;
        }

        Map<Resource, Integer> cost = new HashMap<>();

        if (card instanceof DevelopmentCard) useDevelopmentProduction(player, (DevelopmentCard) card);
        else if (card instanceof LeaderCard) useLeaderProduction(player, (LeaderCard) card);
        else {
            System.out.println("Incorrect card format");
        }
    }

    public void useDevelopmentProduction(Player player, DevelopmentCard card) {
        Map<Resource, Integer> input = card.getProductionInput();
        Map<Resource, Integer> output =  card.getProductionOutput();

        player.getBoard().getDeposit().removeResources(input);
        if(output.containsKey(Resource.FAITH)) {
            player.addFaithPoints(output.get(Resource.FAITH));
            output.remove(Resource.FAITH);
        }
        player.getBoard().getDeposit().addMultipleToStrongbox(output);
    }

    public void useLeaderProduction(Player player, LeaderCard card) {
        //TODO Make sure the card is a production leader card
    }

    public void selectInitialLeaders(Player player, List<LeaderCard> cards) {

    }
}
