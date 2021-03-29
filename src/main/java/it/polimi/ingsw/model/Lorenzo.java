package it.polimi.ingsw.model;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.event.PropertyUpdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lorenzo extends Observable<PropertyUpdate> {
    private int faithPoints;
    private List<LorenzoAction> actions;

    public Lorenzo() {
        faithPoints = 0;
        actions = new ArrayList<>();
        actions.addAll(Arrays.asList(LorenzoAction.values()));
    }

    public LorenzoAction getNextAction() {
        return actions.get(0);
    }

    public int getFaithPoints() {
        return faithPoints;
    }

    public void popAction() {
        if (actions.size() > 0) actions.remove(0);
    }

    public void shuffleActions() {
        actions.clear();
        actions.addAll(Arrays.asList(LorenzoAction.values()));
        Collections.shuffle(actions);
    }

    public void addPoints(int points) {
        faithPoints += points;
    }
}
