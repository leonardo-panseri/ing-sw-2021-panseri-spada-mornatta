package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.implementation.cli.CLIActionSender;
import it.polimi.ingsw.view.implementation.gui.widget.PlayerBoardWidget;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends View {
    private static GUI instance;

    private Stage stage;
    private Scene scene;

    public GUI(Client client, Stage stage) {
        super(client);
        this.stage = stage;

        this.setModelUpdateHandler(new GUIModelUpdateHandler(this));
        this.setRenderer(new GUIRenderer(this));
        this.setActionSender(new CLIActionSender(this));

        instance = this;
    }

    public static GUI instance() {
        return instance;
    }

    @Override
    public void addToLobby(boolean isFirstConnection) {
        super.addToLobby(isFirstConnection);
        Parent nameSelectionPage = FXMLUtils.loadFXML("/gui/NameSelection");
        scene.setRoot(nameSelectionPage);
    }

    @Override
    public void handlePlayerConnect(String playerName, int currentPlayers, int playersToStart) {
        super.handlePlayerConnect(playerName, currentPlayers, playersToStart);
        if(getClient().isNoServer()) {
            Parent gameConfigSelection = FXMLUtils.loadFXML("/gui/GameConfigSelection");
            scene.setRoot(gameConfigSelection);
            return;
        }

        getModel().updatePlayerCount(currentPlayers, playersToStart);

        if(playerName.equals(getPlayerName())) {
            if(isLobbyMaster()) {
                Parent playersToStartSelection = FXMLUtils.loadFXML("/gui/PlayersToStartSelection");
                scene.setRoot(playersToStartSelection);
            } else {
                Parent waitingPlayers = FXMLUtils.loadFXML("/gui/WaitingPlayers");
                scene.setRoot(waitingPlayers);
            }
        }
    }

    @Override
    public void handleSetPlayersToStart(int playersToStart) {
        super.handleSetPlayersToStart(playersToStart);
        getModel().updatePlayerCount(getModel().getPlayers().size(), playersToStart);

        Parent gameConfigSelection = FXMLUtils.loadFXML("/gui/GameConfigSelection");
        scene.setRoot(gameConfigSelection);
    }

    @Override
    public void handleSetGameConfig() {
        super.handleSetGameConfig();
        Parent waitingPlayers = FXMLUtils.loadFXML("/gui/WaitingPlayers");
        scene.setRoot(waitingPlayers);
    }

    @Override
    public void handlePlayerDisconnect(String playerName) {
        getModel().getPlayers().remove(playerName);
    }

    @Override
    public void handlePlayerCrash(String playerName) {
        getRenderer().showErrorMessage("Player " + playerName + " crashed! The game is over!");
        reset();

        Parent homePage = FXMLUtils.loadFXML("/gui/Home");
        scene.setRoot(homePage);
    }

    @Override
    public void handleGameStart(GameConfig gameConfig) {
        super.handleGameStart(gameConfig);
        PlayerBoardWidget localPlayerBoard = new PlayerBoardWidget(getModel().getLocalPlayer());
        scene.setRoot(localPlayerBoard);
    }

    @Override
    public void run() {
        Parent homePage = FXMLUtils.loadFXML("/gui/Home");

        Font.loadFont(getClass().getResourceAsStream("/fonts/Girassol-Regular.ttf"), 16);

        scene = new Scene(homePage);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        if(getClient().isNoServer())
            addToLobby(false);
    }

    public Scene getScene() {
        return scene;
    }
}
