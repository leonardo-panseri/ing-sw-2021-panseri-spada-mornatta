package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.editor.GUIUtils;
import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.implementation.gui.widget.DevelopmentCardWidget;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditDevelopmentCard extends BorderPane {
    @FXML
    private VBox devCardDisplay;
    @FXML
    private VBox cost;
    @FXML
    private TextField victoryPoints;
    @FXML
    private VBox inputResources;
    @FXML
    private VBox outputResources;

    private DevelopmentCardWidget developmentCardWidget;
    private final Map<Resource, BorderPane> costControls;
    private final Map<Resource, BorderPane> inputResourcesControls;
    private final Map<Resource, BorderPane> outputResourcesControls;

    public EditDevelopmentCard(DevelopmentCardWidget developmentCardWidget) {
        this.developmentCardWidget = developmentCardWidget;
        costControls = new HashMap<>();
        inputResourcesControls = new HashMap<>();
        outputResourcesControls = new HashMap<>();

        FXMLUtils.loadEditorFXML(this);
    }

    @FXML
    private void initialize() {
        VBox.setMargin(developmentCardWidget, new Insets(0, 20, 0, 20));
        devCardDisplay.getChildren().setAll(developmentCardWidget);

        Map<Resource, Integer> costRes = developmentCardWidget.getDevelopmentCard().getCost();
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            BorderPane control = GUIUtils.buildControl(resource.toString(), costRes.getOrDefault(resource, -1));
            this.cost.getChildren().add(control);
            costControls.put(resource, control);
        });

        victoryPoints.setTextFormatter(GUIUtils.getNumberInputTextFormatter());
        victoryPoints.setText("" + developmentCardWidget.getDevelopmentCard().getVictoryPoints());

        Map<Resource, Integer> inputRes = developmentCardWidget.getDevelopmentCard().getProductionInput();
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            BorderPane control = GUIUtils.buildControl(resource.toString(), inputRes.getOrDefault(resource, -1));
            this.inputResources.getChildren().add(control);
            inputResourcesControls.put(resource, control);
        });

        Map<Resource, Integer> outputRes = developmentCardWidget.getDevelopmentCard().getProductionOutput();
        Arrays.stream(Resource.values()).forEach(resource -> {
            BorderPane control = GUIUtils.buildControl(resource.toString(), outputRes.getOrDefault(resource, -1));
            this.outputResources.getChildren().add(control);
            outputResourcesControls.put(resource, control);
        });
    }

    @FXML
    private void saveDevelopmentCard() {
        Map<Resource, Integer> cost = new HashMap<>();
        int victoryPoints = 0;
        Map<Resource, Integer> prodInput = new HashMap<>();
        Map<Resource, Integer> prodOutput = new HashMap<>();

        costControls.forEach((resource, control) -> {
            int quantity = GUIUtils.getSelectedQuantityForControl(control);
            if (quantity != 0) cost.put(resource, quantity);
        });

        try {
            victoryPoints = Integer.parseInt(this.victoryPoints.getText());
        } catch (NumberFormatException ignored) {}

        inputResourcesControls.forEach((resource, control) -> {
            int quantity = GUIUtils.getSelectedQuantityForControl(control);
            if (quantity != 0) prodInput.put(resource, quantity);
        });

        outputResourcesControls.forEach((resource, control) -> {
            int quantity = GUIUtils.getSelectedQuantityForControl(control);
            if (quantity != 0) prodOutput.put(resource, quantity);
        });

        DevelopmentCard oldCard = developmentCardWidget.getDevelopmentCard();
        DevelopmentCard modifiedCard = new DevelopmentCard(victoryPoints, cost, oldCard.getLevel(), prodInput,
                prodOutput, oldCard.getColor());
        DevelopmentCardWidget modifiedWidget = new DevelopmentCardWidget(modifiedCard);

        devCardDisplay.getChildren().setAll(modifiedWidget);

        GameConfigEditor.getGameConfig().modifyDevelopmentCard(oldCard, modifiedCard);
        GameConfigEditor.setSavable(true);

        this.developmentCardWidget = modifiedWidget;
        VBox.setMargin(developmentCardWidget, new Insets(0, 20, 0, 20));
    }

    @FXML
    private void goToEditDevelopmentCards() {
        GameConfigEditor.goToEditDevelopmentCards();
    }
}
