package it.polimi.ingsw.model;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.LorenzoUpdate;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Model class representing the singleplayer opponent. Notifies all registered observer of state updates.
 */
public class Lorenzo extends Observable<PropertyUpdate> {
    private int faithPoints;
    private List<LorenzoAction> actions;

    /**
     * Constructs a new singleplayer opponent with 0 faith points and a randomized set of actions.
     */
    public Lorenzo() {
        faithPoints = 0;
        actions = new ArrayList<>();
        shuffleActions();
    }

    /**
     * Returns the next action without deleting it.
     *
     * @return the lorenzo action that will be executed next
     */
    public LorenzoAction getNextAction() {
        return actions.get(0);
    }

    /**
     * Returns the amount of faith points.
     *
     * @return the amount of faith points.
     */
    public int getFaithPoints() {
        return faithPoints;
    }

    /**
     * Returns the actions of Lorenzo
     *
     * @return the actions of Lorenzo
     */
    List<LorenzoAction> getActions(){
        return actions;
    }

    /**
     * Pops the first action from the actions list. If the list is empty does nothing.
     */
    public void popAction() {
        if (actions.size() > 0) actions.remove(0);
    }

    /**
     * Randomizes actions in the actions list.
     */
    public void shuffleActions() {
        actions.clear();
        actions.addAll(Arrays.asList(LorenzoAction.values()));
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
}
