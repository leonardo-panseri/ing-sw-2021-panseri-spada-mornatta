package it.polimi.ingsw.model.lorenzo.action;

import it.polimi.ingsw.controller.LorenzoController;

/**
 * LorenzoAction adding 2 faith points to the single player opponent.
 */
public class MoveAction extends LorenzoAction {
    @Override
    public boolean execute(LorenzoController controller) {
        return controller.executeMoveAction();
    }

    @Override
    public String toString() {
        return "Lorenzo moved on the faith track by gaining 2 faith points!";
    }
}
