package it.polimi.ingsw.view.event;

import java.util.UUID;

public class BoughtCardUpdate extends PropertyUpdate{
    private final String playerName;
    private final UUID developmentCardUUID;
    private final int slot;

    public BoughtCardUpdate(String playerName, UUID developmentCardUUID, int slot) {
        this.playerName = playerName;
        this.developmentCardUUID = developmentCardUUID;
        this.slot = slot;
    }
}
