package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.GameConfigMessage;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.constant.AnsiColor;
import it.polimi.ingsw.constant.Constants;
import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.InitialSelectionPlayerActionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CLI extends View {
    private final CommandHandler commandHandler;

    private List<UUID> selectedLeaderCards;

    public CLI(Client client) {
        super(client);
        this.setModelUpdateHandler(new CLIModelUpdateHandler(this));
        this.setRenderer(new CLIRenderer(this));
        this.setActionSender(new CLIActionSender(this));
        this.commandHandler = new CommandHandler(this);
    }

    @Override
    public void addToLobby(boolean isFirstConnection) {
        super.addToLobby(isFirstConnection);
        getRenderer().showGameMessage(ViewString.CHOOSE_NAME);
    }

    @Override
    public void handlePlayerConnect(String playerName, int currentPlayers, int playersToStart) {
        super.handlePlayerConnect(playerName, currentPlayers, playersToStart);
        if(getClient().isNoServer()) {
            getRenderer().showGameMessage("If you want to use a custom configuration input the file path (relative to the game directory)," +
                    " otherwise input 'n':");
            return;
        }

        boolean playersToStartSet = playersToStart != -1;
        getRenderer().showLobbyMessage(playersToStartSet ? ViewString.PLAYER_CONNECTED_WITH_COUNT.formatted(playerName, currentPlayers, playersToStart) :
                ViewString.PLAYER_CONNECTED.formatted(playerName));

        if(playerName.equals(getPlayerName()) && isLobbyMaster())
            getRenderer().showGameMessage(ViewString.CHOOSE_PLAYERS_TO_START);
    }

    @Override
    public void handleSetPlayersToStart(int playersToStart) {
        super.handleSetPlayersToStart(playersToStart);
        getRenderer().showGameMessage(ViewString.PLAYERS_TO_START_SET);
        getRenderer().showGameMessage("If you want to use a custom configuration input the file path (relative to the game directory)," +
                " otherwise input 'n':");
    }

    @Override
    public void handleSetGameConfig() {
        super.handleSetGameConfig();
        getRenderer().showGameMessage("Choice confirmed!");
    }

    @Override
    public void handlePlayerDisconnect(String playerName) {
        getRenderer().showLobbyMessage(playerName == null ? ViewString.PLAYER_DISCONNECT :
                ViewString.PLAYER_DISCONNECT_WITH_NAME.formatted(playerName));
    }

    @Override
    public void handlePlayerCrash(String playerName) {
        getRenderer().showLobbyMessage(playerName == null ? ViewString.PLAYER_CRASH :
                ViewString.PLAYER_CRASH_WITH_NAME.formatted(playerName));
        getClient().terminate();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(AnsiColor.BLUE + Constants.MASTER + AnsiColor.RESET);

        if(!getClient().isNoServer())
            getRenderer().showGameMessage("Enter the server ip and port (leave blank for localhost):");
        else
            addToLobby(false);

        String command;
        while (getClient().isActive()) {
            command = scanner.nextLine();

            if(!getClient().isActive())
                break;

            cmdSwitch :
            switch (getGameState()) {
                case CONNECTING -> {
                    if(command.isBlank()) {
                        if(!getClient().connect())
                            getRenderer().showErrorMessage("Unknown host or port, please try again!");
                        break;
                    }

                    String[] ipAndPort = command.split(":");

                    if(ipAndPort.length != 2) {
                        getRenderer().showErrorMessage("Invalid format, please type \"serverip:port\"!");
                        break;
                    }

                    String ip = ipAndPort[0];
                    int port;
                    try {
                        port = Integer.parseInt(ipAndPort[1]);
                    } catch (NumberFormatException e) {
                        getRenderer().showErrorMessage("The port should be a number!");
                        break;
                    }

                    getClient().setIp(ip);
                    getClient().setPort(port);

                    if(!getClient().connect())
                        getRenderer().showErrorMessage("Unknown host or port, please try again!");
                }
                case CHOOSING_NAME -> {
                    setPlayerName(command);
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

                    getActionSender().setPlayersToStart(playersToStart);
                }
                case CHOOSING_GAME_CONFIG -> {
                    if(command.equalsIgnoreCase("n")) {
                        getActionSender().setGameConfig(null);
                        getRenderer().showGameMessage("Playing using the default game rules!");
                        break;
                    }
                    File configFile = new File(command);
                    getActionSender().setGameConfig(configFile);
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
                            break;
                        }
                    }
                    selectedLeaderCards = uuids;

                    int initialResourcesToChoose = getModel().getLocalPlayer().getInitialResourcesToChoose();
                    if(initialResourcesToChoose > 0) {
                        int initialFaith = getModel().getLocalPlayer().getFaithPoints();
                        if(getModel().getLocalPlayer().getFaithPoints() != 0)
                            getRenderer().showGameMessage("You start with " + initialFaith + " faith point!");

                        getRenderer().showGameMessage("You must choose " + initialResourcesToChoose + " starting resources!");
                        getRenderer().showGameMessage("You can choose between " + Arrays.toString(Resource.values()) +
                                "; you can type more using comma as a separator:");
                        setGameState(GameState.CHOOSING_RESOURCES);
                    } else {
                        getActionSender().selectLeaders(this.selectedLeaderCards, new HashMap<>());
                        if(!getClient().isNoServer())
                            setGameState(GameState.WAIT_SELECT_LEADERS);
                    }
                }
                case CHOOSING_RESOURCES -> {
                    String[] rawResourcesToKeep = command.split(",");
                    int resourcesToChoose = getModel().getLocalPlayer().getInitialResourcesToChoose();

                    if(rawResourcesToKeep.length != resourcesToChoose) {
                        getRenderer().showErrorMessage("Invalid syntax!");
                        break;
                    }

                    Map<Integer, List<Resource>> selectedResources = new HashMap<>();
                    for(String rawResource : rawResourcesToKeep) {
                        String[] resourceAndPosition = rawResource.split(" ");
                        if(resourceAndPosition.length != 2) {
                            getRenderer().showErrorMessage("Invalid syntax!");
                            break cmdSwitch;
                        }

                        Resource resource;
                        try {
                            resource = Resource.valueOf(resourceAndPosition[0].toUpperCase());
                        } catch (IllegalArgumentException e) {
                            getRenderer().showErrorMessage(resourceAndPosition[0] + " is not a valid resource!");
                            break cmdSwitch;
                        }
                        int slot;
                        try {
                            slot = Integer.parseInt(resourceAndPosition[1]);
                        } catch (NumberFormatException e) {
                            getRenderer().showErrorMessage(resourceAndPosition[0] + " slot is not a valid number!");
                            break cmdSwitch;
                        }
                        if(slot < 1 || slot > 3) {
                            getRenderer().showErrorMessage(resourceAndPosition[0] + " slot is not a number between 1 and 3!");
                            break cmdSwitch;
                        }

                        if(selectedResources.containsKey(slot))
                            if(selectedResources.get(slot).get(0).equals(resource))
                                selectedResources.get(slot).add(resource);
                            else {
                                getRenderer().showErrorMessage(resource + " has been placed in a slot with a mismatched resource!");
                                break cmdSwitch;
                            }
                        else {
                            for(List<Resource> row : selectedResources.values()) {
                                if (row.contains(resource)) {
                                    getRenderer().showErrorMessage(resource + " has been stored in 2 different rows!");
                                    break cmdSwitch;
                                }
                            }
                            selectedResources.put(slot, new ArrayList<>(Collections.singleton(resource)));
                        }

                        if(selectedResources.containsKey(1)) {
                            if (selectedResources.get(1).size() > 1) {
                                getRenderer().showErrorMessage("Top row overflow!");
                                break cmdSwitch;
                            }
                        }
                    }

                    getActionSender().selectLeaders(this.selectedLeaderCards, selectedResources);

                    setGameState(GameState.WAIT_SELECT_LEADERS);
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
