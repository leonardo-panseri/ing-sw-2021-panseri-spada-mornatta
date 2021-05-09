package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update sent when a Player performs an invalid action.
 */
public class InvalidActionUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = 6266673714222888773L;

    private transient final Player player;
    private final String errorMessage;

    /**
     * Constructs a new InvalidActionUpdate for the given Player with the given error message.
     *
     * @param player the player that performed the invalid action
     * @param errorMessage the error message
     */
    public InvalidActionUpdate(Player player, String errorMessage) {
        this.player = player;
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the Player that performed the invalid action.
     *
     * @return the player that performed the invalid action
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public void process(View view) {
        view.getRenderer().showErrorMessage(errorMessage);
        view.setUsingProductions(false);
    }
}
