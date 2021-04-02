package it.polimi.ingsw.client;

import com.google.gson.*;
import it.polimi.ingsw.constant.GsonParser;
import it.polimi.ingsw.view.event.*;

import java.io.ObjectInputStream;

public class SocketClientRead extends Thread {
    private final Client client;
    private final ObjectInputStream socketIn;

    public SocketClientRead(Client client, ObjectInputStream socketIn) {
        super();

        this.client = client;
        this.socketIn = socketIn;
    }

    @Override
    public void run() {
        try {
            while (client.isActive()) {
                Object inputObject = socketIn.readObject();
                if(inputObject instanceof String){
                    String in = (String)inputObject;

                    System.out.println("Received: " + in);

                    JsonObject message;
                    String type;
                    JsonElement content;
                    try {
                        message = JsonParser.parseString(in).getAsJsonObject();
                        type = message.get("type").getAsString();
                        content = message.get("content");
                    } catch (JsonParseException | IllegalStateException | NullPointerException | ClassCastException e) {
                        System.err.println("Received malformed JSON object");
                        break;
                    }

                    handleMessage(type, content);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (Exception e){
            client.setActive(false);
        }
    }

    private void handleMessage(String type, JsonElement content) {
        PropertyUpdate update = null;
        String serverMessage = null;
        switch (type) {
            case "ServerMessages" -> {
                try {
                    serverMessage = content.getAsString();
                } catch (ClassCastException e) {
                    System.err.println("Can't deserialize ServerMessage");
                }
            }
            case "BoughtCardUpdate" -> update = deserializeUpdate(content, BoughtCardUpdate.class);
            case "CreateMarketUpdate" -> update = deserializeUpdate(content, CreateMarketUpdate.class);
            case "DepositStrongBoxUpdate" -> update = deserializeUpdate(content, DepositStrongboxUpdate.class);
            case "DepositUpdate" -> update = deserializeUpdate(content, DepositUpdate.class);
            case "DevelopmentDeckUpdate" -> update = deserializeUpdate(content, DevelopmentDeckUpdate.class);
            case "FaithUpdate" -> update = deserializeUpdate(content, FaithUpdate.class);
            case "LorenzoUpdate" -> update = deserializeUpdate(content, LorenzoUpdate.class);
            case "MarketResultUpdate" -> update = deserializeUpdate(content, MarketResultUpdate.class);
            case "MarketUpdate" -> update = deserializeUpdate(content, MarketUpdate.class);
            case "OwnedLeadersUpdate" -> update = deserializeUpdate(content, OwnedLeadersUpdate.class);
            case "TurnUpdate" -> update = deserializeUpdate(content, TurnUpdate.class);
            default -> System.err.println("Can't deserialize content");
        }

        if(update != null) {
            System.out.println("Deserialized object of type: " + update.getClass().getSimpleName());
            System.out.println("Object toString: " + update.toString());
        }
        if(serverMessage != null) {
            System.out.println("Received server message: " + serverMessage);
        }
    }

    private PropertyUpdate deserializeUpdate(JsonElement content, Class<?> type) {
        try {
            return (PropertyUpdate) GsonParser.instance().getGson().fromJson(content, type);
        } catch (JsonSyntaxException e) {
            System.err.println("Can't deserialize PropertyUpdate");
        }
        return null;
    }
}
