package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.implementation.gui.widget.PlayerBoardWidget;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.Media;

import java.sql.Time;
import java.util.*;

public class GUI extends View {
    private static GUI instance;

    private final Stage stage;
    private Scene scene;
    private MediaPlayer mediaPlayer;

    public GUI(Client client, Stage stage) {
        super(client);
        this.stage = stage;

        this.setModelUpdateHandler(new GUIModelUpdateHandler(this));
        this.setRenderer(new GUIRenderer(this));
        this.setActionSender(new GUIActionSender(this));

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
    public void handlePlayerConnect(String playerName, int currentPlayers, int playersToStart, List<String> otherConnectedPlayers) {
        super.handlePlayerConnect(playerName, currentPlayers, playersToStart, otherConnectedPlayers);
        if(getClient().isNoServer()) {
            Platform.runLater(() -> {
                Parent gameConfigSelection = FXMLUtils.loadFXML("/gui/GameConfigSelection");
                scene.setRoot(gameConfigSelection);
            });
            return;
        }

        getModel().updatePlayerCount(currentPlayers, playersToStart);

        if (playerName.equals(getPlayerName())) {
            if (isLobbyMaster()) {
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
        resetAndGoHome();
    }

    public void resetAndGoHome() {
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
    public void handleEndGame(Map<String, Integer> scores, String winnerName) {
        Platform.runLater(() -> {
            PlayerBoardWidget playerBoardWidget = new PlayerBoardWidget(getModel().getLocalPlayer());
            scene.setRoot(playerBoardWidget);
            playerBoardWidget.openEndGameModal(scores, winnerName);
        });
    }

    @Override
    public void handleEndSingleplayerGame(boolean lorenzoWin, String loseReason, int playerScore) {
        Platform.runLater(() -> {
            PlayerBoardWidget playerBoardWidget = new PlayerBoardWidget(getModel().getLocalPlayer());
            scene.setRoot(playerBoardWidget);
            playerBoardWidget.openEndGameModal(lorenzoWin, loseReason, playerScore);
        });
    }

    @Override
    public void run() {
        Parent homePage = FXMLUtils.loadFXML("/gui/Home");

        Font.loadFont(GUI.class.getResourceAsStream("/fonts/Girassol-Regular.ttf"), 16);

        Media media = new Media(Objects.requireNonNull(GUI.class.getResource("/media/soundtrack.mp3")).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(0.05);
        stage.focusedProperty().addListener((change, oldVal, newVal) -> {
            if(oldVal && !newVal)
                mediaPlayer.pause();
            else if(!oldVal && newVal)
                mediaPlayer.play();
        });

        scene = new Scene(homePage);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Masters Of Renaissance");
        stage.getIcons().add(new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/images/app-icon.png"))));
        stage.show();

        stage.setOnCloseRequest(windowEvent -> getClient().terminate());

        if (getClient().isNoServer())
            addToLobby(false);
    }

    public Scene getScene() {
        return scene;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
