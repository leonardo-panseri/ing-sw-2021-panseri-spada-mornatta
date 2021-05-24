package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.server.GameConfig;
import it.polimi.ingsw.view.GameState;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.*;

public class PlayerBoardWidget extends StackPane {
    @FXML
    public HBox faithTrackDisplay;
    @FXML
    public Pane playerBoardDisplay;
    @FXML
    public Pane depositDisplay;
    @FXML
    public Pane baseProductionDisplay;

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

//        if(GUI.instance().gameStateProperty().get() == GameState.SELECT_LEADERS && GUI.instance().isOwnTurn())
//            openLeaderSelection();
//        GUI.instance().gameStateProperty().addListener((change, oldState, newState) -> {
//            if(newState == GameState.SELECT_LEADERS && GUI.instance().isOwnTurn()) {
//                openLeaderSelection();
//            }
//        });
    }

    private void openLeaderSelection() {
        Platform.runLater(() -> {
            LeaderSelectionWidget selectionWidget = new LeaderSelectionWidget(GUI.instance().getModel().getLocalPlayer().getLeaderCards().keySet());
            getChildren().add(selectionWidget);
            System.out.println("Added");;
        });
    }
}
