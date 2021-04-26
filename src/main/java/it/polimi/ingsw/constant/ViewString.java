package it.polimi.ingsw.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewString {
    /*
    Lobby messages
     */
    public static final String CHOOSE_NAME = "Enter your username:";
    public static final String CHOOSE_PLAYERS_TO_START = "Choose the number of players required to start the game:";
    public static final String PLAYERS_TO_START_SET = "Successfully set, wait for other players";
    public static final String PLAYER_CONNECTED = "Player %s connected";
    public static final String PLAYER_DISCONNECT = "Someone disconnected";
    public static final String PLAYER_DISCONNECT_WITH_NAME = "Player %s disconnected";
    public static final String PLAYER_CONNECTED_WITH_COUNT = "Player %s connected (%d/%d)";
    public static final String WAITING_PLAYERS = "Waiting for other players to join";
    public static final String GAME_STARTING = "The game is starting";

    /*
    Error messages
     */
    public static final String NOT_YOUR_TURN = "It's not your turn";
    public static final String COMMAND_NOT_FOUND = "This command does not exists";
    public static final String NOT_A_NUMBER = "Please input a number";
    public static final String NOT_IN_RANGE = "This is not a number between 1 and 4";
    public static final String LEADERS_SELECT_ERROR = "You must select 2 leader cards";
    public static final String LEADERS_SELECT_NUMBER_ERROR = "You must input 2 numbers between 1 and 4";
    public static final String LEADERS_SELECT_NUMBER_FORMAT_ERROR = "Please input numbers";
    public static final String NOT_ALREADY_PLAYED = "You must perform at least an action before ending your turn";
    public static final String PLAYER_CRASH = "Someone crashed, terminating the game";
    public static final String PLAYER_CRASH_WITH_NAME = "Player %s crashed, terminating the game";
    public static final String ALREADY_PLAYED = "You have already done one of the three major actions!";
    public static final String ALREADY_ACTIVE = "The selected leader card is already active";
    public static final String TWO_LEADER_DEPOSITS_REQUIRED = "You don't have two active leaders with a depot special ability";

    /*
    Game messages
     */
    public static final String OWN_TURN = "It's your turn";
    public static final String OTHER_TURN = "It's %s turn";
    public static final String CHOOSE_ACTION = "Choose an action:";
    public static final String SELECT_LEADERS = "Select the leader cards that you want to keep:";
    public static final String PRODUCTION_QUEUED = "The production has been queued, type \"execute\" to apply all queued productions";
    public static final String INCORRECT_FORMAT = "Incorrect format: please input ";

    /*
    Command format
     */

    public static final String VIEW_LEADERS = "\"view leader <leader index>\"";
    public static final String VIEW_DEVELOPMENT = "\"view development <development index>\"";
    public static final String VIEW_DECK = "\"view deck\"";
    public static final String VIEW_DEPOSIT = "\"view deposit\"";
    public static final String VIEW_STRONGBOX = "\"view strongbox\"";
    public static final String VIEW_MARKET = "\"view market\"";
    public static final String VIEW_RESULT = "\"view leader\"";
    public static final String VIEW_FAITH = "\"view faith\"";
    public static final String BUY_CARD = "\"buy <card num>\"";
    public static final String USE_PRODUCTION = "\"production <leader|development|base> <...>\"";
    public static final String USE_LEADER_PRODUCTION = "\"production leader <leader card index> <resource to receive>\"";
    public static final String USE_DEVELOPMENT_PRODUCTION = "\"production development <development card slot index>\"";
    public static final String USE_BASE_PRODUCTION = "\"production base <input resource 1> <input resource 2> <output resource>\"";
    public static final String DRAW_MARKET = "\"draw <row num or column num> <resource to take instead of white spheres>\"";
    public static final String SPY = "\"spy <player name> <leaders|development|deposit|faith>\"";
    public static final String DISCARD = "\"discard <leader card index>\"";
    public static final String MOVE_DEPOSIT = "\"move <row1> <row2>\"";
    public static final String STORE_DEPOSIT = "\"store <market result index> <row>\"";
    public static final String END_TURN = "\"endturn\"";
    public static final String ACTIVATE_LEADER = "\"activate <leader card index>\"";
    public static final String HELP = "\"help\"";

    public static List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        addMultipleToList(commands, VIEW_LEADERS, VIEW_DEVELOPMENT, VIEW_DECK, VIEW_DEPOSIT, VIEW_STRONGBOX, VIEW_MARKET,
                VIEW_RESULT, VIEW_FAITH, BUY_CARD, USE_LEADER_PRODUCTION, USE_DEVELOPMENT_PRODUCTION,
                USE_BASE_PRODUCTION, DRAW_MARKET, SPY, DISCARD, MOVE_DEPOSIT, STORE_DEPOSIT, END_TURN, ACTIVATE_LEADER, HELP);

        return commands;
    }

    private static void addMultipleToList(List<String> commands, String... args) {
        commands.addAll(Arrays.asList(args));
    }

}
