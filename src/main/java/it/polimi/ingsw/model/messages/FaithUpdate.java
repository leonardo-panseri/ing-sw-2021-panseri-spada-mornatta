package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class FaithUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 2004064498686999989L;

    private final int faithPoints;

    public FaithUpdate(String playerName, int faithPoints) {
        super(playerName);
        this.faithPoints = faithPoints;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateFaith(getPlayerName(), faithPoints);
    }
}
