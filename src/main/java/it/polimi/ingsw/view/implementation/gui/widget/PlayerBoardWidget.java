package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.InputStream;

public class PlayerBoardWidget extends StackPane {
    @FXML
    private Label messageDisplay;
    @FXML
    private Pane leadersDisplay;
    @FXML
    private Pane marketResultsDisplay;
    @FXML
    private FlowPane otherPlayersDisplay;
    @FXML
    public Pane developmentDisplay;
    @FXML
    private HBox faithTrackDisplay;
    @FXML
    private Pane playerBoardDisplay;
    @FXML
    private Pane depositDisplay;
    @FXML
    private Pane baseProductionDisplay;
    @FXML
    private Pane chatDisplay;
    @FXML
    private Pane strongBoxDisplay;
    @FXML
    private Label lorenzoActionDisplay;

    private final MockPlayer player;
    private DepositWidget depositWidget;
    public PlayerBoardWidget(MockPlayer player) {
        this.player = player;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        GameConfig gameConfig = GUI.instance().getModel().getGameConfig();
        FaithTrackWidget faithTrackWidget = new FaithTrackWidget(gameConfig.getPopeReports(), gameConfig.getFaithTrackPoints(),player);
        faithTrackWidget.setScaleX(1.6);
        faithTrackWidget.setScaleY(1.6);
        faithTrackDisplay.getChildren().add(new Group(faithTrackWidget));

        depositWidget = new DepositWidget(player);
        depositDisplay.getChildren().add(depositWidget);

        LeaderDisplayWidget leaderDisplayWidget = new LeaderDisplayWidget(player);
        leadersDisplay.getChildren().add(leaderDisplayWidget);

        DevelopmentSlotsWidget developmentSlotsWidget = new DevelopmentSlotsWidget(player);
        developmentDisplay.getChildren().add(developmentSlotsWidget);

        MarketResultsWidget marketResultsWidget = new MarketResultsWidget(this);
        marketResultsDisplay.getChildren().add(marketResultsWidget);

        initializeOtherPlayersDisplay();

        BaseProductionWidget baseProductionWidget = new BaseProductionWidget(gameConfig.getBaseProductionPower());
        baseProductionWidget.setScaleX(0.42);
        baseProductionWidget.setScaleY(0.42);
        baseProductionDisplay.getChildren().add(baseProductionWidget);

        ChatWidget chatWidget = new ChatWidget();
        chatDisplay.getChildren().add(chatWidget);

        StrongBoxWidget strongBoxWidget = new StrongBoxWidget(player);
        strongBoxDisplay.getChildren().add(strongBoxWidget);

        Platform.runLater(() -> messageDisplay.setText(
                GUI.instance().isOwnTurn() ? "It's your turn" :
                        "It's " + GUI.instance().getModel().currentPlayerNameProperty().get() + " turn"));
        GUI.instance().getModel().currentPlayerNameProperty().addListener((change, oldVal, newVal) -> Platform.runLater(() -> {
            if (GUI.instance().getPlayerName().equals(newVal)) {
                messageDisplay.setText("It's your turn");
            } else {
                messageDisplay.setText("It's " + newVal + " turn");
            }
        }));

        GUI.instance().getModel().lorenzoActionProperty().setValueListener(value ->
                Platform.runLater(() -> lorenzoActionDisplay.setText(value)));

        if (GUI.instance().getGameState() == GameState.SELECT_LEADERS) {
            openLeaderSelection();
        } else if(GUI.instance().getGameState() == GameState.WAIT_SELECT_LEADERS) {
            openWaitForLeaderSelection();
        }

        if(GUI.instance().getGameState() != GameState.PLAYING) {
            GUI.instance().gameStateProperty().addListener((change, oldState, newState) -> {
                if ((oldState == GameState.SELECT_LEADERS && newState == GameState.WAIT_SELECT_LEADERS)
                    || (oldState == GameState.CHOOSING_RESOURCES && newState == GameState.WAIT_SELECT_LEADERS)) {
                    Platform.runLater(() -> {
                        closeLeaderSelection();
                        openWaitForLeaderSelection();
                    });
                } else if (oldState == GameState.WAIT_SELECT_LEADERS && newState == GameState.SELECT_LEADERS) {
                    Platform.runLater(() -> {
                        closeWaitForLeaderSelection();
                        openLeaderSelection();
                    });
                } else if (newState == GameState.PLAYING) {
                    Platform.runLater(this::closeWaitForLeaderSelection);
                }
            });
        }
    }

