package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;

public class PopeFavourUpdate extends PlayerPropertyUpdate {
    @Serial
    private static final long serialVersionUID = 3546468160697053723L;

    private final int popeFavours;

    public PopeFavourUpdate(String playerName, int popeFavours) {
        super(playerName);
        this.popeFavours = popeFavours;
    }

    @Override
    public void process(View view) {
        view.getModelUpdateHandler().updatePopeFavours(getPlayerName(), popeFavours);
    }
}
