package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.Stack;

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

    }

    /**
     * Push the development card on top of the slot, if the move is possible.
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

}


