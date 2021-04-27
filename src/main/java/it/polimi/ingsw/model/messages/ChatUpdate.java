package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

public class ChatUpdate extends PropertyUpdate{

    private final String sender;
    private final String message;

    public ChatUpdate(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    @Override
    public void process(View view) {
        view.getRenderer().printChatMessage(sender, message);
    }
}
