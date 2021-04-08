package it.polimi.ingsw.view.event;

public class FaithUpdate extends PropertyUpdate {
    private String playerName;
    private int faithPoints;
    private int popeFavours;


    public FaithUpdate(String playerName, int faithPoints, int popeFavours) {
        this.playerName = playerName;
        this.faithPoints = faithPoints;
        this.popeFavours = popeFavours;
    }
}
