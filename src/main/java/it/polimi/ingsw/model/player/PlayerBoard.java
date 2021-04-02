package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.BoughtCardUpdate;
import it.polimi.ingsw.view.event.MarketResultUpdate;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        notify(new BoughtCardUpdate(player.getNick(), developmentCard, slot));
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


