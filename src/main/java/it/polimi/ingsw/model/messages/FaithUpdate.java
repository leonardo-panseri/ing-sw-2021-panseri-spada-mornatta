package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class FaithUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 2004064498686999989L;

    private int faithPoints;
    private int popeFavours;


    public FaithUpdate(String playerName, int faithPoints, int popeFavours) {
        super(playerName);
        this.faithPoints = faithPoints;
        this.popeFavours = popeFavours;
    }

    @Override
    public void process(View view) {

    }
}
