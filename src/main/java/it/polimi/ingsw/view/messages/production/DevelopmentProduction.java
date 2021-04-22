package it.polimi.ingsw.view.messages.production;

import java.io.Serial;
import java.util.UUID;

public class DevelopmentProduction extends Production {
    @Serial
    private static final long serialVersionUID = 2401804940481918306L;

    private UUID cardUUID;

    public DevelopmentProduction(UUID cardUUID) {
        this.cardUUID = cardUUID;
    }

    public UUID getCardUUID() {
        return cardUUID;
    }
}
