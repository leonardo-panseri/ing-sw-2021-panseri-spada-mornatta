package it.polimi.ingsw.model.lorenzo.action;

import it.polimi.ingsw.controller.LorenzoController;

public class MoveShuffleAction extends LorenzoAction {
    @Override
    public void execute(LorenzoController controller) {
        controller.executeMoveShuffleAction();
    }

    @Override
    public String toString() {
        return "Lorenzo moved on the faith track and shuffled his actions!";
    }
}
