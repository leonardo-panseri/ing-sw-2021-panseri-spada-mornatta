package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.FXMLUtils;
import it.polimi.ingsw.editor.GUIUtils;
import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.BaseProductionPower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class EditBaseProduction extends BorderPane {
    @FXML
    public VBox baseProductionDisplay;
    @FXML
    public VBox inputResources;
    @FXML
    public VBox outputResources;

    private BaseProductionWidget baseProductionWidget;
    private final Map<Resource, BorderPane> inputControls;
    private final Map<Resource, BorderPane> outputControls;

    public EditBaseProduction() {
        this.baseProductionWidget = new BaseProductionWidget(GameConfigEditor.getGameConfig().getBaseProductionPower());
        inputControls = new HashMap<>();
        outputControls = new HashMap<>();

        FXMLUtils.loadFXML(this);
    }

    @FXML
    private void initialize() {
        baseProductionDisplay.getChildren().add(baseProductionWidget);
        VBox.setMargin(baseProductionWidget, new Insets(0, 20, 0, 20));

        BaseProductionPower productionPower = baseProductionWidget.getBaseProduction();
        BorderPane anyInputControl = GUIUtils.buildControl("ANY", productionPower.getInputMap().getOrDefault(null, -1));
        inputResources.getChildren().add(anyInputControl);
        inputControls.put(null, anyInputControl);
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            BorderPane control = GUIUtils.buildControl(resource.toString(), productionPower.getInputMap().getOrDefault(resource, -1));
            inputResources.getChildren().add(control);
            inputControls.put(resource, control);
        });

        BorderPane anyOutputControl = GUIUtils.buildControl("ANY", productionPower.getOutputMap().getOrDefault(null, -1));
        outputResources.getChildren().add(anyOutputControl);
        outputControls.put(null, anyOutputControl);
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            BorderPane control = GUIUtils.buildControl(resource.toString(), productionPower.getOutputMap().getOrDefault(resource, -1));
            outputResources.getChildren().add(control);
            outputControls.put(resource, control);
        });
    }

    @FXML
    public void goToHome() {
        GameConfigEditor.goToHome();
    }

    @FXML
    public void saveBaseProduction() {
        List<Resource> newInput = new ArrayList<>();
        List<Resource> newOutput = new ArrayList<>();

        inputControls.forEach((resource, control) -> {
            int quantity = GUIUtils.getSelectedQuantityForControl(control);
            while (quantity > 0) {
                newInput.add(resource);
                quantity--;
            }
        });

        outputControls.forEach((resource, control) -> {
            int quantity = GUIUtils.getSelectedQuantityForControl(control);
            while (quantity > 0) {
                newOutput.add(resource);
                quantity--;
            }
        });

        BaseProductionPower modifiedBaseProduction = new BaseProductionPower(newInput, newOutput);
        BaseProductionWidget modifiedWidget = new BaseProductionWidget(modifiedBaseProduction);
        baseProductionDisplay.getChildren().set(0, modifiedWidget);
        VBox.setMargin(modifiedWidget, new Insets(0, 20, 0, 20));

        GameConfigEditor.getGameConfig().modifyBaseProduction(modifiedBaseProduction);
        GameConfigEditor.setSavable();

        this.baseProductionWidget = modifiedWidget;
    }
}
