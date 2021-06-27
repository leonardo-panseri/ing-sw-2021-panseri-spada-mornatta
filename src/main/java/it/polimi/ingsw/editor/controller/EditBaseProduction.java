package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.editor.EditorGUIUtils;
import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.BaseProductionPower;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.implementation.gui.widget.BaseProductionWidget;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * Widget to edit the BaseProduction.
 */
public class EditBaseProduction extends BorderPane {
    @FXML
    private VBox baseProductionDisplay;
    @FXML
    private VBox inputResources;
    @FXML
    private VBox outputResources;

    private BaseProductionWidget baseProductionWidget;
    private final Map<Resource, BorderPane> inputControls;
    private final Map<Resource, BorderPane> outputControls;

    /**
     * Constructs a new EditBaseProduction widget.
     */
    public EditBaseProduction() {
        this.baseProductionWidget = new BaseProductionWidget(GameConfigEditor.getGameConfig().getBaseProductionPower());
        inputControls = new HashMap<>();
        outputControls = new HashMap<>();

        FXMLUtils.loadEditorFXML(this);
    }

    @FXML
    private void initialize() {
        baseProductionDisplay.getChildren().add(baseProductionWidget);
        VBox.setMargin(baseProductionWidget, new Insets(0, 20, 0, 20));

        BaseProductionPower productionPower = baseProductionWidget.getBaseProduction();
        BorderPane anyInputControl = EditorGUIUtils.buildControl("ANY", productionPower.getInputMap().getOrDefault(null, -1));
        inputResources.getChildren().add(anyInputControl);
        inputControls.put(null, anyInputControl);
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            BorderPane control = EditorGUIUtils.buildControl(resource.toString(), productionPower.getInputMap().getOrDefault(resource, -1));
            inputResources.getChildren().add(control);
            inputControls.put(resource, control);
        });

        BorderPane anyOutputControl = EditorGUIUtils.buildControl("ANY", productionPower.getOutputMap().getOrDefault(null, -1));
        outputResources.getChildren().add(anyOutputControl);
        outputControls.put(null, anyOutputControl);
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            BorderPane control = EditorGUIUtils.buildControl(resource.toString(), productionPower.getOutputMap().getOrDefault(resource, -1));
            outputResources.getChildren().add(control);
            outputControls.put(resource, control);
        });
    }

    /**
     * Navigates to the home page.
     */
    @FXML
    private void goToHome() {
        GameConfigEditor.goToHome();
    }

    /**
     * Saves the modified base production to the custom GameConfig.
     */
    @FXML
    private void saveBaseProduction() {
        List<Resource> newInput = new ArrayList<>();
        List<Resource> newOutput = new ArrayList<>();

        inputControls.forEach((resource, control) -> {
            int quantity = EditorGUIUtils.getSelectedQuantityForControl(control);
            while (quantity > 0) {
                newInput.add(resource);
                quantity--;
            }
        });

        outputControls.forEach((resource, control) -> {
            int quantity = EditorGUIUtils.getSelectedQuantityForControl(control);
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
        GameConfigEditor.setSavable(true);

        this.baseProductionWidget = modifiedWidget;
    }
}
