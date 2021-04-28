package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;
import it.polimi.ingsw.model.messages.LorenzoActionUpdate;
import it.polimi.ingsw.model.messages.TurnUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.model.messages.LorenzoFaithUpdate;
import it.polimi.ingsw.server.IServerPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class representing the single player opponent. Notifies all registered observer of state updates.
 */
public class Lorenzo extends Observable<IServerPacket> {
    private int faithPoints;
    private final List<LorenzoAction> actions;

    /**
     * Constructs a new single player opponent with 0 faith points and a randomized set of actions.
     */
    public Lorenzo() {
        faithPoints = 0;
        actions = new ArrayList<>();
        shuffleActions();
    }

    /**
     * Returns the amount of faith points.
     *
     * @return the amount of faith points.
     */
    int getFaithPoints() {
        return faithPoints;
    }

    /**
     * Pops the first action from the actions list. If the list is empty does nothing.
     */
    public LorenzoAction popAction() {
        if (actions.size() > 0)
            return actions.remove(0);
        return null;
    }

    /**
     * Randomizes actions in the actions list.
     */
    public void shuffleActions() {
        actions.clear();
        actions.addAll(LorenzoAction.getAllActions());
        Collections.shuffle(actions);
    }

    /**
     * Adds the given amount of faith points.
     *
     * @param points the amount of faith points to add.
     */
    public void addPoints(int points) {
        faithPoints += points;
        notify(new LorenzoFaithUpdate(this.faithPoints));
    }

    /**
     * Ends the turn of Lorenzo.
     *
     * @param nextPlayerName the name of the player that should play next
     */
    public void endTurn(String nextPlayerName, LorenzoAction executedAction) {
        notify(new LorenzoActionUpdate(executedAction));
        notify(new TurnUpdate(nextPlayerName));
    }
}
