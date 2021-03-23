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

import java.util.List;

public class GameController {
    private Game game;
    private int eventNum;

    public GameController() {
        eventNum = 0;
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
        //view scelta carte
    }

    public void handlePlayerAction(PlayerActionEvent event) {
        if(event instanceof SelectLeadersPlayerActionEvent) {
            eventNum++;
            event.getPlayer().setLeaderCards(((SelectLeadersPlayerActionEvent) event).getSelectedLeaders());
            if (eventNum >= game.getPlayerNum()) {
                //primo turno giocatore (view)
            }
        } else if(event instanceof MarketPlayerActionEvent) {
            MarketPlayerActionEvent marketEvent = (MarketPlayerActionEvent) event;
            marketEvent.setResult(useMarket(marketEvent.getPlayer(), marketEvent.getSelected(), marketEvent.getWhiteConversion()));
            // view vede risultati
            // player deve disporre resources
        } else if(event instanceof ProductionPlayerActionEvent){
        }
    }

    public int calculateScore(Player player) {
        return 0;
    }

    public void exit() {

    }

    private List<Resource> useMarket(Player player, int selection, Resource whiteConversion) {
        return null;
    }

    private List<Resource> useProduction(Player player, Card card) {
        return null;
    }

    private void useBaseProduction(Player player, Resource[] baseProductionInput, Resource baseProductionOutput) {

    }

    private boolean buyDevelopmentCard(Player player, DevelopmentCard leader) {
        return false;
    }

    private boolean activateLeaderCard(Player player, LeaderCard leader) {
        return false;
    }

    private void endTurn(Player player) {

    }

    private void exitGame() {

    }
}
