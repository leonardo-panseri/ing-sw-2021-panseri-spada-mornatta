package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.UUID;

public class BoughtCardUpdate extends PropertyUpdate{
    @Serial
    private static final long serialVersionUID = 5376131271721131000L;

    private final String playerName;
    private final DevelopmentCard developmentCard;
    private final int slot;

    public BoughtCardUpdate(String playerName, DevelopmentCard developmentCard, int slot) {
        this.playerName = playerName;
        this.developmentCard = developmentCard;
        this.slot = slot;
    }

    @Override
    public void process(View view) {
        view.updateDevelopmentCards(developmentCard, slot);
    }
}
