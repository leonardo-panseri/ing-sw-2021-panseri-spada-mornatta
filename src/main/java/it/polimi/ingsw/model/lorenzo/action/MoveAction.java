package it.polimi.ingsw.model.lorenzo.action;

import it.polimi.ingsw.controller.LorenzoController;

public class MoveAction extends LorenzoAction {
    @Override
    public void execute(LorenzoController controller) {
        controller.executeMoveAction();
    }
}
