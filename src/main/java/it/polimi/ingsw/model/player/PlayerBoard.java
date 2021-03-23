package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;

import java.util.Stack;

public class PlayerBoard {
    private Stack<DevelopmentCard> cardSlotLeft;
    private Stack<DevelopmentCard> cardSlotCenter;
    private Stack<DevelopmentCard> cardSlotRight;
    private Deposit deposit;

    public PlayerBoard(){
        cardSlotLeft = new Stack<>();
        cardSlotCenter = new Stack<>();
        cardSlotRight = new Stack<>();
    }

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


