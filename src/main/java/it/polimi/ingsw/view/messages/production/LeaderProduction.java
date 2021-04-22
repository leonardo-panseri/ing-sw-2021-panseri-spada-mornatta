package it.polimi.ingsw.view.messages.production;

import it.polimi.ingsw.model.Resource;

import java.io.Serial;
import java.util.UUID;

public class LeaderProduction extends Production {
    @Serial
    private static final long serialVersionUID = 1390968791890649217L;

    private final UUID cardUUID;
    private final Resource leaderProductionDesiredResource;

    public LeaderProduction(UUID cardUUID, Resource leaderProductionDesiredResource) {
        this.cardUUID = cardUUID;
        this.leaderProductionDesiredResource = leaderProductionDesiredResource;
    }

    public UUID getCardUUID() {
        return cardUUID;
    }

    public Resource getLeaderProductionDesiredResource() {
        return leaderProductionDesiredResource;
    }
}
