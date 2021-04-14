package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.constant.AnsiColor;
import it.polimi.ingsw.constant.Constants;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.MockModel;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.BuyPlayerActionEvent;
import it.polimi.ingsw.view.messages.SelectLeadersPlayerActionEvent;

import java.util.*;

public class CLI extends View {
    private final CommandHandler commandHandler;
    private final MockModel model;

    private boolean ownTurn;

    public CLI(Client client) {
        super(client);
        this.commandHandler = new CommandHandler(this);
        this.model = new MockModel();
        this.ownTurn = false;
    }

    @Override
    public void showDirectMessage(String message) {
        System.out.println(AnsiColor.BLUE + "Server -> You: " + message + AnsiColor.RESET);
    }

    @Override
    public void showLobbyMessage(String message) {
        System.out.println(AnsiColor.GREEN + "Server -> Lobby: " + message + AnsiColor.RESET);
    }

    @Override
    public void showErrorMessage(String message) {
        System.err.println(message);
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        switch (gamePhase) {
            case SELECTING_LEADERS -> {
                if (ownTurn) {
                    setGameState(GameState.SELECT_LEADERS);
                    System.out.println("Select the leader cards that you want to keep:\n" + model.getLeaderCards().keySet());
                } else {
                    setGameState(GameState.WAIT_SELECT_LEADERS);
                }
            }
            case PLAYING -> setGameState(GameState.PLAYING);
        }
    }

    @Override
    public void createDevelopmentDeck(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck) {
        model.setDevelopmentDeck(developmentDeck);
    }

    @Override
    public void updateLeaderCards(Map<LeaderCard, Boolean> ownedLeaders) {
        model.setLeaderCards(ownedLeaders);
    }

    @Override
    public void updateDevelopmentCards(DevelopmentCard card, int slot) {
        model.setNewDevelopmentCard(card, slot);
    }

    @Override
    public void updateTurn(String playerName) {
        if (playerName.equals(getPlayerName())) {
            ownTurn = true;
            System.out.println("It's your turn");
            if (getGameState() == GameState.WAIT_SELECT_LEADERS) {
                setGameState(GameState.SELECT_LEADERS);
                System.out.println("Select the leader cards that you want to keep:\n" + model.getLeaderCards().keySet());
            } else if (getGameState() == GameState.PLAYING) {
                System.out.println("Choose an action:");
            }
        } else {
            ownTurn = false;
            System.out.println("It's " + playerName + " turn");
        }
    }

    @Override
    public void createMarket(List<List<Resource>> market) {
        model.setMarket(Constants.buildMarket(market));
        printMarket();
    }

    @Override
    public void buyDevelopmentCard(String[] args) {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> deck = model.getDevelopmentDeck();
        int cardIndex = Integer.parseInt(args[0]);
        int mapIndex = cardIndex == 0 ? 0 : (cardIndex - 1) / 4;
        int stackIndex = cardIndex == 0 ? 0 : (cardIndex - 1) - 4 * mapIndex;

        ArrayList<Stack<DevelopmentCard>> stacks = new ArrayList<>(deck.get(mapIndex).values());
        getClient().send(new BuyPlayerActionEvent(getPlayerName(), stacks.get(stackIndex).peek().getUuid(), 1));
    }

    @Override
    public void printMarket() {
        System.out.println(model.getMarket());
    }

    @Override
    public void printOwnLeaders() {
        System.out.println(model.getLeaderCards());
    }

    @Override
    public void printOwnDevelopmentCards() {
        System.out.println(model.getDevelopmentCards());
    }

    @Override
    public void printDevelopmentDeck() {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> deck = model.getDevelopmentDeck();
        int index = 1;
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : deck) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                System.out.println(index + ") " + stack.peek());
                index++;
            }
        }
    }

    @Override
    public void printDeposit() {
        System.out.println(model.getDeposit());
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
                    List<LeaderCard> leaders = new ArrayList<>(model.getLeaderCards().keySet());
                    for (int i : leadersToKeep) {
                        uuids.add(leaders.get(i - 1).getUuid());
                    }

                    getClient().send(new SelectLeadersPlayerActionEvent(getPlayerName(), uuids));
                }
                case WAIT_SELECT_LEADERS -> System.out.println("It's not your turn");
                case PLAYING -> {
                    if (!ownTurn) {
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
