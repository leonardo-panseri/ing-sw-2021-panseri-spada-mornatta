package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.constant.AnsiColor;
import it.polimi.ingsw.constant.Constants;
import it.polimi.ingsw.constant.ViewString;
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
        System.out.println(AnsiColor.BLUE + Constants.MASTER + AnsiColor.RESET);
        String command;
        while (getClient().isActive()) {
            command = scanner.nextLine();

            if(!getClient().isActive())
                break;

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
                        getRenderer().showErrorMessage(ViewString.NOT_A_NUMBER);
                        break;
                    }

                    if (playersToStart < 1 || playersToStart > 4) {
                        getRenderer().showErrorMessage(ViewString.NOT_IN_RANGE);
                        break;
                    }

                    getClient().send(new PlayersToStartMessage(playersToStart));
                }
                case WAITING_PLAYERS -> getRenderer().showGameMessage(ViewString.WAITING_PLAYERS);
                case SELECT_LEADERS -> {
                    String[] rawLeadersToKeep = command.split(",");
                    int[] leadersToKeep = new int[2];
                    if (rawLeadersToKeep.length != 2) {
                        getRenderer().showErrorMessage(ViewString.LEADERS_SELECT_ERROR);
                        break;
                    }
                    for (int i = 0; i < rawLeadersToKeep.length; i++) {
                        try {
                            leadersToKeep[i] = Integer.parseInt(rawLeadersToKeep[i]);
                        } catch (NumberFormatException e) {
                            getRenderer().showErrorMessage(ViewString.LEADERS_SELECT_NUMBER_FORMAT_ERROR);
                            break;
                        }
                    }
                    if (leadersToKeep[0] < 1 || leadersToKeep[0] > 4 || leadersToKeep[1] < 1 || leadersToKeep[1] > 4) {
                        getRenderer().showErrorMessage(ViewString.LEADERS_SELECT_NUMBER_ERROR);
                        break;
                    }
                    List<UUID> uuids = new ArrayList<>();
                    for (int i : leadersToKeep) {
                        try {
                            uuids.add(getModel().getLocalPlayer().getLeaderCardAt(i - 1).getUuid());
                        } catch (NullPointerException e) {
                            getRenderer().showErrorMessage(ViewString.LEADERS_SELECT_ERROR);
                        }
                    }

                    getClient().send(new SelectLeadersPlayerActionEvent(uuids));
                }
                case WAIT_SELECT_LEADERS -> getRenderer().showErrorMessage(ViewString.NOT_YOUR_TURN);
                case PLAYING -> {
                    try {
                        commandHandler.handle(command);
                    } catch (IllegalArgumentException e) {
                        getRenderer().showErrorMessage(ViewString.COMMAND_NOT_FOUND);
                    }
                }
            }
        }
        scanner.close();
    }
}
