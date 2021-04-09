package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.server.event.PlayerNameMessage;
import it.polimi.ingsw.server.event.PlayersToStartMessage;
import it.polimi.ingsw.server.event.ServerMessage;
import it.polimi.ingsw.view.GameState;
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
                        System.out.println(ServerMessages.INVALID_INPUT);
                        break;
                    }

                    if(playersToStart < 1 || playersToStart > 4) {
                        System.out.println(ServerMessages.INVALID_INPUT);
                        break;
                    }

                    client.send(new PlayersToStartMessage(playersToStart));
                }
            }
        }
    }
}
