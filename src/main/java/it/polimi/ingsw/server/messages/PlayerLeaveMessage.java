package it.polimi.ingsw.server.messages;

public class PlayerLeaveMessage extends ServerMessage {
    public PlayerLeaveMessage(String playerName) {
        super(playerName == null ?
                "Someone left the lobby" :
                playerName + " left the lobby");
    }
}
