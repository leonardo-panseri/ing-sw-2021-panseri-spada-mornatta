package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.card.LeaderCard;

import java.util.List;

public class SelectLeadersPlayerActionEvent extends PlayerActionEvent {
    private List<LeaderCard> selectedLeaders;


    public List<LeaderCard> getSelectedLeaders() {
        return selectedLeaders;
    }

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().selectInitialLeaders(getPlayerName(), selectedLeaders);
    }
}
