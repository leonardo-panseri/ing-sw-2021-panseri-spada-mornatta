package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.server.messages.DirectServerMessage;
import it.polimi.ingsw.view.messages.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.client.messages.ClientMessage;

/**
 * Represents a client view on the server. It is responsible of handling incoming and outgoing messages and updates to
 * the associated client connection.
 */
public class RemoteView implements Observer<IServerPacket> {
    private Player player;
    private final SocketClientConnection clientConnection;
    private final LobbyController lobbyController;
    private GameController gameController;

    /**
     * Constructs a new RemoteView associated with the given client connection.
     *
     * @param connection the connection to be associated with this remote view
     * @param lobbyController the lobby controller that should handle this remote view
     */
    public RemoteView(SocketClientConnection connection, LobbyController lobbyController) {
        this.player = null;
        this.clientConnection = connection;
        this.lobbyController = lobbyController;
        this.gameController = null;
    }

    /**
     * Sets the Player associated with this RemoteView.
     *
     * @param player the player to be associated with the remote view
     */
    public synchronized void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the client connection associated with this RemoteView.
     *
     * @return the associated client connection
     */
    private SocketClientConnection getClientConnection() {
        return clientConnection;
    }

    /**
     * Gets the LobbyController that handles this RemoteView.
     *
     * @return the lobby controller
     */
    public LobbyController getLobbyController() {
        return lobbyController;
    }

    /**
     * Sets the GameController that handles this RemoteView.
     *
     * @param gameController the game controller to be set
     */
    synchronized void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Notifies the GameController of the given PlayerActionEvent, if the GameController is null (the game is not started)
     * prints an error and does nothing.
     *
     * @param event the player action event that will be notified to the game controller
     */
    private void notifyActionEvent(PlayerActionEvent event) {
        if(gameController != null) {
            event.setPlayer(player);
            gameController.update(event);
        } else
            System.err.println("Received PlayerActionEvent, but game is not started yet");
    }

    /**
     * Notifies the LobbyController of the given ClientMessage, if the game is already started (GameController is not null)
     * prints an error and does nothing.
     *
     * @param message the client message that will be notified to the lobby controller
     */
    private void notifyClientMessage(ClientMessage message) {
        if(gameController == null) {
            message.setClientConnection(getClientConnection());
            lobbyController.update(message);
        } else
            System.err.println("Received ClientMessage, but the game is already started");
    }

    /**
     * Handles an IServerPacket and sends it to the client associated with this RemoteView.
     *
     * @param packet the packet to be sent to the client
     */
    @Override
    public synchronized void update(IServerPacket packet) {
        if(packet instanceof DirectServerMessage) {
            DirectServerMessage dm = (DirectServerMessage) packet;
            if(dm.getRecipient() == clientConnection)
                clientConnection.send(dm);
        } else
            clientConnection.send(packet);
    }

    /**
     * Handles an incoming packet, notifying it to the adequate controller.
     *
     * @param packet the incoming packet to be processed
     */
    void handlePacket(Object packet) {
        System.out.println("Received: " + packet);

        if(packet instanceof ClientMessage) {
            ClientMessage clientMessage = (ClientMessage) packet;
            notifyClientMessage(clientMessage);
        } else if(packet instanceof PlayerActionEvent) {
            PlayerActionEvent actionEvent = (PlayerActionEvent) packet;
            notifyActionEvent(actionEvent);
        } else {
            System.err.println("Received object is of unknown type");
        }
    }
}
