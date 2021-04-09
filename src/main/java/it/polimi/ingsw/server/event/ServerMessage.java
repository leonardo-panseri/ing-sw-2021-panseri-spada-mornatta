package it.polimi.ingsw.server.event;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.io.Serializable;

public class ServerMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 4167105844763539403L;

    private String message;

    public ServerMessage(String message) {
        this.message = message;
    }

    public void process(View view) {
        view.showServerMessage(message);
        switch (message) {
            case ServerMessages.INPUT_NAME -> view.setGameState(GameState.CHOOSING_NAME);
            case ServerMessages.CHOOSE_PLAYER_NUM -> view.setGameState(GameState.CHOOSING_PLAYERS);
        }
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
