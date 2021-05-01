package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.card.DevelopmentCard;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Local copy of a Player's board.
 */
public class MockPlayerBoard {
    private final MockDeposit mockDeposit;

    private final List<Stack<DevelopmentCard>> developmentCards;

    /**
     * Constructs a new MockPlayerBoard and a new MockPlayerDeposit, initializing all attributes.
     *
     * @param player the player that is the owner of this player board
     */
    public MockPlayerBoard(MockPlayer player) {
        mockDeposit = new MockDeposit(player);
        developmentCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            developmentCards.add(new Stack<>());
        }
    }

    /**
     * Gets the MockDeposit associated with this MockPlayerBoard.
     *
     * @return the deposit
     */
    public MockDeposit getDeposit() {
        return mockDeposit;
    }

    /**
     * Gets the development cards of the player.
     *
     * @return a list representing the development cards of the player
     */
    public List<Stack<DevelopmentCard>> getDevelopmentCards() {
        return developmentCards;
    }

    /**
     * Gets the DevelopmentCard that is at the top of the given slot.
     *
     * @param slot the slot to search in (should be 0, 1 or 2)
     * @return the development card at the top of the given slot, or null if the slot is empty
     */
    public DevelopmentCard getTopDevelopmentCardAt(int slot) {
        if(slot < 0 || slot > 2)
            return null;

        try {
            return developmentCards.get(slot).peek();
        } catch (EmptyStackException ignored) {
        }
        return null;
    }

    /**
     * Shows if the current player has any DevelopmentCards.
     *
     * @return true if the current player has at least one development card, false if
     *         the player does not have any.
     */
    public boolean hasOwnDevelopmentCard() {
        for (Stack<DevelopmentCard> stack : developmentCards) {
            if (!stack.isEmpty()) return true;
        }
        return false;
    }

    /**
     * Sets a new development card to the player.
     *
     * @param card the development card needed to be set
     * @param slot the slot in which the card will be put
     */
    public void setNewDevelopmentCard(DevelopmentCard card, int slot) {
        developmentCards.get(slot - 1).push(card);
    }
}
