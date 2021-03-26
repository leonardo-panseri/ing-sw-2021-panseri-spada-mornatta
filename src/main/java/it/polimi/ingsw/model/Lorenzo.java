package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Lorenzo {
    private int faithPoints;
    private ArrayList<LorenzoAction> actions;

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
        actions.clone();
        actions.addAll(Arrays.asList(LorenzoAction.values()));
        Collections.shuffle(actions);
    }

    public void addPoints(int points) {
        faithPoints += points;
    }
}
