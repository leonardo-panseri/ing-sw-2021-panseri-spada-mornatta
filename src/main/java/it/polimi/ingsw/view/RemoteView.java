package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.event.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.event.PropertyUpdate;

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

    @Override
    public void update(PropertyUpdate update) {
        clientConnection.asyncSend(update);
    }
}
