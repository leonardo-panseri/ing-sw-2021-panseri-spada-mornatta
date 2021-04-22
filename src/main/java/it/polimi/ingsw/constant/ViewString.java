package it.polimi.ingsw.constant;

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

    /*
    Game messages
     */
    public static final String OWN_TURN = "It's your turn";
    public static final String OTHER_TURN = "It's %s turn";
    public static final String CHOOSE_ACTION = "Choose an action:";
    public static final String SELECT_LEADERS = "Select the leader cards that you want to keep:";
    public static final String PRODUCTION_QUEUED = "The production has been queued, type \"execute\" to apply all queued productions";
}
