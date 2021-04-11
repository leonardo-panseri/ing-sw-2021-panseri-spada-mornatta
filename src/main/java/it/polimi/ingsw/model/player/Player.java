package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.FaithUpdate;
import it.polimi.ingsw.view.event.OwnedLeadersUpdate;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.*;

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
     * Getter for Leadercards
     *
     * @return a Map containing the LeaderCards
     */
    public Map<LeaderCard, Boolean> getLeaderCards() {
        return leaderCards;
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
     * @throws IllegalArgumentException if the given leader card is not present in this player board or if it is
     *                                  already active
     */
    public void setLeaderActive(LeaderCard leaderCard) throws IllegalArgumentException {
        if(!leaderCards.containsKey(leaderCard))
            throw new IllegalArgumentException("leadercard_not_present");
        if(leaderCards.get(leaderCard))
            throw new IllegalArgumentException("leadercard_already_active");

        leaderCards.put(leaderCard, true);
        notify(new OwnedLeadersUpdate(getNick(), leaderCards));
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
        notify(new OwnedLeadersUpdate(this.getNick(), leaderCards));
    }

    /**
     * Adds the given amount of faith points to this player.
     *
     * @param faithPoints the amount of faith points to add
     */
    public void addFaithPoints(int faithPoints) {
        this.faithPoints += faithPoints;
        notify(new FaithUpdate(getNick(), faithPoints, popeFavours));
    }

    /**
     * Discards the given LeaderCard from this player hand.
     *
     * @param card the leader card that will be discarded
     * @throws IllegalArgumentException if the player does not have this leader card in his hand or if this card is
     *                                  already active
     */
    public void discardLeader(LeaderCard card) throws IllegalArgumentException {
        if(!leaderCards.containsKey(card))
            throw new IllegalArgumentException("leadercard_not_present");
        if(leaderCards.get(card))
            throw new IllegalArgumentException("leadercard_already_active");

        leaderCards.remove(card);
        notify(new OwnedLeadersUpdate(getNick(), leaderCards));
    }

    /**
     * Keeps the given leader cards and removes the others from the player's hand.
     *
     * @param selected the leader cards that the player wants to keep
     * @throws IllegalArgumentException if the selected cards contains one or more cards not possessed by the player
     */
    public void keepLeaders(List<LeaderCard> selected) throws IllegalArgumentException{
        for (LeaderCard card : leaderCards.keySet()) {
            if (!selected.contains(card)) {
                discardLeader(card);
                selected.remove(card);
            }
        }

        if (selected.size() > 0)
            throw new IllegalArgumentException("leadercard_not_possessed");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(nick, player.nick);
    }
}
