package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MockPlayer {
    private final String name;
    private final boolean localPlayer;
    int faithPoints;
    int popeFavours;

    private Map<LeaderCard, Boolean> leaderCards;
    private final MockPlayerBoard playerBoard;

    public MockPlayer(String name, boolean localPlayer) {
        this.name = name;
        this.localPlayer = localPlayer;
        this.faithPoints = 0;
        this.popeFavours = 0;
        this.leaderCards = new HashMap<>();
        this.playerBoard = new MockPlayerBoard(this);
    }

    public boolean isLocalPlayer() {
        return localPlayer;
    }

    /**
     * Gets faith points of the player.
     *
     * @return the number of faith points held by the player
     */
    public int getFaithPoints() {
        return this.faithPoints;
    }

    /**
     * Sets the faith points of the current player.
     *
     * @param faithPoints the number of faith points that need to be added to the current player
     */

    public void setFaithPoints(int faithPoints) {
        this.faithPoints = faithPoints;
    }

    /**
     * Gets the number of pope favours of the current player.
     *
     * @return the number of pope favours of the current player
     */
    public int getPopeFavours() {
        return popeFavours;
    }

    /**
     * Sets the number of pope favours of the current player.
     *
     * @param popeFavours the number of pope favours of the current player
     */
    public void setPopeFavours(int popeFavours) {
        this.popeFavours = popeFavours;
    }

    /**
     * Gets a map corresponding to the LeaderCards of the player.
     *
     * @return a map of the LeaderCards.
     */
    public Map<LeaderCard, Boolean> getLeaderCards() {
        return leaderCards;
    }

    public LeaderCard getLeaderCardAt(int index) {
        ArrayList<LeaderCard> leaderCards = new ArrayList<>(getLeaderCards().keySet());
        try {
            return leaderCards.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;//TODO Check if this return is considered
        }
    }

    public boolean isLeaderCardActive(LeaderCard leaderCard) {
        return leaderCards.get(leaderCard);
    }

    /**
     * Sets the LeaderCards of the player
     *
     * @param leaderCards a map containing the LeaderCards that need to be set
     */
    public void setLeaderCards(Map<LeaderCard, Boolean> leaderCards) {
        this.leaderCards = leaderCards;
    }

    /**
     * Checks if the player has two leader cards with deposit as their special ability type.
     *
     * @return true if the player has two leader cards with deposit as their special ability type,
     * else false
     */
    public boolean hasTwoLeaderDeposits() {
        int count = 0;
        for (LeaderCard card : leaderCards.keySet()) {
            if (card.getSpecialAbility().getType() == SpecialAbilityType.DEPOT) count++;
        }
        return count == 2;
    }

    /**
     * Gets the current player board.
     *
     * @return the player board of the current player
     */

    public MockPlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Gets the deposit of the current player.
     *
     * @return the deposit of the current player
     */
    public MockDeposit getDeposit() {
        return playerBoard.getDeposit();
    }
}
