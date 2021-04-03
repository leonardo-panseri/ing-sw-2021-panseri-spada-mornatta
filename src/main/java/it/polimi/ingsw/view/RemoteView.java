package it.polimi.ingsw.view;

import com.google.gson.*;
import it.polimi.ingsw.constant.GsonParser;
import it.polimi.ingsw.controller.event.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.view.event.PropertyUpdate;

public class RemoteView extends View implements Observer<PropertyUpdate> {

    private class ActionReceiver implements Observer<String> {

        @Override
        public void update(String serializedAction) {
            System.out.println("Received: " + serializedAction);

            JsonObject message;
            String type;
            JsonElement content;
            try {
                message = JsonParser.parseString(serializedAction).getAsJsonObject();
                type = message.get("type").getAsString();
                content = message.get("content");
            } catch (JsonParseException | IllegalStateException | NullPointerException | ClassCastException e) {
                System.err.println("Received malformed JSON object");
                return;
            }

            handleAction(type, content);
        }

        private void handleAction(String type, JsonElement content) {
            PlayerActionEvent actionEvent = null;
            switch (type) {
                case "PlayerName" -> {
                    try {
                        clientConnection.setPlayerName(content.getAsString());
                    } catch (ClassCastException | IllegalStateException e) {
                        System.err.println("Can't assign name");
                    }
                }
                case "PlayerNum" -> {
                    try {
                        int playerNum = content.getAsInt();
                        if(playerNum < 1 || playerNum > 4) {
                            clientConnection.asyncSendServerMessage(ServerMessages.INVALID_INPUT);
                            break;
                        }
                        clientConnection.setPlayersToStart(playerNum);
                        clientConnection.addToLobby();
                    } catch (ClassCastException | IllegalStateException e) {
                        System.err.println("Can't assign players required to start");
                    }
                }
                case "ActivateLeaderPlayerActionEvent" -> actionEvent = deserializeActionEvent(content, ActivateLeaderPlayerActionEvent.class);
                case "BuyPlayerActionEvent" -> actionEvent = deserializeActionEvent(content, BuyPlayerActionEvent.class);
                case "DepositPlayerActionEvent" -> actionEvent = deserializeActionEvent(content, DepositPlayerActionEvent.class);
                case "DiscardLeaderPlayerActionEvent" -> actionEvent = deserializeActionEvent(content, DiscardLeaderPlayerActionEvent.class);
                case "EndTurnPlayerActionEvent" -> actionEvent = deserializeActionEvent(content, EndTurnPlayerActionEvent.class);
                case "MarketPlayerActionEvent" -> actionEvent = deserializeActionEvent(content, MarketPlayerActionEvent.class);
                case "ProductionPlayerActionEvent" -> actionEvent = deserializeActionEvent(content, ProductionPlayerActionEvent.class);
                case "SelectLeadersPlayerActionEvent" -> actionEvent = deserializeActionEvent(content, SelectLeadersPlayerActionEvent.class);
                default -> System.err.println("Can't deserialize content");
            }

            if(actionEvent != null) {
                System.out.println("Deserialized object of type: " + actionEvent.getClass().getSimpleName());
                System.out.println("Object toString: " + actionEvent.toString());

                notifyActionEvent(actionEvent);
            }
        }

        private PlayerActionEvent deserializeActionEvent(JsonElement content, Class<?> type) {
            try {
                return (PlayerActionEvent) GsonParser.instance().getGson().fromJson(content, type);
            } catch (JsonSyntaxException e) {
                System.err.println("Can't deserialize PlayerActionEvent");
            }
            return null;
        }
    }

    private final SocketClientConnection clientConnection;

    public RemoteView(Player player, SocketClientConnection c) {
        super(player);
        this.clientConnection = c;
        c.addObserver(new ActionReceiver());
    }

    protected void notifyActionEvent(PlayerActionEvent event) {
        notify(event);
    }

    @Override
    protected void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    @Override
    public void update(PropertyUpdate update) {
        clientConnection.asyncSend(update.serialize());
    }
}
