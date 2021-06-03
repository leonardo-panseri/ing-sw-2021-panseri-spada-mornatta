package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class ProductionWidget extends FlowPane {
    @FXML
    private VBox resourcesDisplay;
    @FXML
    private VBox productionDisplay;
    @FXML
    private Button exitButton;
    @FXML
    private Button confirmButton;

    private final PlayerBoardWidget playerBoard;

    public ProductionWidget(PlayerBoardWidget playerBoard) {
        this.playerBoard = playerBoard;

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void closeProductionModal() {
        playerBoard.closeProductionModal();
    }
}
