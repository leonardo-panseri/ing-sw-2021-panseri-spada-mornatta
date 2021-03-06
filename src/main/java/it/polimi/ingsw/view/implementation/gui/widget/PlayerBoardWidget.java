package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import it.polimi.ingsw.view.messages.production.BaseProduction;
import it.polimi.ingsw.view.messages.production.Production;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Main widget that represents a player board.
 */

public class PlayerBoardWidget extends StackPane {
    @FXML
    public Pane musicButtonDisplay;
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
    @FXML
    private Label productionLabel;
    @FXML
    private VBox productionExecute;

    private final MockPlayer player;
    private DepositWidget depositWidget;

    /**
     * Creates a player board widget for a given player.
     * @param player the player that owns the player board
     */

    public PlayerBoardWidget(MockPlayer player) {
        this.player = player;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        initializeWidgets();

        messageDisplay.setText(GUI.instance().isOwnTurn() ? "It's your turn" :
                "It's " + GUI.instance().getModel().currentPlayerNameProperty().get() + "'s turn");
        GUI.instance().getModel().currentPlayerNameProperty().addListener((change, oldVal, newVal) -> Platform.runLater(() -> {
            if (GUI.instance().getPlayerName().equals(newVal)) {
                messageDisplay.setText("It's your turn");
            } else {
                messageDisplay.setText("It's " + newVal + "'s turn");
            }
        }));

        GUI.instance().getModel().lorenzoActionProperty().setValueListener(value ->
                Platform.runLater(() -> lorenzoActionDisplay.setText(value)));

        if(!GUI.instance().getActionSender().getPendingProductions().isEmpty()) {
            productionLabel.setText("You have " + GUI.instance().getActionSender().getPendingProductions().size() + " queued productions:");
            productionExecute.setVisible(true);
        }
        GUI.instance().getActionSender().pendingProductionsProperty().addListener(
                (ListChangeListener<? super Production>) change -> Platform.runLater(() -> {
                    if (change.getList().size() > 0) {
                        productionLabel.setText("You have " + change.getList().size() + " queued productions:");
                        productionExecute.setVisible(true);
                    } else {
                        productionExecute.setVisible(false);
                    }
                }));

        if (GUI.instance().getGameState() == GameState.SELECT_LEADERS) {
            openLeaderSelection();
        } else if (GUI.instance().getGameState() == GameState.WAIT_SELECT_LEADERS) {
            openWaitForLeaderSelection();
        }

        if (GUI.instance().getGameState() != GameState.PLAYING) {
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

    /**
     * Creates all sub-widgets included in the player board.
     */
    private void initializeWidgets() {
        GameConfig gameConfig = GUI.instance().getModel().getGameConfig();
        FaithTrackWidget faithTrackWidget = new FaithTrackWidget(gameConfig.getPopeReports(), gameConfig.getFaithTrackPoints(), player);
        faithTrackWidget.setScaleX(1.6);
        faithTrackWidget.setScaleY(1.6);
        faithTrackDisplay.getChildren().add(new Group(faithTrackWidget));

        depositWidget = new DepositWidget(player);
        depositDisplay.getChildren().add(depositWidget);

        LeaderDisplayWidget leaderDisplayWidget = new LeaderDisplayWidget(this);
        leadersDisplay.getChildren().add(leaderDisplayWidget);

        DevelopmentSlotsWidget developmentSlotsWidget = new DevelopmentSlotsWidget(this);
        developmentDisplay.getChildren().add(developmentSlotsWidget);

        MarketResultsWidget marketResultsWidget = new MarketResultsWidget(this);
        marketResultsDisplay.getChildren().add(marketResultsWidget);

        initializeOtherPlayersDisplay();

        BaseProductionWidget baseProductionWidget = new BaseProductionWidget(gameConfig.getBaseProductionPower());
        baseProductionWidget.setScaleX(0.42);
        baseProductionWidget.setScaleY(0.42);
        baseProductionDisplay.getChildren().add(baseProductionWidget);
        baseProductionWidget.setOnMouseClicked(mouseEvent -> {
            if (player.isLocalPlayer())
                openProductionModal(baseProductionWidget.getBaseProduction().getInput(), baseProductionWidget.getBaseProduction().getOutput(),
                        BaseProduction.class, null);
        });

        ChatWidget chatWidget = new ChatWidget();
        chatDisplay.getChildren().add(chatWidget);

        StrongBoxWidget strongBoxWidget = new StrongBoxWidget(player);
        strongBoxDisplay.getChildren().add(strongBoxWidget);

        MusicButtonWidget musicButtonWidget = new MusicButtonWidget(GUI.instance().getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING);
        musicButtonDisplay.getChildren().add(musicButtonWidget);
    }

    /**
     * Creates the other user images that, after being clicked on, show the relative player's board.
     */
    private void initializeOtherPlayersDisplay() {
        String path = "/images/user.png";
        InputStream imgIs = GUIUtils.class.getResourceAsStream(path);
        if (imgIs == null) {
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

        if (player == GUI.instance().getModel().getLocalPlayer())
            currentPlayerBox.getStyleClass().add("selected");

        currentPlayerBox.setOnMouseClicked(mouseEvent ->
                Platform.runLater(() ->
                        getScene().setRoot(new PlayerBoardWidget(GUI.instance().getModel().getLocalPlayer()))));

        otherPlayersDisplay.getChildren().add(currentPlayerBox);

        for (MockPlayer player : GUI.instance().getModel().getPlayers().values()) {
            if (player.isLocalPlayer())
                continue;
            ImageView img = new ImageView(image);
            BorderPane imgWrapper = new BorderPane(img);
            imgWrapper.setPrefHeight(60);
            imgWrapper.setPrefWidth(60);
            Label playerName = new Label(player.getName());
            VBox playerBox = new VBox(imgWrapper, playerName);
            playerBox.setAlignment(Pos.CENTER);

            if (this.player == player)
                playerBox.getStyleClass().add("selected");

            playerBox.setOnMouseClicked(mouseEvent ->
                    Platform.runLater(() -> getScene().setRoot(new PlayerBoardWidget(player))));

            otherPlayersDisplay.getChildren().add(playerBox);
        }
    }

    /**
     * Opens the selection widget for the initial phase of the game, when player has to choose leaders and resources.
     */
    private void openLeaderSelection() {
        if (!isLeaderSelectionOpen()) {
            LeaderSelectionWidget selectionWidget = new LeaderSelectionWidget(GUI.instance().getModel().getLocalPlayer().getLeaderCards().keySet());
            getChildren().add(selectionWidget);
        } else
            System.out.println("leader selection not open");
    }

    /**
     * Closes the selection widget for the initial phase of the game, when player has to choose leaders and resources.
     */
    private void closeLeaderSelection() {
        if (isLeaderSelectionOpen())
            getChildren().remove(1);
    }

    /**
     * Checks if the initial selection widget is open.
     * @return true if the widget is open
     */
    private boolean isLeaderSelectionOpen() {
        return getChildren().size() > 1 && getChildren().get(1) instanceof LeaderSelectionWidget;
    }

    /**
     * Opens the waiting widget for the initial phase of the game,
     * when has to wait while other choose initial leaders and resources.
     */
    private void openWaitForLeaderSelection() {
        if (!isWaitForLeaderSelectionOpen()) {
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

    /**
     * Closes the waiting widget for the initial phase of the game,
     * when has to wait while other choose initial leaders and resources.
     */
    private void closeWaitForLeaderSelection() {
        if (isWaitForLeaderSelectionOpen())
            getChildren().remove(1);
    }

    /**
     * Checks if the initial selection waiting widget is open.
     * @return true if the widget is open
     */
    private boolean isWaitForLeaderSelectionOpen() {
        return getChildren().size() > 1 && getChildren().get(1) instanceof HBox;
    }

    /**
     * Opens a production modal when a production icon is clicked
     * (base production, development production or leader production)
     * @param input the required input resources
     * @param output the required output resources
     * @param prodType the type of the production
     * @param card null if the production is the base production
     */
    protected void openProductionModal(List<Resource> input, List<Resource> output, Class<? extends Production> prodType, Card card) {
        if (!isProductionModalOpen()) {
            if(player.isLocalPlayer() && GUI.instance().isOwnTurn()) {
                Platform.runLater(() -> {
                    ProductionWidget productionWidget = new ProductionWidget(this, input, output, prodType, card);
                    getChildren().add(productionWidget);
                });
            }
        }
    }

    /**
     * Closes the production modal
     */
    protected void closeProductionModal() {
        if (isProductionModalOpen())
            Platform.runLater(() -> getChildren().remove(1));
    }

    /**
     * Checks if the production modal is open.
     * @return true if a production modal is open
     */
    private boolean isProductionModalOpen() {
        return getChildren().size() > 1 && getChildren().get(1) instanceof ProductionWidget;
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

    @FXML
    private void executeProductions() {
        GUI.instance().getActionSender().executeProductions();
    }

    protected MockPlayer getPlayer() {
        return player;
    }

    protected DepositWidget getDepositWidget() {
        return depositWidget;
    }

    public void openEndGameModal(Map<String, Integer> scores, String winnerName) {
        EndGameWidget endGameWidget = new EndGameWidget(scores, winnerName);
        getChildren().add(endGameWidget);
    }

    public void openEndGameModal(boolean lorenzoWin, String loseReason, int playerScore) {
        EndGameWidget endGameWidget = new EndGameWidget(lorenzoWin, loseReason, playerScore);
        getChildren().add(endGameWidget);
    }
}
