package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

/**
 * Update sent when a player wants to broadcast a new chat message.
 */

public class ChatUpdate extends PropertyUpdate {
    @Serial
    private static final long serialVersionUID = -5748108204406842439L;

    private final String sender;
    private final String message;

    /**
     * Constructor: creates a new ChatUpdate
     *
     * @param sender  nick of the player that sent the message
     * @param message the chat message
     */
    public ChatUpdate(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updateChat(sender, message);
    }
}
