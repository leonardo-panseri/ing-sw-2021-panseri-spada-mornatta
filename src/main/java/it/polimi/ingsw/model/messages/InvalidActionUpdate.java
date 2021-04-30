package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.View;

public class InvalidActionUpdate extends PropertyUpdate {
    private transient final Player player;
    private final String errorMessage;

    public InvalidActionUpdate(Player player, String errorMessage) {
        this.player = player;
        this.errorMessage = errorMessage;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void process(View view) {
        view.getRenderer().showErrorMessage(errorMessage);
    }
}
