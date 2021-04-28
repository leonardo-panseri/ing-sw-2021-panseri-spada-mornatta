package it.polimi.ingsw.model.lorenzo.action;

import it.polimi.ingsw.controller.LorenzoController;
import it.polimi.ingsw.model.card.CardColor;

import java.util.Arrays;
import java.util.List;

/**
 * Super class representing an action that the single player opponent can perform.
 */
public abstract class LorenzoAction {
    /**
     * Executes the action.
     */
    public abstract void execute(LorenzoController controller);

    /**
     * Gets a list of all possible actions.
     *
     * @return a list of all possible actions
     */
    public static List<LorenzoAction> getAllActions() {
        return Arrays.asList(new DevelopmentAction(CardColor.GREEN), new DevelopmentAction(CardColor.BLUE),
                new DevelopmentAction(CardColor.YELLOW), new DevelopmentAction(CardColor.PURPLE), new MoveAction(),
                new MoveShuffleAction());
    }
}
