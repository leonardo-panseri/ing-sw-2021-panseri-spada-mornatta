package it.polimi.ingsw.client;

import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.constant.Constants;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Scanner;

public class CLI extends View {

    private final Client client;
    private String market;

    public CLI(Client client) {
        this.client = client;
    }

    @Override
    public void showServerMessage(String message) {
        System.out.println(message);
    }

    public void createMarket(List<List<Resource>> market) {
        this.market = Constants.buildMarket(market);
        printMarket();
    }

    public void printMarket() {
        System.out.println(market);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Constants.ANSI_BLUE + Constants.MASTER + Constants.ANSI_RESET);
        String command;
        while (client.isActive()) {
            command = scanner.nextLine();
            switch (getGameState()) {
                case CHOOSING_NAME -> {
                    setPlayerName(command);
                    client.send(new PlayerNameMessage(command));
                }
                case CHOOSING_PLAYERS -> {
                    int playersToStart;
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
                case WAITING_PLAYERS -> System.out.println("Waiting for other players to join");
            }
        }
    }
}
