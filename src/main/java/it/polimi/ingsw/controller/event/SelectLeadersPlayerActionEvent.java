package it.polimi.ingsw.controller.event;

import it.polimi.ingsw.model.card.LeaderCard;

import java.util.List;

public class SelectLeadersPlayerActionEvent extends PlayerActionEvent {
    private List<LeaderCard> selectedLeaders;


    public List<LeaderCard> getSelectedLeaders() {
        return selectedLeaders;
    }
}
