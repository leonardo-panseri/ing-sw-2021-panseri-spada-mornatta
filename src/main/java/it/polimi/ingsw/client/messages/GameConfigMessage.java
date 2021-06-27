package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.server.LobbyController;

import java.io.Serial;

/**
 * ClientMessage notifying the server of the chosen GameConfig.
 */
public class GameConfigMessage extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -564375582074907221L;

    private final String serializedGameConfig;

    /**
     * Constructs a new GameConfigMessage.
     *
     * @param serializedGameConfig the game config serialized as a String or null if the game should be played with
     *                             standard rules
     */
    public GameConfigMessage(String serializedGameConfig) {
        this.serializedGameConfig = serializedGameConfig;
    }

    public String getSerializedGameConfig() {
        return serializedGameConfig;
    }

    @Override
    public void process(LobbyController controller) {
        controller.setGameConfig(getClientConnection(), serializedGameConfig);
    }
}
