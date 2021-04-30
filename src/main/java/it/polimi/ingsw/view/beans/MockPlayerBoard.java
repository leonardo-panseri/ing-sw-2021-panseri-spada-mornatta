package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.card.DevelopmentCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MockPlayerBoard {
    private final MockPlayer player;
    private final MockDeposit mockDeposit;

    private List<Stack<DevelopmentCard>> developmentCards;

    public MockPlayerBoard(MockPlayer player) {
        this.player = player;
        mockDeposit = new MockDeposit(this.player);
        developmentCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            developmentCards.add(new Stack<>());
        }
    }

    public MockDeposit getDeposit() {
        return mockDeposit;
    }

    /**
     * Gets the development cards of the current player.
     *
     * @return a list representing the development cards of the current player
     */
    public List<Stack<DevelopmentCard>> getDevelopmentCards() {
        return developmentCards;
    }

    public DevelopmentCard getTopDevelopmentCardAt(int slot) {
        return developmentCards.get(slot).peek();
    }

    /**
     * Shows if the current player has any development cards
     *
     * @return returns true if the current player has at least one development card, false if
     *         the player does not have any.
     */
    public boolean hasOwnDevelopmentCard() {
        for (Stack<DevelopmentCard> stack : developmentCards) {
            if (!stack.isEmpty()) return true;
        }
        return false;
    }

    /**
     * Sets a new development card to the current player.
     *
     * @param card the development card needed to be set
     * @param slot the slot in which the card will be put
     */
    public void setNewDevelopmentCard(DevelopmentCard card, int slot) {
        developmentCards.get(slot - 1).push(card);
    }
}
