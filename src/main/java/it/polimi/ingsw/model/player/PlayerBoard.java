package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.BoughtCardUpdate;
import it.polimi.ingsw.view.event.MarketResultUpdate;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.*;

/**
 * Models the player board of a player, composed by a reference to the owner, three stacks of development cards
 * and a deposit.
 */
public class PlayerBoard extends Observable<PropertyUpdate> {
    private Player player;
    private Stack<DevelopmentCard> cardSlotLeft;
    private Stack<DevelopmentCard> cardSlotCenter;
    private Stack<DevelopmentCard> cardSlotRight;
    private Deposit deposit;
    private List<Resource> marketResults;

    /**
     * Constructor: instantiates the slots, the deposit and the player reference.
     *
     * @param player the owner of the board
     */
    public PlayerBoard(Player player) {
        cardSlotLeft = new Stack<>();
        cardSlotCenter = new Stack<>();
        cardSlotRight = new Stack<>();
        deposit = new Deposit(player);
        marketResults = new ArrayList<>();
        this.player = player;
    }

    /**
     * Getter for the deposit.
     *
     * @return the deposit
     */
    public Deposit getDeposit(){
        return deposit;
    }

    /**
     * Gets a List containing all card slots.
     *
     * @return an arraylist containing all card slots
     */
    private List<Stack<DevelopmentCard>> getAllCardSlots() {
        return new ArrayList<>(Arrays.asList(cardSlotLeft, cardSlotCenter, cardSlotRight));
    }

    /**
     * Gets the total amount of cards with the given color.
     *
     * @param color the card color of the cards that will be counted
     * @return the total amount of cards with the given color
     */
    public int getAmountOfCardOfColor(CardColor color) {
        int amount = 0;
        for(Stack<DevelopmentCard> slot : getAllCardSlots())
            for(DevelopmentCard card : slot)
                if(card.getColor() == color)
                    amount++;
        return amount;
    }

    /**
     * Checks if this PlayerBoard has a card with the given color and the given level.
     *
     * @param color the color to search for
     * @param level the level to search for
     * @return true if found, false otherwise
     */
    public boolean hasCardOfColorAndLevel(CardColor color, int level) {
        for(Stack<DevelopmentCard> slot : getAllCardSlots())
            for(DevelopmentCard card : slot)
                if(card.getColor() == color && card.getLevel() == level)
                    return true;
        return false;
    }

    /**
     * Checks if a card of the given level can be placed in the given slot.
     *
     * @param level the level of the card
     * @param slot the slot to place the card in
     * @return true if the card can be placed in the slot, false otherwise
     */
    public boolean canPlaceCardOfLevel(int level, int slot) {
        if(slot < 1 || slot > 3)
            return false;

        List<Stack<DevelopmentCard>> slots = getAllCardSlots();
        boolean canPlace = false;
        if(level == 1) {
            if(slots.get(slot - 1).isEmpty()) {
                canPlace = true;
            }
        } else if(level > 1 && level < 4) {
            int topCardLevel = slots.get(slot - 1).peek().getLevel();
            if (topCardLevel == level - 1) {
                canPlace = true;
            }

        }
        return canPlace;
    }

    /**
     * Gets the total amount of development cards in this player board.
     *
     * @return the total amount of development cards
     */
    public int getNumberOfDevelopmentCards() {
        int amount = 0;
        for(Stack<DevelopmentCard> slot : getAllCardSlots())
            amount += slot.size();
        return amount;
    }

    /**
     * Calls for the method to push the bought development card in the desired stack.
     *
     * @param slot the slot where to push the card
     * @param developmentCard the bought development card
     */
    public void addCard(int slot, DevelopmentCard developmentCard) {
        //slot 1 == cardSlotLeft
        //slot 2 == cardSlotCenter
        //slot 3 == cardSlotRight
        if (slot == 1) {
            pushDevelopmentCard(developmentCard, cardSlotLeft);
        }
        if (slot == 2) {
            pushDevelopmentCard(developmentCard, cardSlotCenter);
        }
        if (slot == 3) {
            pushDevelopmentCard(developmentCard, cardSlotRight);
        }
        notify(new BoughtCardUpdate(player.getNick(), developmentCard.getUuid(), slot));
    }

    /**
     * Pushes the development card on top of the slot, if the move is possible.
     *
     * @param developmentCard the card to push
     * @param cardSlot the slot where to push the card
     */
    private void pushDevelopmentCard(DevelopmentCard developmentCard, Stack<DevelopmentCard> cardSlot) {
        if (cardSlot.size() == 0 && developmentCard.getLevel() == 1) {
            cardSlot.push(developmentCard);
        } else if (cardSlot.size() == 1) {
            if (cardSlot.get(0).getLevel() == 1 && developmentCard.getLevel() == 2) {
                cardSlot.push(developmentCard);
            }
        } else if (cardSlot.size() == 2) {
            if (cardSlot.get(1).getLevel() == 2 && developmentCard.getLevel() == 3) {
                cardSlot.push(developmentCard);
            }
        }
    }

    /**
     * Sets the market results of this {@link Player} PlayerBoard.
     *
     * @param marketResults an arraylist containing the market results
     */
    public void setMarketResults(ArrayList<Resource> marketResults) {
        this.marketResults.clear();
        this.marketResults.addAll(marketResults);
        notify(new MarketResultUpdate(player.getNick(), this.marketResults));
    }
}


