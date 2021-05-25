package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.implementation.cli.CLIActionSender;
import it.polimi.ingsw.view.implementation.gui.widget.PlayerBoardWidget;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

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

        Platform.runLater(() -> {
            Parent nameSelectionPage = FXMLUtils.loadFXML("/gui/NameSelection");
            scene.setRoot(nameSelectionPage);
        });
    }

    @Override
    public void handlePlayerConnect(String playerName, int currentPlayers, int playersToStart) {
        super.handlePlayerConnect(playerName, currentPlayers, playersToStart);
        if(getClient().isNoServer()) {
            Platform.runLater(() -> {
                Parent gameConfigSelection = FXMLUtils.loadFXML("/gui/GameConfigSelection");
                scene.setRoot(gameConfigSelection);
            });
            return;
        }

        getModel().updatePlayerCount(currentPlayers, playersToStart);

        if(playerName.equals(getPlayerName())) {
            if(isLobbyMaster()) {
                Platform.runLater(() -> {
                    Parent playersToStartSelection = FXMLUtils.loadFXML("/gui/PlayersToStartSelection");
                    scene.setRoot(playersToStartSelection);
                });
            } else {
                Platform.runLater(() -> {
                    Parent waitingPlayers = FXMLUtils.loadFXML("/gui/WaitingPlayers");
                    scene.setRoot(waitingPlayers);
                });
            }
        }
    }

    @Override
    public void handleSetPlayersToStart(int playersToStart) {
        super.handleSetPlayersToStart(playersToStart);
        getModel().updatePlayerCount(getModel().getPlayers().size(), playersToStart);

        Platform.runLater(() -> {
            Parent gameConfigSelection = FXMLUtils.loadFXML("/gui/GameConfigSelection");
            scene.setRoot(gameConfigSelection);
        });
    }

    @Override
    public void handleSetGameConfig() {
        super.handleSetGameConfig();
        Platform.runLater(() -> {
            Parent waitingPlayers = FXMLUtils.loadFXML("/gui/WaitingPlayers");
            scene.setRoot(waitingPlayers);
        });
    }

    @Override
    public void handlePlayerDisconnect(String playerName) {
        getModel().getPlayers().remove(playerName);
    }

    @Override
    public void handlePlayerCrash(String playerName) {
        getRenderer().showErrorMessage("Player " + playerName + " crashed! The game is over!");
        reset();
        Platform.runLater(() -> {
            Parent homePage = FXMLUtils.loadFXML("/gui/Home");
            scene.setRoot(homePage);
        });
    }

    public void showPlayerBoard() {
        Platform.runLater(() -> {
            PlayerBoardWidget playerBoard = new PlayerBoardWidget(getModel().getLocalPlayer());
            scene.setRoot(playerBoard);
        });
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
