package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.LeaderCard;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InitialSelectionPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = -4349081220755030803L;

    private final List<UUID> selectedLeadersUUID;
    private final Map<Integer, List<Resource>> selectedResources;

    public InitialSelectionPlayerActionEvent(List<UUID> selectedLeadersUUID,  Map<Integer, List<Resource>> selectedResources) {
        this.selectedLeadersUUID = selectedLeadersUUID;
        this.selectedResources = selectedResources;
    }

    @Override
    public void process(GameController controller) {
        List<LeaderCard> cards = new ArrayList<>();
        for(UUID uuid : selectedLeadersUUID) {
            LeaderCard card = getPlayer().getLeaderCardByUuid(uuid);
            if(card == null) {
                System.err.println("SelectLeadersPlayerActionEvent: Can't find leader card");
                break;
            }
            cards.add(card);
        }
        controller.getPlayerController().handleInitialSelection(getPlayer(), cards, selectedResources);
    }
}
