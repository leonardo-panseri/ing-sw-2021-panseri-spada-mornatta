package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.event.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.event.ClientMessage;

public class ActionReceiver implements Observer<Object> {
    private final RemoteView remoteView;

    public ActionReceiver(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    @Override
    public void update(Object action) {
        System.out.println("Received: " + action);

        if(action instanceof ClientMessage) {
            System.out.println("Processing client message");

            ClientMessage clientMessage = (ClientMessage) action;
            clientMessage.process(remoteView.getClientConnection());
        } else if(action instanceof PlayerActionEvent) {
            System.out.println("Processing player action");

            PlayerActionEvent actionEvent = (PlayerActionEvent) action;
            remoteView.notifyActionEvent(actionEvent);
        } else {
            System.err.println("Received object is of unknown type");
        }
    }
}