    private void initializeOtherPlayersDisplay() {
        String path = "/images/user.png" ;
        InputStream imgIs = GUIUtils.class.getResourceAsStream(path);
        if(imgIs == null) {
            System.err.println("User image not found");
            return;
        }
        Image image = new Image(imgIs, 50, 50, true, true);

        ImageView currentPlayerImg = new ImageView(image);
        BorderPane currentPlayerImgWrapper = new BorderPane(currentPlayerImg);
        currentPlayerImgWrapper.setPrefHeight(60);
        currentPlayerImgWrapper.setPrefWidth(60);
        Label currentPlayerName = new Label("You");
        VBox currentPlayerBox = new VBox(currentPlayerImgWrapper, currentPlayerName);
        currentPlayerBox.setAlignment(Pos.CENTER);

        if(player == GUI.instance().getModel().getLocalPlayer())
            currentPlayerBox.getStyleClass().add("selected");

        currentPlayerBox.setOnMouseClicked(mouseEvent ->
                Platform.runLater(() ->
                        getScene().setRoot(new PlayerBoardWidget(GUI.instance().getModel().getLocalPlayer()))));

        otherPlayersDisplay.getChildren().add(currentPlayerBox);

        for(MockPlayer player : GUI.instance().getModel().getPlayers().values()) {
            if(player.isLocalPlayer())
                continue;
            ImageView img = new ImageView(image);
            BorderPane imgWrapper = new BorderPane(img);
            imgWrapper.setPrefHeight(60);
            imgWrapper.setPrefWidth(60);
            Label playerName = new Label(player.getName());
            VBox playerBox = new VBox(imgWrapper, playerName);
            playerBox.setAlignment(Pos.CENTER);

            if(this.player == player)
                playerBox.getStyleClass().add("selected");

            playerBox.setOnMouseClicked(mouseEvent ->
                    Platform.runLater(() -> getScene().setRoot(new PlayerBoardWidget(player))));

            otherPlayersDisplay.getChildren().add(playerBox);
        }
    }

    private void openLeaderSelection() {
        if(!isLeaderSelectionOpen()) {
            LeaderSelectionWidget selectionWidget = new LeaderSelectionWidget(GUI.instance().getModel().getLocalPlayer().getLeaderCards().keySet());
            getChildren().add(selectionWidget);
        } else
            System.out.println("leader selection not open");
    }

    private void closeLeaderSelection() {
        if(isLeaderSelectionOpen())
            getChildren().remove(1);
    }

    private boolean isLeaderSelectionOpen() {
        return getChildren().size() > 1 && getChildren().get(1) instanceof LeaderSelectionWidget;
    }

    private void openWaitForLeaderSelection() {
        if(!isWaitForLeaderSelectionOpen()) {
            Label text = new Label("Wait while other players select their Leader cards...");
            text.getStyleClass().add("leader-select-title");
            FlowPane pane = new FlowPane(text);
            pane.getStyleClass().add("selection-box");
            pane.setAlignment(Pos.CENTER);
            pane.setPrefHeight(400);
            pane.setPrefWidth(700);
            VBox vBox = new VBox(pane);
            vBox.setAlignment(Pos.CENTER);
            HBox box = new HBox(vBox);
            box.getStyleClass().add("leader-selection");
            box.setAlignment(Pos.CENTER);
            box.setPrefHeight(720);
            box.setPrefWidth(1080);
            getChildren().add(box);
        } else
            System.out.println("leader wait not open");
    }

    private void closeWaitForLeaderSelection() {
        if(isWaitForLeaderSelectionOpen())
            getChildren().remove(1);
    }

    private boolean isWaitForLeaderSelectionOpen() {
        return getChildren().size() > 1 && getChildren().get(1) instanceof HBox;
    }

    @FXML
    private void goToMarket() {
        Platform.runLater(() -> {
            MarketWidget marketWidget = new MarketWidget();
            getScene().setRoot(marketWidget);
        });
    }

    @FXML
    private void goToDeck() {
        Platform.runLater(() -> {
            DeckWidget deckWidget = new DeckWidget();
            getScene().setRoot(deckWidget);
        });
    }

    @FXML
    private void endTurn() {
        GUI.instance().getActionSender().endTurn();
    }

    protected MockPlayer getPlayer() {
        return player;
    }

    protected DepositWidget getDepositWidget() {
        return depositWidget;
    }
}
