package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

/**
 * Update sent when a player wants to broadcast a new chat message.
 */

public class ChatUpdate extends PropertyUpdate{

    private final String sender;
    private final String message;

    /**
     * Constructor: creates a new ChatUpdate
     * @param sender nick of the player that sent the message
     * @param message the chat message
     */
    public ChatUpdate(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    @Override
    public void process(View view) {
        view.getRenderer().printChatMessage(sender, message);
    }
}
