package it.polimi.ingsw.model.lorenzo.action;

import it.polimi.ingsw.controller.LorenzoController;

/**
 * LorenzoAction adding 1 faith point and shuffling the actions of the single player opponent.
 */
public class MoveShuffleAction extends LorenzoAction {
    @Override
    public boolean execute(LorenzoController controller) {
        return controller.executeMoveShuffleAction();
    }

    @Override
    public String toString() {
        return "Lorenzo moved on the faith track and shuffled his actions!";
    }
}
