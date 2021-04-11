package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.LeaderCard;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SelectLeadersPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = -4349081220755030803L;

    private List<UUID> selectedLeadersUUID;

    public List<UUID> getSelectedLeaders() {
        return selectedLeadersUUID;
    }

    @Override
    public void process(GameController controller) {
        List<LeaderCard> cards = new ArrayList<>();
        for(UUID uuid : selectedLeadersUUID) {
            cards.add(controller.getGame().getDeck().getLeaderCardByUuid(uuid));
        }
        controller.getPlayerController().selectInitialLeaders(getPlayer(controller), cards);
    }
}
