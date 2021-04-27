package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.GameController;

public class ChatPlayerActionEvent extends PlayerActionEvent{

    private final String message;
    public ChatPlayerActionEvent(String message) {
        this.message = message;
    }

    @Override
    public void process(GameController controller) {
        controller.getPlayerController().sendChatMessage(getPlayer(), message);
    }
}
