package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.server.messages.DirectServerMessage;
import it.polimi.ingsw.server.messages.PlayerCrashMessage;
import it.polimi.ingsw.view.messages.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.client.messages.ClientMessage;

public class RemoteView implements Observer<IServerPacket> {

    private Player player;
    private final SocketClientConnection clientConnection;
    private final LobbyController lobbyController;
    private GameController gameController;

    public RemoteView(SocketClientConnection c, LobbyController lobbyController) {
        this.player = null;
        this.clientConnection = c;
        this.lobbyController = lobbyController;
        this.gameController = null;
    }

    public synchronized void setPlayer(Player player) {
        this.player = player;
    }

    private SocketClientConnection getClientConnection() {
        return clientConnection;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    synchronized void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private void notifyActionEvent(PlayerActionEvent event) {
        if(gameController != null)
            gameController.update(event);
        else
            System.err.println("Received PlayerActionEvent, but game is not started yet");
    }

    private void notifyClientMessage(ClientMessage message) {
        message.setClientConnection(getClientConnection());
        lobbyController.update(message);
    }

    @Override
    public synchronized void update(IServerPacket packet) {
        if(packet instanceof DirectServerMessage) {
            DirectServerMessage dm = (DirectServerMessage) packet;
            if(dm.getRecipient() == clientConnection)
                clientConnection.send(dm);
        } else
            clientConnection.send(packet);
    }

    void handlePacket(Object packet) {
        System.out.println("Received: " + packet);

        if(packet instanceof ClientMessage) {
            System.out.println("Processing client message");

            ClientMessage clientMessage = (ClientMessage) packet;
            notifyClientMessage(clientMessage);
        } else if(packet instanceof PlayerActionEvent) {
            System.out.println("Processing player action");

            PlayerActionEvent actionEvent = (PlayerActionEvent) packet;
            if(!actionEvent.getPlayerName().equals(player.getNick())) {
                System.err.println("Player tried to send packet with different nick (got '" + actionEvent.getPlayerName()
                        + "', expected '" + player.getNick() + "'");
                return;
            }
            notifyActionEvent(actionEvent);
        } else {
            System.err.println("Received object is of unknown type");
        }
    }
}
