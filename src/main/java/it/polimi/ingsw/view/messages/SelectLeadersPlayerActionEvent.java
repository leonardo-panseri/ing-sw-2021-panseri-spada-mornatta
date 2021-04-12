package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SelectLeadersPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = -4349081220755030803L;

    private final List<UUID> selectedLeadersUUID;

    public SelectLeadersPlayerActionEvent(String playerName, List<UUID> selectedLeadersUUID) {
        super(playerName);
        this.selectedLeadersUUID = selectedLeadersUUID;
    }

    @Override
    public void process(GameController controller) {
        Player player = getPlayer(controller);
        List<LeaderCard> cards = new ArrayList<>();
        for(UUID uuid : selectedLeadersUUID) {
            LeaderCard card = player.getLeaderCardByUuid(uuid);
            if(card == null) {
                System.err.println("SelectLeadersPlayerActionEvent: Can't find leader card");
                break;
            }
            cards.add(card);
        }
        controller.getPlayerController().selectInitialLeaders(player, cards);
    }
}
