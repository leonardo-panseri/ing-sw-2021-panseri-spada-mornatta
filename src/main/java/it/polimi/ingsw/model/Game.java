package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class Game {
    private int[] popeReports;
    private int[] popeFavourValues;
    private Market market;
    private List<Player> players;
    private Deck deck;
    private Player currentPlayer;
    private Lorenzo lorenzo;

    public void start() {

    }

    public void addNewPlayer() {

    }

    public void removePlayer(Player player) {

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Deck getDeck() {
        return deck;
    }

    public Market getMarket() {
        return market;
    }

    public void nextPlayer() {

    }

    public void createDeck() {

    }

    public void createMarket() {

    }
}
