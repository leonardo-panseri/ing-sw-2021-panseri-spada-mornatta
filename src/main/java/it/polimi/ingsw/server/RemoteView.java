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
    private GameController gameController;

    public RemoteView(SocketClientConnection c) {
        this.player = null;
        this.clientConnection = c;
        this.gameController = null;
        c.addObserver(new ClientPacketReceiver(this));
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public SocketClientConnection getClientConnection() {
        return clientConnection;
    }

    void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    void notifyActionEvent(PlayerActionEvent event) {
        if(gameController != null)
            gameController.update(event);
        else
            System.err.println("Received PlayerActionEvent, but game is not started yet");
    }

    void notifyClientMessage(ClientMessage message) {
        message.setClientConnection(getClientConnection());
        clientConnection.getLobbyController().update(message);
    }

    @Override
    public void update(IServerPacket packet) {
        if(packet instanceof DirectServerMessage) {
            DirectServerMessage dm = (DirectServerMessage) packet;
            if(dm.getRecipient() == clientConnection)
                clientConnection.asyncSend(dm);
        } else if(packet instanceof PlayerCrashMessage) {
            clientConnection.send(packet);
        } else
            clientConnection.asyncSend(packet);
    }
}
