package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.client.messages.PlayersToStartMessage;
import it.polimi.ingsw.constant.Constants;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.SelectLeadersPlayerActionEvent;

import java.util.*;

public class CLI extends View {
    private final Client client;
    private final CommandHandler commandHandler;

    private boolean ownTurn;

    private Map<LeaderCard, Boolean> leaderCards;
    private String market;

    public CLI(Client client) {
        this.client = client;
        this.commandHandler = new CommandHandler(this);
        this.ownTurn = false;
    }

    @Override
    public void showServerMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void updateGamePhase(GamePhase gamePhase) {
        switch (gamePhase) {
            case SELECTING_LEADERS -> {
                if(ownTurn) {
                    setGameState(GameState.SELECT_LEADERS);
                    System.out.println("Select the leader cards that you want to keep:\n" + leaderCards.keySet().toString());
                } else {
                    setGameState(GameState.WAIT_SELECT_LEADERS);
                }
            }
            case PLAYING -> {
                setGameState(GameState.PLAYING);
            }
        }
    }

    @Override
    public void createDevelopmentDeck(List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentDeck) {

    }

    @Override
    public void updateLeaderCards(Map<LeaderCard, Boolean> ownedLeaders) {
        this.leaderCards = ownedLeaders;
    }

    @Override
    public void updateTurn(String playerName) {
        if(playerName.equals(getPlayerName())) {
            ownTurn = true;
            System.out.println("It's your turn");
            if(getGameState() == GameState.WAIT_SELECT_LEADERS) {
                setGameState(GameState.SELECT_LEADERS);
                System.out.println("Select the leader cards that you want to keep:\n" + leaderCards.keySet().toString());
            } else if(getGameState() == GameState.PLAYING) {
                System.out.println("Choose an action:");
            }
        } else {
            ownTurn = false;
            System.out.println("It's " + playerName + " turn");
        }
    }

    @Override
    public void createMarket(List<List<Resource>> market) {
        this.market = Constants.buildMarket(market);
        printMarket();
    }

    @Override
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
                case SELECT_LEADERS -> {
                    String[] rawLeadersToKeep = command.split(",");
                    int[] leadersToKeep = new int[2];
                    if(rawLeadersToKeep.length != 2) {
                        System.out.println("You must select 2 leaders");
                        break;
                    }
                    for(int i = 0; i < rawLeadersToKeep.length; i++) {
                        try {
                            leadersToKeep[i] = Integer.parseInt(rawLeadersToKeep[i]);
                        } catch (NumberFormatException e) {
                            System.out.println("Input 2 numbers");
                            break;
                        }
                    }
                    List<UUID> uuids = new ArrayList<>();
                    List<LeaderCard> leaders = new ArrayList<>(leaderCards.keySet());
                    for(int i : leadersToKeep) {
                        uuids.add(leaders.get(i - 1).getUuid());
                    }

                    client.send(new SelectLeadersPlayerActionEvent(getPlayerName(), uuids));
                }
                case WAIT_SELECT_LEADERS -> System.out.println("It's not your turn");
                case PLAYING -> {
                    if(!ownTurn) {
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
