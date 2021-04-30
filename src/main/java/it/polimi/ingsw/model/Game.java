package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.lorenzo.Lorenzo;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.IServerPacket;

import java.util.*;

/**
 * Model class representing the game state, contains references to all other game model objects. Notifies all registered
 * observer of state updates.
 */
public class Game extends Observable<IServerPacket> {
    /**
     * Map correlating pope report slot in the faith track with:
     * <ol><li>victory points awarded to players in range</li><li>range of the pope report</li></ol>
     */
    private final Map<Integer, List<Integer>> popeReports = new HashMap<>(Map.of(8, Arrays.asList(2, 4), 16, Arrays.asList(3, 5), 24, Arrays.asList(4, 6)));
    private final Map<Integer, Integer> faithTrackPoints = Map.of(3, 1, 6, 2, 9, 4, 12, 6, 15, 9, 18, 12, 21, 16, 24, 20);
    private final Market market;
    private final List<Player> players;
    private final Deck deck;
    private Player currentPlayer;
    private Lorenzo lorenzo;

    private GamePhase gamePhase = GamePhase.SELECTING_LEADERS;

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
    public synchronized Lorenzo getLorenzo() {
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
     * Gets the Player with the given name.
     *
     * @param playerName the name of the player to search for
     * @return the player with the given name or <code>null</code> if not found
     */
    public synchronized Player getPlayerByName(String playerName) {
        Player result = null;
        for(Player player : players) {
            if(player.getNick().equals(playerName)) {
                result = player;
                break;
            }
        }
        return result;
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
    public synchronized Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current {@link Player}.
     *
     * @param currentPlayer the player that will be set as current player
     */
    public synchronized void setCurrentPlayer(Player currentPlayer) {
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
     * Gets all players.
     *
     * @return a list of players participating in this game
     */
    public List<Player> getPlayers() {
        return players;
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
     * Gets the current game phase.
     *
     * @return the current game phase
     */
    private GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets the current game phase.
     *
     * @param gamePhase the game phase to be set
     */
    public synchronized void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        notify(new GamePhaseUpdate(getGamePhase()));
    }

    /**
     * Sets the game to the last round game phase.
     *
     * @param player the player that caused this phase to start
     */
    public synchronized void startLastRound(Player player) {
        if(gamePhase != GamePhase.LAST_ROUND) {
            this.gamePhase = GamePhase.LAST_ROUND;
            notify(new LastRoundUpdate(player.getNick(), isLastPlayerTurn()));
        }
    }

    /**
     * Checks if it is the last round of the game.
     *
     * @return true if it is the last round, false otherwise
     */
    public synchronized boolean isLastRound() {
        return gamePhase == GamePhase.LAST_ROUND;
    }

    /**
     * Passes the turn to the next player, if the current player is the last in the players list restarts from the
     * first player.
     */
    public synchronized void nextPlayer() {
        int nextIndex = players.indexOf(currentPlayer) + 1;
        if (nextIndex >= players.size()) nextIndex = 0;
        setCurrentPlayer(players.get(nextIndex));
    }

    /**
     * Checks if it is the last player turn.
     *
     * @return true if it is the last player turn
     */
    public boolean isLastPlayerTurn() {
        return players.indexOf(currentPlayer) == players.size() - 1;
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

    /**
     * Terminates a game, sending scores and the winner name to Players.
     *
     * @param scores the final scores of this game
     * @param winnerName the winner name
     */
    public void terminate(Map<String, Integer> scores, String winnerName) {
        gamePhase = GamePhase.END;
        notify(new EndGameUpdate(scores, winnerName));
    }

    /**
     * Terminates a single player game, sending results to the Player.
     *
     * @param lorenzoWin boolean representing if Lorenzo has won
     * @param loseReason the reason why Lorenzo has won, if lorenzoWin is false, this will be ignored
     * @param playerScore the score of the Player, if lorenzoWin is true, this will be ignored
     */
    public void terminateSingleplayer(boolean lorenzoWin, String loseReason, int playerScore) {
        gamePhase = GamePhase.END;
        notify(new EndSingleplayerGameUpdate(lorenzoWin, loseReason, playerScore));
    }

    /**
     * Calculates victory points awarded by the given position in the faith track.
     *
     * @param faithPoints the progression in the faith track
     * @return the victory points awarded by the given position in the faith track
     */
    public int calculateFaithTrackVictoryPoints(int faithPoints) {
        for(int i = faithPoints; i > 0; i--) {
            if(faithTrackPoints.containsKey(i)) return faithTrackPoints.get(i);
        }
        return 0;
    }

    /**
     * Checks if there is a pope report slot before the given position in the faith track.
     *
     * @param faithPoints the position in the faith track to check from
     * @return the index of the pope report slot if found, <code>-1</code> otherwise
     */
    public int checkForPopeReportSlot(int faithPoints) {
        for(int i = faithPoints; i > 0; i--)
            if(popeReports.containsKey(i)) return i;
        return -1;
    }

    /**
     * Activates the pope report action that awards.
     *
     * @param popeReportSlot the pope report slot that caused the activation
     */
    public void activatePopeReport(int popeReportSlot) {
        if(!popeReports.containsKey(popeReportSlot))
            return;
        int minSlot = popeReportSlot - popeReports.get(popeReportSlot).get(1) + 1;
        for(Player player : getPlayers()) {
            if(player.getFaithPoints() >= minSlot && player.getFaithPoints() <= popeReportSlot)
                player.addPopeFavours(popeReports.get(popeReportSlot).get(0));
        }
        popeReports.remove(popeReportSlot);
    }

    /**
     * Notifies the clients of a new global chat message.
     * @param sender the player that has sent the message
     * @param message the content of the message
     */
    public void notifyChatMessage(String sender, String message) {
        notify(new ChatUpdate(sender, message));
    }

    public void notifyInvalidAction(Player player, String message) {
        notify(new InvalidActionUpdate(player, message));
    }
}
