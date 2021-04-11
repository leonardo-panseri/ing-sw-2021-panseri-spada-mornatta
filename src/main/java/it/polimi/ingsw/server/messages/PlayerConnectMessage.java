package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;

public class PlayerConnectMessage extends ServerMessage {
    private final String playerName;

    public PlayerConnectMessage(String playerName, int current, int total) {
        super(total == -1 ? "Player " + playerName + " connected" :
                            "Player " + playerName + " connected (" + current + "/" + total + ")");
        this.playerName = playerName;
    }

    @Override
    public void process(View view) {
        super.process(view);
        if(playerName.equals(view.getPlayerName()))
            view.setGameState(GameState.WAITING_PLAYERS);
    }
}
