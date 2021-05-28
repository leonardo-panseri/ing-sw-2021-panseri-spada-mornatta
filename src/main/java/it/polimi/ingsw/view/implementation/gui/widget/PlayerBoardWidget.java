package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlayerBoardWidget extends StackPane {
    @FXML
    public Pane leadersDisplay;
    @FXML
    public FlowPane marketResultsDisplay;
    @FXML
    private HBox faithTrackDisplay;
    @FXML
    private Pane playerBoardDisplay;
    @FXML
    private Pane depositDisplay;
    @FXML
    private Pane baseProductionDisplay;

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

        initializeMarketResultsDisplay();

        BaseProductionWidget baseProductionWidget = new BaseProductionWidget(gameConfig.getBaseProductionPower());
        baseProductionWidget.setScaleX(0.42);
        baseProductionWidget.setScaleY(0.42);
        baseProductionDisplay.getChildren().add(baseProductionWidget);

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

    private void initializeMarketResultsDisplay() {
        for(int i = 0; i < 4; i++) {
            ImageView img = new ImageView();
            BorderPane imgWrapper = new BorderPane(img);
            imgWrapper.setPrefWidth(60);
            imgWrapper.setPrefHeight(60);
            imgWrapper.getStyleClass().add("resource-box");
            if(i != 3)
                imgWrapper.getStyleClass().add("margin-right");
            marketResultsDisplay.getChildren().add(imgWrapper);
        }

        updateMarketResults(player.getDeposit().marketResultProperty());
        player.getDeposit().marketResultProperty().addListener((ListChangeListener<Resource>) change -> updateMarketResults(change.getList().stream()
                .map(resToCast -> (Resource) resToCast).collect(Collectors.toList())));
    }

    private void updateMarketResults(List<Resource> resources) {


        for(int i = 0; i < 4; i++) {
            ImageView imageView = (ImageView) ((BorderPane) marketResultsDisplay.getChildren().get(i)).getCenter();
            if(i < resources.size()) {
                imageView.setImage(GUIUtils.getResourceImage(resources.get(i), 50, 50));
                int finalI = i;
                imageView.setOnDragDetected(mouseEvent -> {
                    depositWidget.setDropAllowed(true);
                    depositWidget.setOnDragDroppedHandler(marketResultHandler());

                    Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);

                    ClipboardContent content = new ClipboardContent();
                    content.putString("marketResult" + (finalI + 1));
                    content.putImage(imageView.getImage());
                    db.setContent(content);

                    mouseEvent.consume();
                });
            } else {
                imageView.setImage(null);
            }
        }
    }

    private Consumer<DragEvent> marketResultHandler() {
        return dragEvent -> {
            Dragboard db = dragEvent.getDragboard();

            boolean success = true;
            int resourceIndex = -1;
            int rowIndex = -1;
            try {
                resourceIndex = Integer.parseInt(db.getString().replace("marketResult", ""));

                rowIndex = DepositWidget.getRowId(dragEvent.getGestureTarget()) + 1;
            } catch (Exception e) {
                success = false;
            }

            if(success) {
                GUI.instance().getActionSender().storeMarketResult(resourceIndex, rowIndex);
            }

            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        };
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
}
