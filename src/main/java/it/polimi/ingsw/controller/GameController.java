package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.event.MarketPlayerActionEvent;
import it.polimi.ingsw.controller.event.PlayerActionEvent;
import it.polimi.ingsw.controller.event.ProductionPlayerActionEvent;
import it.polimi.ingsw.controller.event.SelectLeadersPlayerActionEvent;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;

import java.util.List;
import java.util.Map;

public class GameController implements Observer<PlayerActionEvent> {
    private final Game game;

    public GameController() {
        game = new Game();
    }

    public Game getGame() {
        return game;
    }

    public void addPlayer(Player player) {
        game.addPlayer(player);
    }

    public void start() {
        game.randomSortPlayers();
        for (int i = 0; i < game.getPlayerNum(); i++) {
            List<LeaderCard> draw = game.getDeck().initialDrawLeaders();
            game.getPlayerAt(i).setLeaderCards(draw);
        }
        game.setCurrentPlayer(game.getPlayerAt(0));
    }

    public void activateLeaderCard(String playerName, LeaderCard leader) {

    }

    public void buyDevelopmentCard(String playerName, DevelopmentCard leader, int slot) {

    }

    public void updatePlayerDeposit(String playerName, Map<Integer, List<Resource>> changes) {

    }

    public void discardLeader(String playerName, LeaderCard card) {

    }

    public void endTurn(String playerName) {

    }

    public void useMarket(String playerName, int selection, Resource whiteConversion) {

    }

    public void useBaseProduction(String playerName, Resource[] baseProductionInput, Resource baseProductionOutput) {

    }

    public void useProduction(String playerName, Card card) {

    }

    public void selectInitialLeaders(String playerName, List<LeaderCard> cards) {

    }




    public int calculateScore(Player player) {
        return 0;
    }

    public void exit() {

    }

    private void exitGame() {

    }




    @Override
    public void update(PlayerActionEvent event) {
        event.process(this);
    }
}
