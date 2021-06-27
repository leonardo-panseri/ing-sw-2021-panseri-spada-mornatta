package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.model.messages.FaithUpdate;
import it.polimi.ingsw.model.messages.InitialTurnUpdate;
import it.polimi.ingsw.model.messages.OwnedLeadersUpdate;
import it.polimi.ingsw.model.messages.PopeFavourUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.server.IServerPacket;

import java.util.*;

/**
 * Models a player, composed by a nickname, faith points indicating the current position in the faith track,
 * the amount of pope favours activated, a board and a map containing the possessed leaders and a boolean true if the
 * leader is active.
 */
public class Player extends Observable<IServerPacket> {
    private final String nick;
    private int faithPoints;
    private int popeFavours;
    private Map<LeaderCard, Boolean> leaderCards;
    private final PlayerBoard board;
    private boolean hasAlreadyPlayed;

    private int initialResourcesToPick;

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
        hasAlreadyPlayed = false;
    }

    /**
     * Getter for Leadercards
     *
     * @return a Map containing the LeaderCards
     */
    Map<LeaderCard, Boolean> getLeaderCards() {
        return leaderCards;
    }


    /**
     * Getter for the nickname of the player.
     *
     * @return a string with the nick
     */
    public synchronized String getNick() {
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
    void setFaithPoints(int faithPoints) {
        this.faithPoints = faithPoints;
        notify(new FaithUpdate(this.nick, getFaithPoints()));
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
    void setPopeFavours(int popeFavours) {
        this.popeFavours = popeFavours;
        notify(new PopeFavourUpdate(this.nick, this.popeFavours));
    }

    /**
     * Gets the number of resources that this Player can pick in the first turn.
     *
     * @return the amount of initial resources to pick
     */
    public int getInitialResourcesToPick() {
        return initialResourcesToPick;
    }

    /**
     * Checks if the given LeaderCard is active.
     *
     * @param card the card to check
     * @return true if the card is active, false if it is not active or if it is not present
     */
    public boolean isLeaderActive(LeaderCard card) {
        return leaderCards.containsKey(card) && leaderCards.get(card);
    }

    /**
     * Activates a leader card possessed by the player by putting the true value in the card map.
     *
     * @param leaderCard a leader card to be activated
     * @throws IllegalArgumentException if the given leader card is not present in this player board or if it is
     *                                  already active
     */
    public void setLeaderActive(LeaderCard leaderCard) throws IllegalArgumentException {
        if (!leaderCards.containsKey(leaderCard))
            throw new IllegalArgumentException("You don't have this leader card!");
        if (leaderCards.get(leaderCard))
            throw new IllegalArgumentException("This leader card is already active!");

        leaderCards.put(leaderCard, true);
        notify(new OwnedLeadersUpdate(getNick(), leaderCards));
    }

    /**
     * Notifies this Player of the start of the game, sending him all necessary information.
     *
     * @param leaderCards      the initial leader cards to give to the player
     * @param resourcesToPick  the number of resources that the player can pick
     * @param faithPointsGiven the amount of faith points initially given to the player
     */
    public void notifyInitialTurn(List<LeaderCard> leaderCards, int resourcesToPick, int faithPointsGiven) {
        for (LeaderCard leaderCard : leaderCards) {
            this.leaderCards.put(leaderCard, false);
        }
        this.initialResourcesToPick = resourcesToPick;
        addFaithPoints(faithPointsGiven);
        notify(new InitialTurnUpdate(getNick(), getLeaderCards(), resourcesToPick));
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
     * Checks if the player has some leaders that grant a discount on the given resource.
     *
     * @param res the resource to be checked
     * @return the number of discounts on the given resource
     */
    public int numLeadersDiscount(Resource res) {
        int result = 0;
        for (LeaderCard card : leaderCards.keySet()) {
            if (isLeaderActive(card) && card.getSpecialAbility().getType() == SpecialAbilityType.DISCOUNT) {
                if (card.getSpecialAbility().getTargetResource() == res) result++;
            }
        }
        return result;
    }

    /**
     * Checks if the Player has an active leader card with the given white conversion.
     *
     * @param resource the resource to convert to
     * @return true if the player has this special ability, false otherwise
     */
    public boolean hasLeaderWhiteConversion(Resource resource) {
        for (LeaderCard card : leaderCards.keySet()) {
            if (isLeaderActive(card) && card.getSpecialAbility().getType() == SpecialAbilityType.EXCHANGE)
                if (card.getSpecialAbility().getTargetResource() == resource) return true;
        }
        return false;
    }

    /**
     * Checks if the Player has an active leader card with a deposit for the given resource.
     *
     * @param resourcesToCheck a list of resource to check for
     * @return true if the player has this special ability, false otherwise
     */
    public boolean hasLeaderDeposits(List<Resource> resourcesToCheck) {
        List<Resource> resources = new ArrayList<>(resourcesToCheck);
        for (LeaderCard card : leaderCards.keySet()) {
            if (isLeaderActive(card) && card.getSpecialAbility().getType() == SpecialAbilityType.DEPOT) {
                int indexFound = resources.lastIndexOf(card.getSpecialAbility().getTargetResource());
                if (indexFound != -1)
                    resources.remove(indexFound);
            }
        }
        return resources.isEmpty();
    }

    /**
     * Adds the given amount of faith points to this player.
     *
     * @param faithPoints the amount of faith points to add
     */
    public synchronized void addFaithPoints(int faithPoints) {
        this.faithPoints += faithPoints;
        if (this.faithPoints > 24) this.faithPoints = 24;
        notify(new FaithUpdate(getNick(), getFaithPoints()));
    }

    /**
     * Adds the given amount of pope favours to this player.
     *
     * @param popeFavours the amount of pope favours to add
     */
    public synchronized void addPopeFavours(int popeFavours) {
        this.popeFavours += popeFavours;
        notify(new PopeFavourUpdate(getNick(), getPopeFavours()));
    }

    /**
     * Discards the given LeaderCard from this player hand.
     *
     * @param card the leader card that will be discarded
     * @throws IllegalArgumentException if the player does not have this leader card in his hand or if this card is
     *                                  already active
     */
    public void discardLeader(LeaderCard card) throws IllegalArgumentException {
        if (!leaderCards.containsKey(card))
            throw new IllegalArgumentException("You don't have this leader card!");
        if (leaderCards.get(card))
            throw new IllegalArgumentException("This leader card is active, you cannot discard it!");

        leaderCards.remove(card);

        notify(new OwnedLeadersUpdate(getNick(), leaderCards));
    }

    /**
     * Checks if the given leader cards are a valid selection. Used during first turn of the Player to avoid cheating
     * and state inconsistency.
     *
     * @param selected the leader cards that the player wants to keep
     * @return true if the selected leader cards are valid, false otherwise
     */
    public boolean isLeaderSelectionValid(List<LeaderCard> selected) {
        for (LeaderCard card : selected) {
            if (!leaderCards.containsKey(card))
                return false;
        }
        return true;
    }

    /**
     * Keeps the given leader cards and removes the others from the player's hand.
     *
     * @param selected the leader cards that the player wants to keep
     */
    public void keepLeaders(List<LeaderCard> selected) throws IllegalArgumentException {
        Map<LeaderCard, Boolean> selectedLeaders = new HashMap<>();
        selectedLeaders.put(selected.get(0), false);
        selectedLeaders.put(selected.get(1), false);

        leaderCards = selectedLeaders;
        notify(new OwnedLeadersUpdate(getNick(), leaderCards));
    }

    /**
     * Gets the LeaderCard with the given UUID.
     *
     * @param uuid the uuid of the card to search for
     * @return the leader card with the given UUID or <code>null</code> if not found
     */
    public LeaderCard getLeaderCardByUuid(UUID uuid) {
        LeaderCard result = null;
        for (LeaderCard card : leaderCards.keySet()) {
            if (uuid.equals(card.getUuid())) {
                result = card;
                break;
            }
        }
        return result;
    }

    /**
     * Calculates the total amount of victory points given by all active leader cards of this Player.
     *
     * @return the total amount of victory points
     */
    public int getLeaderCardsTotalVictoryPoints() {
        int victoryPoints = 0;
        for (LeaderCard card : leaderCards.keySet()) {
            if (isLeaderActive(card))
                victoryPoints += card.getVictoryPoints();
        }
        return victoryPoints;
    }

    /**
     * Checks if this Player has the given resources.
     *
     * @param resources a map associating resource type to quantity
     * @return true if this player has at least the given types and amounts of resources, false otherwise
     */
    public boolean hasResources(Map<Resource, Integer> resources) {
        boolean canAfford = true;
        for (Resource res : resources.keySet()) {
            int required = resources.get(res);
            int playerAmount = getBoard().getDeposit().getAmountOfResource(res);
            if (playerAmount < required)
                canAfford = false;
        }
        return canAfford;
    }

    /**
     * Checks if this Player has already done one of the three major actions during his turn.
     *
     * @return true if has already played, false otherwise
     */
    public boolean hasAlreadyPlayed() {
        return hasAlreadyPlayed;
    }

    /**
     * Sets if this Player has already done one of the three major actions during his turn.
     *
     * @param hasAlreadyPlayed true if has already played, false otherwise
     */
    public void setHasAlreadyPlayed(boolean hasAlreadyPlayed) {
        this.hasAlreadyPlayed = hasAlreadyPlayed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(nick, player.nick);
    }
}
