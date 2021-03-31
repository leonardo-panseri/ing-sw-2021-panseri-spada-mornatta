package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.PropertyUpdate;
import it.polimi.ingsw.view.event.TurnUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class representing the game state, contains references to all other game model objects. Notifies all registered
 * observer of state updates.
 */
public class Game extends Observable<PropertyUpdate> {
    private static final int[] popeReports = {8, 16, 24};
    private static final int[] popeFavourValues = {2, 3, 4};
    private Market market;
    private List<Player> players;
    private Deck deck;
    private Player currentPlayer;
    private Lorenzo lorenzo;

    /**
     * Constructs a new game, instantiating a new {@link Deck} and a new {@link Market}.
     */
    public Game() {
        deck = new Deck();
        market = new Market();
        players = new ArrayList<>();
    }

    /**
     * Gets the singleplayer opponent.
     *
     * @return the lorenzo that is the current singleplayer opponent, if the game is not singleplayer return
     *         <code>null</code>
     */
    public Lorenzo getLorenzo() {
        return lorenzo;
    }

    /**
     * Adds the given {@link Player} to the game.
     *
     * @param player the player that will be added to the game
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes the given {@link Player} from the game. Does nothing if the player is not in the game.
     *
     * @param player the player that will be removed from the game
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Gets the current {@link Player}.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current {@link Player}.
     *
     * @param currentPlayer the player that will be set as current player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        notify(new TurnUpdate(currentPlayer.getNick()));
    }

    /**
     * Gets the deck.
     *
     * @return the deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the market.
     *
     * @return the market
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Gets the player at the given index in the players list.
     *
     * @param index the position of the player in the player list
     * @return the player in the given position
     */
    public Player getPlayerAt(int index) {
        return players.get(index);
    }

    /**
     * Gets the number of players.
     *
     * @return the number of players
     */
    public int getPlayerNum() {
        return players.size();
    }

    /**
     * Passes the turn to the next player, if the current player is the last in the players list restarts from the
     * first player.
     */
    public void nextPlayer() {
        int nextIndex = players.indexOf(currentPlayer) + 1;
        if (nextIndex >= players.size()) nextIndex = 0;
        currentPlayer = players.get(nextIndex);
    }

    /**
     * Creates the singleplayer opponent.
     */
    public Lorenzo createLorenzo() {
        lorenzo = new Lorenzo();
        return lorenzo;
    }

    /**
     * Randomizes players list order.
     */
    public void randomSortPlayers() {
        Collections.shuffle(players);
    }
}
