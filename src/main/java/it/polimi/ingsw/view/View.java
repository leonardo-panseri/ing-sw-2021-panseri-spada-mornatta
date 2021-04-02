package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.event.PlayerActionEvent;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public abstract class View extends Observable<PlayerActionEvent> {

    private Player player;

    protected View(Player player){
        this.player = player;
    }

    protected Player getPlayer(){
        return player;
    }

    protected abstract void showMessage(Object message);

    public void reportError(String message){
        showMessage(message);
    }

}
