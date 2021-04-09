package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class FaithUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = 2004064498686999989L;

    private String playerName;
    private int faithPoints;
    private int popeFavours;


    public FaithUpdate(String playerName, int faithPoints, int popeFavours) {
        this.playerName = playerName;
        this.faithPoints = faithPoints;
        this.popeFavours = popeFavours;
    }

    @Override
    public void process(View view) {

    }
}
