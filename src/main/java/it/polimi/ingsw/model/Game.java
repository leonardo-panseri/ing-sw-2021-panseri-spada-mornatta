package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.event.PlayerActionEvent;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Game extends Observable<PropertyUpdate> {
    private static final int[] popeReports = {8, 16, 24};
    private static final int[] popeFavourValues = {2, 3, 4};
    private Market market;
    private List<Player> players;
    private Deck deck;
    private Player currentPlayer;
    private Lorenzo lorenzo;

    public Game() {
        deck = new Deck();
        market = new Market();
    }

    public Lorenzo getLorenzo() {
        return lorenzo;
    }

    public void addPlayer(Player player) {
        if (players == null) players = new ArrayList<>();
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
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

    public Player getPlayerAt(int index) {
        return players.get(index);
    }

    public int getPlayerNum() {
        return players.size();
    }

    public void nextPlayer() {
        int nextIndex = players.indexOf(currentPlayer) + 1;
        if (nextIndex >= players.size()) nextIndex = 0;
        currentPlayer = players.get(nextIndex);
    }

    public void createLorenzo() {
        lorenzo = new Lorenzo();
    }

    public void randomSortPlayers() {
        Collections.shuffle(players);
    }
}
