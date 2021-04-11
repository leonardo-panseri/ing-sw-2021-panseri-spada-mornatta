package it.polimi.ingsw.client;

import it.polimi.ingsw.server.messages.ServerMessages;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.view.View;

import java.util.Scanner;

public class CLI extends View {

    private final Client client;

    public CLI(Client client) {
        this.client = client;
    }

    @Override
    public void showServerMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String command;
        while (client.isActive()) {
            command = scanner.nextLine();
            switch (getGameState()) {
                case CHOOSING_NAME -> client.send(new PlayerNameMessage(command));
                case CHOOSING_PLAYERS -> {
                    int playersToStart = -1;
                    try {
                        playersToStart = Integer.parseInt(command);
                    } catch (NumberFormatException e) {
                        System.out.println("Please input a number");
                        break;
                    }

                    if(playersToStart < 1 || playersToStart > 4) {
                        System.out.println("This is not a number between 1 and 4");
                        break;
                    }

                    client.send(new PlayersToStartMessage(playersToStart));
                }
            }
        }
    }
}
