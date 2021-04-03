package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.view.GameState;

import java.util.Scanner;

public class CLI implements Runnable{

    private final Client client;
    private GameState state;

    public CLI(Client client) {
        this.client = client;
        state = GameState.CONNECTING;
    }

    public void handleServerMessage(String message) {
        System.out.println(message);
        switch (message) {
            case ServerMessages.INPUT_NAME -> state = GameState.CHOOSING_NAME;
            case ServerMessages.CHOOSE_PLAYER_NUM -> state = GameState.CHOOSING_PLAYERS;
        }
    }

    public void sendName(String name) {
        String serializedName = "{\"type\":\"PlayerName\",\"content\": \"" + name + "\"}";
        client.sendMessage(serializedName);
    }

    public void sendPlayerNum(String num) {
        String serializedName = "{\"type\":\"PlayerNum\",\"content\": " + num + "}";
        client.sendMessage(serializedName);
    }


    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String command;
        while (client.isActive()) {
            command = scanner.nextLine();
            switch (state) {
                case CHOOSING_NAME -> sendName(command);
                case CHOOSING_PLAYERS -> sendPlayerNum(command);
            }
        }
    }
}
