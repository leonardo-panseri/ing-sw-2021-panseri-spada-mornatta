package it.polimi.ingsw.server.messages;

public class PlayerCrashMessage extends ServerMessage {
    public PlayerCrashMessage(String playerName) {
        super(playerName == null ?
                "Someone crashed, terminating the game" :
                playerName + " crashed, terminating the game");
    }

}
