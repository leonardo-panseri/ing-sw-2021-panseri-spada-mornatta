package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.view.View;

import java.io.Serial;
import java.io.Serializable;

public abstract class ServerMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 4167105844763539403L;

    private final String message;

    ServerMessage(String message) {
        this.message = message;
    }

    public void process(View view) {
        view.showServerMessage(message);
    }

    @Override
    public String toString() {
        return "ServerMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
