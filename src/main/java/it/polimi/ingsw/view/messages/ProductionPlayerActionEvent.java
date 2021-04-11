package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

public class ProductionPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = -1555283945398296364L;

    private UUID cardUUID;
    private Resource leaderProductionDesiredResource;
    private List<Resource> baseProductionInput;
    private Resource baseProductionOutput;

    @Override
    public void process(GameController controller) {
        if(cardUUID == null) {
            controller.getPlayerController().useBaseProduction(getPlayer(controller), baseProductionInput, baseProductionOutput);
        } else {
            if (leaderProductionDesiredResource == null) {
                DevelopmentCard card = controller.getGame().getDeck().getDevelopmentCardByUuid(cardUUID);
                controller.getPlayerController().useDevelopmentProduction(getPlayer(controller), card);
            }
            else{
                LeaderCard card = controller.getGame().getDeck().getLeaderCardByUuid(cardUUID);
                controller.getPlayerController().useLeaderProduction(getPlayer(controller), card, leaderProductionDesiredResource);
            }
        }
    }
}
