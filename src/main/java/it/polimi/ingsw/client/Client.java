package it.polimi.ingsw.client;

import it.polimi.ingsw.client.messages.GameConfigMessage;
import it.polimi.ingsw.client.messages.PlayerNameMessage;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.messages.PropertyUpdate;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.server.IServerPacket;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.messages.AddToLobbyMessage;
import it.polimi.ingsw.server.messages.GameStartMessage;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.cli.CLI;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.messages.PlayerActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Main client instance.
 */
public class Client {
    private String ip = "localhost";
    private int port = 12345;

    private final Stage stage;
    private final boolean startCli, noServer;

    private Socket socket;
    private SocketClientWrite writeThread;
    private SocketClientRead readThread;

    private GameController localGameController;
    private String localPlayerName;
    private GameConfig localGameConfig = null;
    private ExecutorService localExecutor;

    private View view;

    private boolean active = true;

    /**
     * Constructs a new Client with the given arguments.
     *
     * @param startCli a boolean indicating if the client should be started in CLI mode
     * @param noServer a boolean indicating if the client should not connect to the server and play locally
     */
    public Client(Stage stage, boolean startCli, boolean noServer){
        this.stage = stage;
        this.startCli = startCli;
        this.noServer = noServer;

        if(noServer)
            localExecutor = Executors.newFixedThreadPool(16);
    }

    /**
     * Checks if this client is still active.
     *
     * @return true if the client is active, false otherwise
     */
    public synchronized boolean isActive(){
        return active;
    }

    /**
     * Terminates this client.
     */
    public synchronized void terminate(){
        this.active = false;
        if(!isNoServer()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeThread.interrupt();
            System.out.flush();
            readThread.interrupt();
        }
        System.exit(0);
    }

    /**
     * Gets the View associated with this Client.
     *
     * @return the view that's associated with this client
     */
    public View getView() {
        return view;
    }

    public boolean isNoServer() {
        return noServer;
    }

    /**
     * Sends a message to the Server.
     *
     * @param message the message that will be sent to the server
     */
    public void send(Object message) {
        if (noServer) {
            if (message instanceof PlayerNameMessage) {
                localPlayerName = ((PlayerNameMessage) message).getPlayerName();

                getView().handlePlayerConnect(localPlayerName, 1, -1, Collections.emptyList());
            } else if (message instanceof GameConfigMessage) {
                localGameConfig = GameConfig.deserialize(((GameConfigMessage) message).getSerializedGameConfig());
                initializeLocalGame();
            } else {
                PlayerActionEvent event = (PlayerActionEvent) message;
                event.setPlayer(localGameController.getGame().getCurrentPlayer());

                localExecutor.submit(() -> localGameController.update(event));
            }
        } else {
            writeThread.send(message);
        }
    }

    /**
     * Starts the main client loop, reading and interpreting user commands.
     *
     * @throws IOException if the creation of the socket input or output stream fails
     */
    public void run() throws IOException {
        if(startCli) {
            view = new CLI(this);
        } else view = new GUI(this, stage);

        try {
            view.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(ip, port);
            ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

            readThread = new SocketClientRead(this, socketIn);
            writeThread = new SocketClientWrite(this, socketOut);
            readThread.start();
            writeThread.start();
            System.out.println("Connection established");
        } catch (UnknownHostException | ConnectException e) {
            return false;
        } catch (NoSuchElementException | IOException e) {
            System.out.println("Connection closed from the client side");
            e.printStackTrace();
        }
        return true;
    }

    private void initializeLocalGame() {
        Lobby localLobby = new Lobby();
        try {
            Field playersToStart = localLobby.getClass().getDeclaredField("playersToStart");
            playersToStart.setAccessible(true);
            playersToStart.set(localLobby, 1);

            Field gameConfig = localLobby.getClass().getDeclaredField("customGameConfig");
            gameConfig.setAccessible(true);
            gameConfig.set(localLobby, localGameConfig);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Something went wrong!");
            e.printStackTrace();
            return;
        }

        localGameController = new GameController(localLobby);
        Player player = new Player(localPlayerName);
        localGameController.addPlayer(player);

        Observer<IServerPacket> localObserver = update -> update.process(getView());

        localGameController.getGame().addObserver(localObserver);
        localGameController.getGame().getDeck().addObserver(localObserver);
        localGameController.getGame().getMarket().addObserver(localObserver);

        localGameController.getGame().createLorenzo().addObserver(localObserver);

        player.addObserver(localObserver);
        player.getBoard().addObserver(localObserver);
        player.getBoard().getDeposit().addObserver(localObserver);

        (new GameStartMessage(localGameConfig == null ? GameConfig.loadDefaultGameConfig() : localGameConfig)).process(getView());
        localGameController.getGame().getMarket().initializeMarket();
        localGameController.getGame().getDeck().shuffleDevelopmentDeck();
        localGameController.getTurnController().start();
    }

    public void reset() {
        ip = "localhost";
        port = 12345;

        writeThread.interrupt();
        readThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
