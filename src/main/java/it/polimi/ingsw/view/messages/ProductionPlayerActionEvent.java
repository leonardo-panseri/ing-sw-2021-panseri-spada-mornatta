package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.messages.production.Production;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductionPlayerActionEvent extends PlayerActionEvent {
    @Serial
    private static final long serialVersionUID = -1555283945398296364L;

    private final List<Production> productions;

    public ProductionPlayerActionEvent(String playerName, List<Production> productions) {
        super(playerName);
        this.productions = new ArrayList<>(productions);
    }

    @Override
    public void process(GameController controller) {
        System.out.println(productions);
        //TODO Reimplement server code to validate and use productions
//        if(cardUUID == null) {
//            controller.getPlayerController().useBaseProduction(getPlayer(controller), baseProductionInput, baseProductionOutput);
//        } else {
//            if (leaderProductionDesiredResource == null) {
//                DevelopmentCard card = controller.getGame().getDeck().getDevelopmentCardByUuid(cardUUID);
//                controller.getPlayerController().useDevelopmentProduction(getPlayer(controller), card);
//            } else {
//                LeaderCard card = controller.getGame().getDeck().getLeaderCardByUuid(cardUUID);
//                controller.getPlayerController().useLeaderProduction(getPlayer(controller), card, leaderProductionDesiredResource);
//            }
//        }
    }
}
