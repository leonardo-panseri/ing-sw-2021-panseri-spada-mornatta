package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.messages.production.Production;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the actions performed by each player during the game,
 * checking the legality of the moves and executing them.
 */
public class PlayerController {
    private final GameController gameController;

    /**
     * Constructor: create a new PlayerController with reference to the game controller
     * @param gameController the game controller
     */
    public PlayerController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Activates a leader card. If it's not the player's turn, prints an error and returns.
     * If the player does not meet the requirements, prints an error and returns.
     * @param player the player who wants to activate a card
     * @param leader the leader card to be activated
     */
    public synchronized void activateLeaderCard(Player player, LeaderCard leader) {
        if (!gameController.isPlaying(player)) {
            System.out.println("Not " + player.getNick() + "'s turn!");
            return;
        }
        if (!leader.canPlayerAfford(player)) {
            System.out.println(player.getNick() + " cannot afford the leader card!");
            return;
        }

        try {
            player.setLeaderActive(leader);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Make a player buy a development card. If the card is null, prints an error and returns.
     * If the player cannot afford the card, or does not have a space for the card, prints an error and returns.
     * Prior to buying the card, checks if the player has any discount granted by the leaders.
     * @param player the player who wants to buy the card
     * @param developmentCard the desired development card
     * @param slot the slot of the player's board where to stack the bought card
     */
    public synchronized void buyDevelopmentCard(Player player, DevelopmentCard developmentCard, int slot) {
        if (gameController.getGame().getDeck().getDevelopmentCardByUuid(developmentCard.getUuid()) == null) {
            System.out.println("The requested card is not in the deck!");
            return;
        }
        if (!developmentCard.canPlayerAfford(player)) {
            System.out.println(player.getNick() + " cannot afford the development card!");
            return;
        }
        if (!player.getBoard().canPlaceCardOfLevel(developmentCard.getLevel(), slot)) {
            System.out.println(player.getNick() + " does not have space for this kind of card!");
            return;
        }

        boolean endGame = player.getBoard().getNumberOfDevelopmentCards() > 6;

        //Check for leaders discounts
        Map<Resource, Integer> cost = new HashMap<>(developmentCard.getCost());
        for(Resource res: cost.keySet()) {
            int discount = player.numLeadersDiscount(res);
            cost.put(res, cost.get(res) - discount);
            if(cost.get(res) < 0) cost.put(res, 0);
        }
        player.getBoard().getDeposit().removeResources(cost); //Maybe let player decide from where to remove resources
        gameController.getGame().getDeck().removeBoughtCard(developmentCard);
        player.getBoard().addCard(slot, developmentCard);

        if (endGame)
            gameController.getGame().startLastRound(player);
    }

    /**
     * Updates a player deposit after storing a resource or moving rows, if the move is legal.
     * If it's not the player's turn, prints an error and returns.
     * @param player the owner of the deposit
     * @param changes a map containing the indexes of the changed rows and the new rows to replace them with
     * @param toBeStored the list of drawn resource still waiting to be stored
     * @param leadersDeposit the leaders deposits granted by some leaders
     */
    public synchronized void updatePlayerDeposit(Player player, Map<Integer, List<Resource>> changes, List<Resource> toBeStored, Map<Integer, List<Resource>> leadersDeposit) {
        if (!gameController.isPlaying(player)) {
            System.out.println("Not " + player.getNick() + "'s turn!");
            return;
        }

        try {
            player.getBoard().getDeposit().applyChanges(changes, toBeStored, leadersDeposit);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        player.getBoard().getDeposit().setMarketResults(toBeStored);

    }

    /**
     * Discards a leader from the player hand and gives the player a faith point.
     * If it's not the player's turn, prints an error and returns.
     * Also checks if the player has crossed a pope reports, or if the player has reached the end of the faith track
     * @param player the player that wants to discard a leader
     * @param card the card to be activated
     */
    public synchronized void discardLeader(Player player, LeaderCard card) {
        if (!gameController.isPlaying(player)) {
            System.out.println("Not " + player.getNick() + "'s turn!");
            return;
        }

        try {
            player.discardLeader(card);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        player.addFaithPoints(1);

        checkFaithPoints(player);
    }

    /**
     * Makes the player use the market giving him the drawn resources and shifting the market row/column.
     * Also, converts the white resources with the requested conversion resource, if specified.
     * If it's not the player's turn, prints an error and returns.
     * If the player does not have a leader that grants the selected white resource conversion,
     * prints an error and returns.
     * @param player the player that wants to use the market
     * @param selection the selected row/column
     * @param whiteConversions the list of requested conversion resources
     */
    public synchronized void useMarket(Player player, int selection, List<Resource> whiteConversions) {
        if (!gameController.isPlaying(player)) {
            System.out.println("Not " + player.getNick() + "'s turn!");
            return;
        }
        for(Resource whiteConversion : whiteConversions) {
            if (!player.hasLeaderWhiteConversion(whiteConversion)) {
                System.err.println("Player " + player.getNick() + " does not have this white conversion special ability");
                return;
            }
        }

        List<Resource> marketResults = new ArrayList<>();
        if (selection >= 0 && selection < 4) {
            marketResults.addAll(gameController.getGame().getMarket().getColumn(selection));
            gameController.getGame().getMarket().shiftColumn(selection);
        } else if (selection >= 4 && selection < 7) {
            int row = selection - 4;
            marketResults.addAll(gameController.getGame().getMarket().getRow(row));
            gameController.getGame().getMarket().shiftRow(row);
        }


        int faithIncrement = 0;
        int whiteConversionIndex = 0;
        for (int i = marketResults.size() - 1; i >= 0; i--) {
            if (marketResults.get(i) == null) {
                if (whiteConversionIndex < whiteConversions.size()) {
                    marketResults.set(i, whiteConversions.get(whiteConversionIndex));
                    whiteConversionIndex++;
                } else
                    marketResults.remove(i);
            }
            else if (marketResults.get(i) == Resource.FAITH) {
                faithIncrement++;
                marketResults.remove(i);
            }
        }
        if (faithIncrement > 0) {
            player.addFaithPoints(faithIncrement);
            checkFaithPoints(player);
        }

        player.getBoard().getDeposit().setMarketResults(marketResults);
    }

    /**
     * Performs the selected various productions calling the appropriate method for each one.
     * If an error occurs during a production, prints an error message and skips the production.
     * At the end, adds the sum of faith points to the player,
     * checks if the player crossed a pope report or made the game end.
     * Eventually, stores the obtained resources in the strongbox.
     * @param player the player that wants to perform the productions
     * @param productions the list of productions
     */
    public synchronized void useProductions(Player player, List<Production> productions) {
        Map<Resource, Integer> result = new HashMap<>();
        for (Production production : productions) {
            try {
                Map<Resource, Integer> productionResult = production.use(gameController, player);
                productionResult.forEach(
                        (resource, quantity) -> result.merge(resource, quantity, Integer::sum));
            } catch (IllegalArgumentException e) {
                System.err.println("Error during production: " + production + "\nMessage: " + e.getMessage());
            }
        }
        if (result.containsKey(Resource.FAITH)) {
            int faithToAdd = result.get(Resource.FAITH);
            result.remove(Resource.FAITH);
            player.addFaithPoints(faithToAdd);
            checkFaithPoints(player);
        }
        player.getBoard().getDeposit().addMultipleToStrongbox(result);
    }

    /**
     * Executes a player board production with the given input and return the desired output resource.
     * If it's not the player's turn, prints an error and returns.
     * If the player has specified less then two inputs, prints an error and returns.
     * If the player has not specified an output resource, prints an error and returns.
     * If the player has not enough resource to complete the production, prints an error and returns.
     * @param player the player that uses the production
     * @param baseProductionInput the two production input resources
     * @param baseProductionOutput the output resource
     * @return a map containing the output resource
     */
    public synchronized Map<Resource, Integer> useBaseProduction(Player player, List<Resource> baseProductionInput, Resource baseProductionOutput) {
        if (!gameController.isPlaying(player)) {
            throw new IllegalArgumentException("Not " + player.getNick() + "'s turn!");
        }
        if (baseProductionInput.size() != 2) {
            throw new IllegalArgumentException("Wrong base production format!");
        }
        if (baseProductionOutput == null) {
            throw new IllegalArgumentException("Wrong base production format!");
        }

        Map<Resource, Integer> cost = new HashMap<>();
        if (baseProductionInput.get(0) == baseProductionInput.get(1)) {
            Resource resource = baseProductionInput.get(0);
            if (player.getBoard().getDeposit().getAmountOfResource(resource) < 2) {
                throw new IllegalArgumentException(player.getNick() + " does not have enough " + resource);
            }

            cost.put(resource, 2);
        } else {
            Resource resource1 = baseProductionInput.get(0);
            Resource resource2 = baseProductionInput.get(1);
            if (player.getBoard().getDeposit().getAmountOfResource(resource1) < 1) {
                throw new IllegalArgumentException(player.getNick() + " does not have enough " + resource1);
            }
            if (player.getBoard().getDeposit().getAmountOfResource(resource2) < 1) {
                throw new IllegalArgumentException(player.getNick() + " does not have enough " + resource2);
            }

            cost.put(resource1, 1);
            cost.put(resource2, 1);
        }

        player.getBoard().getDeposit().removeResources(cost); //TODO let player decide from where to remove resources

        Map<Resource, Integer> result = new HashMap<>();
        result.put(baseProductionOutput, 1);
        return result;
    }

    public synchronized Map<Resource, Integer> useDevelopmentProduction(Player player, DevelopmentCard card) {
        if (!gameController.isPlaying(player)) {
            throw new IllegalArgumentException("Not " + player.getNick() + "'s turn!");
        }
        if (card == null) {
            throw new IllegalArgumentException("The requested card is not at the top of one of the player's slots!");
        }

        Map<Resource, Integer> input = card.getProductionInput();
        Map<Resource, Integer> output = card.getProductionOutput();

        for (Resource res : input.keySet()) {
            if (player.getBoard().getDeposit().getAmountOfResource(res) < input.get(res)) {
                throw new IllegalArgumentException(player.getNick() + " does not have enough " + res);
            }
        }

        player.getBoard().getDeposit().removeResources(input); //TODO let player decide from where to remove resources

        if (output.containsKey(Resource.FAITH)) {
            player.addFaithPoints(output.get(Resource.FAITH));
            output.remove(Resource.FAITH);
        }
        return output;
    }

    public synchronized Map<Resource, Integer> useLeaderProduction(Player player, LeaderCard card, Resource output) {
        if (!gameController.isPlaying(player)) {
            throw new IllegalArgumentException("Not " + player.getNick() + "'s turn!");
        }
        if (card == null) {
            throw new IllegalArgumentException("The requested card is not in the player board!");
        }
        if (!player.isLeaderActive(card))
            throw new IllegalArgumentException("This leader card is not active");
        if (output == null || output == Resource.FAITH) {
            throw new IllegalArgumentException("Incorrect required resource type");
        }

        if (!card.getSpecialAbility().getType().equals(SpecialAbilityType.PRODUCTION)) {
            throw new IllegalArgumentException("The selected leader does not have a production power!");
        }

        Resource cost = card.getSpecialAbility().getTargetResource();
        if (player.getBoard().getDeposit().getAmountOfResource(cost) < 1) {
            throw new IllegalArgumentException(player.getNick() + " does not have enough " + cost);
        }

        player.getBoard().getDeposit().removeResources(Map.of(cost, 1)); //TODO let player decide from where to remove resources

        Map<Resource, Integer> result = new HashMap<>();
        result.put(Resource.FAITH, 1);
        result.put(output, 1);
        return result;
    }

    public synchronized void selectInitialLeaders(Player player, List<LeaderCard> cards) {
        if (cards.size() != 2) {
            System.out.println("Wrong amount of selected leaders!");
        }

        try {
            player.keepLeaders(cards);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        if (gameController.getGame().isLastPlayerTurn())
            gameController.getGame().setGamePhase(GamePhase.PLAYING);
        gameController.getGame().nextPlayer();
    }

    void checkFaithPoints(Player player) {
        int popeReportSlot = gameController.getGame().checkForPopeReportSlot(player.getFaithPoints());
        if(popeReportSlot != -1)
            gameController.getGame().activatePopeReport(popeReportSlot);
        if(player.getFaithPoints() > 23)
            gameController.getGame().startLastRound(player);
    }

    public synchronized void sendChatMessage(String sender, String message) {
        gameController.getGame().notifyChatMessage(sender, message);
    }
}
