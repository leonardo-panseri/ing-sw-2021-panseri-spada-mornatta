package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.FaithUpdate;
import it.polimi.ingsw.view.event.OwnedLeadersUpdate;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models a player, composed by a nickname, faith points indicating the current position in the faith track,
 * the amount of pope favours activated, a board and a map containing the possessed leaders and a boolean true if the
 * leader is active.
 */
public class Player extends Observable<PropertyUpdate> {
    private String nick;
    private int faithPoints;
    private int popeFavours;
    private Map<LeaderCard, Boolean> leaderCards;
    private PlayerBoard board;

    /**
     * Constructor: creates a new Player with the given nick.
     *
     * @param nick a string with the nick
     */
    public Player(String nick) {
        this.nick = nick;
        faithPoints = 0;
        popeFavours = 0;
        leaderCards = new HashMap<>();
        board = new PlayerBoard(this);
    }

    /**
     * Getter for the nickname of the player.
     *
     * @return a string with the nick
     */
    public String getNick() {
        return nick;
    }

    public int getFaithPoints() {
        return faithPoints;
    }

    /**
     * Getter for the board of the player.
     *
     * @return a board
     */
    public PlayerBoard getBoard() {
        return board;
    }

    /**
     * Setter for the faith points of the player.
     *
     * @param faithPoints the position in the faith track
     */
    public void setFaithPoints(int faithPoints) {

        this.faithPoints = faithPoints;
        notify(new FaithUpdate(this.nick, this.faithPoints, this.popeFavours));
    }

    /**
     * Getter for the pope favours of the player.
     *
     * @return the amount of pope favours activated
     */
    public int getPopeFavours() {
        return popeFavours;
    }

    /**
     * Setter for the pope favours amount of the player.
     *
     * @param popeFavours the new amount of pope favours
     */
    public void setPopeFavours(int popeFavours) {

        this.popeFavours = popeFavours;
        notify(new FaithUpdate(this.nick, this.faithPoints, this.popeFavours));
    }

    /**
     * Activates a leader card possessed by the player by putting the true value in the card map.
     *
     * @param leaderCard a leader card to be activated
     */
    public void setLeaderActive(LeaderCard leaderCard) {
        leaderCards.put(leaderCard, true);
    }

    /**
     * Setter for the leader cards of the player.
     *
     * @param cards a new list of leader cards of the player.
     */
    public void setLeaderCards(List<LeaderCard> cards) {
        for (LeaderCard leaderCard : cards) {
            leaderCards.put(leaderCard, false);
        }
        notify(new OwnedLeadersUpdate(this, leaderCards));
    }
}
