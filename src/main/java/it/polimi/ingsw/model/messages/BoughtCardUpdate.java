package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.util.UUID;

public class BoughtCardUpdate extends PropertyUpdate{
    @Serial
    private static final long serialVersionUID = 5376131271721131000L;

    private final String playerName;
    private final UUID developmentCardUUID;
    private final int slot;

    public BoughtCardUpdate(String playerName, UUID developmentCardUUID, int slot) {
        this.playerName = playerName;
        this.developmentCardUUID = developmentCardUUID;
        this.slot = slot;
    }

    @Override
    public void process(View view) {

    }
}
