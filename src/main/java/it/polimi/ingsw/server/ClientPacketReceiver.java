package it.polimi.ingsw.server;

import it.polimi.ingsw.view.messages.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.client.messages.ClientMessage;

public class ClientPacketReceiver implements Observer<Object> {
    private final RemoteView remoteView;

    public ClientPacketReceiver(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    @Override
    public void update(Object packet) {
        System.out.println("Received: " + packet);

        if(packet instanceof ClientMessage) {
            System.out.println("Processing client message");

            ClientMessage clientMessage = (ClientMessage) packet;
            remoteView.notifyClientMessage(clientMessage);
        } else if(packet instanceof PlayerActionEvent) {
            System.out.println("Processing player action");

            PlayerActionEvent actionEvent = (PlayerActionEvent) packet;
            remoteView.notifyActionEvent(actionEvent);
        } else {
            System.err.println("Received object is of unknown type");
        }
    }
}
