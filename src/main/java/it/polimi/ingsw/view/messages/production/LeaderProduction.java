package it.polimi.ingsw.view.messages.production;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.util.Map;
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

    @Override
    public Map<Resource, Integer> use(GameController controller, Player player) {
        LeaderCard card = player.getLeaderCardByUuid(cardUUID);
        return controller.getPlayerController().useLeaderProduction(player, card, leaderProductionDesiredResource);
    }

    @Override
    public String toString() {
        return "leader production(" +
                "desired resource=" + leaderProductionDesiredResource +
                ')';
    }
}
