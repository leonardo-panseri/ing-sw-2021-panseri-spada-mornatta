package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.card.DevelopmentCard;

public class BoughtCardUpdate extends PropertyUpdate{
    private final String playerName;
    private final DevelopmentCard card;

    public BoughtCardUpdate(String playerName, DevelopmentCard card) {
        this.playerName = playerName;
        this.card = card;
    }
}
