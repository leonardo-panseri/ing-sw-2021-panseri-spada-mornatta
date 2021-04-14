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
import it.polimi.ingsw.view.MockModel;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.BuyPlayerActionEvent;
import it.polimi.ingsw.view.messages.MarketPlayerActionEvent;
import it.polimi.ingsw.view.messages.SelectLeadersPlayerActionEvent;

import java.util.*;

public class CLI extends View {
    private final Client client;
    private final CommandHandler commandHandler;
    private final MockModel model;

    private boolean ownTurn;

    public CLI(Client client) {
        this.client = client;
        this.commandHandler = new CommandHandler(this);
        this.model = new MockModel();
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
        model.setMarket(market);
    }

    @Override
    public void updateMarket(int index, List<Resource> changes) {
        if (index >= 4) {
            model.updateMarketRow(index - 4, changes);
        }
        else model.updateMarketColumn(index, changes);
    }

    @Override
    public void buyDevelopmentCard(int cardIndex) {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> deck = model.getDevelopmentDeck();
        int mapIndex = cardIndex == 0 ? 0 : (cardIndex - 1) / 4;
        int stackIndex = cardIndex == 0 ? 0 : (cardIndex - 1) - 4 * mapIndex;

        ArrayList<Stack<DevelopmentCard>> stacks = new ArrayList<>(deck.get(mapIndex).values());
        client.send(new BuyPlayerActionEvent(getPlayerName(), stacks.get(stackIndex).peek().getUuid(), 1));
    }

    @Override
    public void draw(int marketIndex, Resource whiteConversion) {
        client.send(new MarketPlayerActionEvent(getPlayerName(), marketIndex - 1, whiteConversion));
    }

    @Override
    public void insertDrawnResources() {
        System.out.println("OK");
        System.out.flush();
    }

    @Override
    public void printMarket() {
        System.out.println(Constants.buildMarket(model.getMarket()));
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
                renderCard(stack.peek(), index);
                index++;
            }
        }
    }

    @Override
    public void printDeposit() {
        System.out.println(model.getDeposit());
    }

    @Override
    public void renderCard(DevelopmentCard card) {

    }

    public void renderCard(DevelopmentCard card, int label) {
        ArrayList<Resource> keys = new ArrayList<>(card.getCost().keySet());
        ArrayList<Resource> input = new ArrayList<>(card.getProductionInput().keySet());
        ArrayList<Resource> output = new ArrayList<>(card.getProductionOutput().keySet());

        String prettyCard = label + ") Color: " + card.getColor() + "\n" +
                "Level: " + card.getLevel() + "\n" +
                "Cost: " + card.getCost().get(keys.get(0)) + " " + keys.get(0) + "\n";

        if(keys.size() > 1){
            for (int i = 1; i < keys.size(); i++) {
                Resource key = keys.get(i);
                prettyCard = prettyCard.concat( "      "+ key + " " + card.getCost().get(key) +"\n");
            }
        }

        prettyCard = prettyCard.concat("Production input: " + card.getProductionInput().get(input.get(0)) + " " + input.get(0) + "\n");

        if(input.size() > 1){
            for (int i = 1; i < input.size(); i++) {
                Resource res = input.get(i);
                prettyCard = prettyCard.concat( "      "+ res + " " + card.getProductionInput().get(res) +"\n");
            }
        }

        prettyCard = prettyCard.concat("Production output: " + card.getProductionOutput().get(output.get(0)) + " " + output.get(0) + "\n");

        if(output.size() > 1){
            for (int i = 1; i < output.size(); i++) {
                Resource res = output.get(i);
                prettyCard = prettyCard.concat( "      "+ res + " " + card.getProductionOutput().get(res) +"\n");
            }
        }

        System.out.println(prettyCard);
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

                    if (playersToStart < 1 || playersToStart > 4) {
                        System.out.println("This is not a number between 1 and 4");
                        break;
                    }

                    client.send(new PlayersToStartMessage(playersToStart));
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

                    client.send(new SelectLeadersPlayerActionEvent(getPlayerName(), uuids));
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
