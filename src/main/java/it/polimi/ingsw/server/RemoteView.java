package it.polimi.ingsw.server;

import it.polimi.ingsw.view.messages.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.client.messages.ClientMessage;
import it.polimi.ingsw.model.messages.PropertyUpdate;

public class RemoteView extends Observable<PlayerActionEvent> implements Observer<PropertyUpdate> {

    private Player player;
    private final SocketClientConnection clientConnection;

    public RemoteView(SocketClientConnection c) {
        this.player = null;
        this.clientConnection = c;
        c.addObserver(new ActionReceiver(this));
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

    void notifyActionEvent(PlayerActionEvent event) {
        notify(event);
    }

    void notifyClientMessage(ClientMessage message) {
        message.setClientConnection(getClientConnection());
        clientConnection.getLobbyController().update(message);
    }

    @Override
    public void update(PropertyUpdate update) {
        clientConnection.asyncSend(update);
    }
}
