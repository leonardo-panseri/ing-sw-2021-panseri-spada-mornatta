package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;
import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update sent after Lorenzo performs an action, in single player mode.
 */
public class LorenzoActionUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = 5123184263333073010L;

    private final LorenzoAction action;

    /**
     * Constructor that takes in the executed action.
     *
     * @param action the executed action
     */
    public LorenzoActionUpdate(LorenzoAction action) {
        this.action = action;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().handleLorenzoAction(action);
    }
}
