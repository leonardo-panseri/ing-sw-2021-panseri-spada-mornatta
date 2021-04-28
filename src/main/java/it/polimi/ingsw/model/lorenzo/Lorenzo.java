package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.lorenzo.action.LorenzoAction;
import it.polimi.ingsw.model.messages.TurnUpdate;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.model.messages.LorenzoUpdate;
import it.polimi.ingsw.server.IServerPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Model class representing the singleplayer opponent. Notifies all registered observer of state updates.
 */
public class Lorenzo extends Observable<IServerPacket> {
    private int faithPoints;
    private final List<LorenzoAction> actions;

    /**
     * Constructs a new singleplayer opponent with 0 faith points and a randomized set of actions.
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
        notify(new LorenzoUpdate(this.faithPoints));
    }

    /**
     * Ends the turn of Lorenzo.
     *
     * @param nextPlayerName the name of the player that should play next
     */
    public void endTurn(String nextPlayerName) {
        notify(new TurnUpdate(nextPlayerName));
    }
}
