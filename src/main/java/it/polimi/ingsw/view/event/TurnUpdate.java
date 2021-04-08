package it.polimi.ingsw.view.event;

public class TurnUpdate extends PropertyUpdate {
    private final String currentPlayerName;

    public TurnUpdate(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }
}
