package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SelectLeadersPlayerActionEvent extends PlayerActionEvent {
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
