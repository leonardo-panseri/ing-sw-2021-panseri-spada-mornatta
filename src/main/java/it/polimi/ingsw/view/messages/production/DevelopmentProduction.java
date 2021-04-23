package it.polimi.ingsw.view.messages.production;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.util.Map;
import java.util.UUID;

public class DevelopmentProduction extends Production {
    @Serial
    private static final long serialVersionUID = 2401804940481918306L;

    private final UUID cardUUID;

    public DevelopmentProduction(UUID cardUUID) {
        this.cardUUID = cardUUID;
    }

    @Override
    public Map<Resource, Integer> use(GameController controller, Player player) {
        DevelopmentCard card = player.getBoard().getDevelopmentCardByUuid(cardUUID);
        return controller.getPlayerController().useDevelopmentProduction(player, card);
    }

    @Override
    public String toString() {
        return "DevelopmentProduction{" +
                "cardUUID=" + cardUUID +
                '}';
    }
}
