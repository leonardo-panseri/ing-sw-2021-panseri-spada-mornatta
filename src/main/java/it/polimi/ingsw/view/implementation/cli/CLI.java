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
import it.polimi.ingsw.view.messages.MarketPlayerActionEvent;
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
                    System.out.println("Select the leader cards that you want to keep:\n");
                    int index = 1;
                    for (LeaderCard card : model.getLeaderCards().keySet()) {
                        renderLeaderCard(card, index);
                        index++;
                    }
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
    public void updateLeaderCards(String playerName, Map<LeaderCard, Boolean> ownedLeaders) {
        if (playerName.equals(getPlayerName())) {
            model.setLeaderCards(ownedLeaders);
        } else model.setOthersLeaderCards(playerName, ownedLeaders);
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
                System.out.println("Select the leader cards that you want to keep:\n");
                int index = 1;
                for (LeaderCard card : model.getLeaderCards().keySet()) {
                    renderLeaderCard(card, index);
                    index++;
                }
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
        getClient().send(new BuyPlayerActionEvent(getPlayerName(), stacks.get(stackIndex).peek().getUuid(), 1));
    }

    @Override
    public void draw(int marketIndex, Resource whiteConversion) {
        getClient().send(new MarketPlayerActionEvent(getPlayerName(), marketIndex - 1, whiteConversion));
    }

    @Override
    public void insertDrawnResources() {
        //TODO implementa metodo
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
    public void printOthersDevelopmentCards(String playerName) {
        if (!model.getOtherLeaderCards().containsKey(playerName)) {
            System.out.println("Player does not have active cards!");
            return;
        }

        int numActive = 0;
        Map<LeaderCard, Boolean> targetCards = model.getOtherLeaderCards().get(playerName);
        for (LeaderCard card : targetCards.keySet()) {
            if (targetCards.get(card)) {
                numActive++;
                renderLeaderCard(card, -1);
            }
        }
        if(numActive == 0) System.out.println("Player does not have active cards!");
    }

    @Override
    public void printDevelopmentDeck() {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> deck = model.getDevelopmentDeck();
        int index = 1;
        for (HashMap<CardColor, Stack<DevelopmentCard>> map : deck) {
            for (Stack<DevelopmentCard> stack : map.values()) {
                renderDevelopmentCard(stack.peek(), index);
                index++;
            }
        }
    }

    @Override
    public void printDeposit() {
        System.out.println(model.getDeposit());
    }

    @Override
    public void renderDevelopmentCard(DevelopmentCard card, int label) {
        ArrayList<Resource> cost = new ArrayList<>(card.getCost().keySet());
        ArrayList<Resource> input = new ArrayList<>(card.getProductionInput().keySet());
        ArrayList<Resource> output = new ArrayList<>(card.getProductionOutput().keySet());

        String prettyCard = "";

        if(label > 0) {
            prettyCard = prettyCard.concat(label + ") ");
        }

        prettyCard = prettyCard.concat("Color: " + card.getColor() + "\n" +
                "Points: " + card.getVictoryPoints() + "\n" +
                "Level: " + card.getLevel() + "\n" +
                "Cost: " + card.getCost().get(cost.get(0)) + " " + cost.get(0) + "\n");

        if(cost.size() > 1){
            for (int i = 1; i < cost.size(); i++) {
                Resource res = cost.get(i);
                prettyCard = prettyCard.concat( "      "+ card.getCost().get(res) + " " + res +"\n");
            }
        }

        prettyCard = prettyCard.concat("Production input: " + card.getProductionInput().get(input.get(0)) + " " + input.get(0) + "\n");

        if(input.size() > 1){
            for (int i = 1; i < input.size(); i++) {
                Resource res = input.get(i);
                prettyCard = prettyCard.concat( "      "+ card.getProductionInput().get(res) + " " + res +"\n");
            }
        }

        prettyCard = prettyCard.concat("Production output: " + card.getProductionOutput().get(output.get(0)) + " " + output.get(0) + "\n");

        if(output.size() > 1){
            for (int i = 1; i < output.size(); i++) {
                Resource res = output.get(i);
                prettyCard = prettyCard.concat( "      "+ card.getProductionOutput().get(res) + " " + res +"\n");
            }
        }

        System.out.println(prettyCard);
    }

    @Override
    public void renderLeaderCard(LeaderCard card, int label) {
        Map<Resource, Integer> costRes = card.getCardRequirements().getResourceRequirements();
        Map<CardColor, Integer> costCol = card.getCardRequirements().getCardColorRequirements();
        Map<CardColor, Integer> costLev = card.getCardRequirements().getCardLevelRequirements();

        String prettyCard = "";

        if(label > 0) {
            prettyCard = prettyCard.concat(label + ") ");
        }

        prettyCard = prettyCard.concat("Points: " + card.getVictoryPoints() + "\n");

        if(costRes != null){
            for (Resource res : costRes.keySet()) {
                prettyCard = prettyCard.concat("Resource needed: " + costRes.get(res) + " " + res + "\n");
            }
        }

        if(costCol != null){
            for (CardColor color : costCol.keySet()) {
                prettyCard = prettyCard.concat("Card color needed: " + costCol.get(color) + " " + color + "\n");
            }
        }

        if(costLev != null){
            for (CardColor color : costLev.keySet()) {
                prettyCard = prettyCard.concat("Card level needed: " + costLev.get(color) + " " + color + "\n");
            }
        }

        prettyCard = prettyCard.concat("Special ability: " + card.getSpecialAbility().getType() + "\n");

        prettyCard = prettyCard.concat("Targeted resource: " + card.getSpecialAbility().getTargetResource() + "\n");

        System.out.println(prettyCard);
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
