package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class PlayerBoardWidget extends StackPane {
    @FXML
    private HBox faithTrackDisplay;
    @FXML
    private Pane playerBoardDisplay;
    @FXML
    private Pane depositDisplay;
    @FXML
    private Pane baseProductionDisplay;

    private final MockPlayer player;
    public PlayerBoardWidget(MockPlayer player) {
        this.player = player;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        GameConfig gameConfig = GUI.instance().getModel().getGameConfig();
        FaithTrackWidget faithTrackWidget = new FaithTrackWidget(gameConfig.getPopeReports(), gameConfig.getFaithTrackPoints());
        faithTrackWidget.setScaleX(1.6);
        faithTrackWidget.setScaleY(1.6);
        faithTrackDisplay.getChildren().add(faithTrackWidget);

        DepositWidget depositWidget = new DepositWidget(player);
        depositDisplay.getChildren().add(depositWidget);

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
}
