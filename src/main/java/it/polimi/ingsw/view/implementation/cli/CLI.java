package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.constant.Constants;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.SelectLeadersPlayerActionEvent;

import java.util.*;

public class CLI extends View {
    private final CommandHandler commandHandler;

    public CLI(Client client) {
        super(client);
        this.setModelUpdateHandler(new CLIModelUpdateHandler(this));
        this.setRenderer(new CLIRenderer(this));
        this.setActionSender(new CLIActionSender(this));
        this.commandHandler = new CommandHandler(this);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Constants.ANSI_BLUE + Constants.MASTER + Constants.ANSI_RESET);
        String command;
        while (getClient().isActive()) {
            command = scanner.nextLine();
            switch (getGameState()) {
                case CHOOSING_NAME -> {
                    setPlayerName(command);
                    getClient().send(new PlayerNameMessage(command));
                }
                case CHOOSING_PLAYERS -> {
                    int playersToStart;
                    try {
                        playersToStart = Integer.parseInt(command);
                    } catch (NumberFormatException e) {
                        System.out.println("Please input a number");
                        break;
                    }

                    if (playersToStart < 1 || playersToStart > 4) {
                        System.out.println("This is not a number between 1 and 4");
                        break;
                    }

                    getClient().send(new PlayersToStartMessage(playersToStart));
                }
                case WAITING_PLAYERS -> System.out.println("Waiting for other players to join");
                case SELECT_LEADERS -> {
                    String[] rawLeadersToKeep = command.split(",");
                    int[] leadersToKeep = new int[2];
                    if (rawLeadersToKeep.length != 2) {
                        System.out.println("You must select 2 leaders");
                        break;
                    }
                    for (int i = 0; i < rawLeadersToKeep.length; i++) {
                        try {
                            leadersToKeep[i] = Integer.parseInt(rawLeadersToKeep[i]);
                        } catch (NumberFormatException e) {
                            System.out.println("Input 2 numbers");
                            break;
                        }
                    }
                    List<UUID> uuids = new ArrayList<>();
                    List<LeaderCard> leaders = new ArrayList<>(getModel().getLeaderCards().keySet());
                    for (int i : leadersToKeep) {
                        uuids.add(leaders.get(i - 1).getUuid());
                    }

                    getClient().send(new SelectLeadersPlayerActionEvent(getPlayerName(), uuids));
                }
                case WAIT_SELECT_LEADERS -> System.out.println("It's not your turn");
                case PLAYING -> {
                    if (!isOwnTurn()) {
                        System.out.println("It's not your turn");
                        break;
                    }

                    try {
                        commandHandler.handle(command);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
