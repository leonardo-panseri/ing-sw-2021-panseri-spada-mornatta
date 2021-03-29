package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.event.PropertyUpdate;

public class RemoteView extends View implements Observer<PropertyUpdate> {

    @Override
    public void update(PropertyUpdate message) {

    }

    private class MessageReceiver implements Observer<String> {

        @Override
        public void update(String message) {
            System.out.println("Received: " + message);
            try{
                String[] inputs = message.split(",");
                handleMove(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]));
            }catch(IllegalArgumentException e){
                clientConnection.asyncSend("Error!");
            }
        }

    }

    private ClientConnection clientConnection;

    public RemoteView(Player player, ClientConnection c) {
        super(player);
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());

    }

    @Override
    protected void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    /*@Override
    public void update(MoveMessage message)
    {
        showMessage(message.getBoard());
        String resultMsg = "";
        boolean gameOver = message.getBoard().isGameOver(message.getPlayer().getMarker());
        boolean draw = message.getBoard().isFull();
        if (gameOver) {
            if (message.getPlayer() == getPlayer()) {
                resultMsg = gameMessage.winMessage + "\n";
            } else {
                resultMsg = gameMessage.loseMessage + "\n";
            }
        }
        else {
            if (draw) {
                resultMsg = gameMessage.drawMessage + "\n";
            }
        }
        if(message.getPlayer() == getPlayer()){
            resultMsg += gameMessage.waitMessage;
        }
        else{
            resultMsg += gameMessage.moveMessage;
        }

        showMessage(resultMsg);
    }*/

}
