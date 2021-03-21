package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.event.PlayerActionEvent;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class GameController {
    private Game game;

    public GameController() {

    }

    public void initializeGame() {
        game = new Game();
        game.createDeck();
        game.createMarket();
    }

    public void addPlayer(Player player) {
        game.addPlayer(player);
    }

    public void start() {
        game.randomSortPlayers();
        game.setCurrentPlayer(game.getPlayerAt(0));
        for(int i=0; i<game.getPlayerNum(); i++){
            List<LeaderCard> draw = game.getDeck().initialDrawLeaders();
            game.getPlayerAt(i).setLeaderCards(draw);
        }
    }

    public void handlePlayerAction(PlayerActionEvent event) {

    }

    public int calculateScore(Player player) {

    }

    public void exit() {

    }

    private List<Resource> useMarket(Player player, int selection, Resource whiteConversion) {

    }

    private List<Resource> useProduction(Player player, Card card) {

    }

    private void useBaseProduction(Player player, Resource[] baseProductionInput, Resource baseProductionOutput) {

    }

    private boolean buyDevelopmentCard(Player player, DevelopmentCard leader) {

    }

    private boolean activateLeaderCard(Player player, LeaderCard leader) {

    }

    private void endTurn(Player player) {

    }

    private void exitGame() {

    }
}
