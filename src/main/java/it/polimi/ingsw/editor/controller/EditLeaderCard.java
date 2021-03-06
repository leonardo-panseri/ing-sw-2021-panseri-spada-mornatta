package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.editor.EditorGUIUtils;
import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.implementation.gui.widget.LeaderCardWidget;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

/**
 * Widget for editing a LeaderCard.
 */
public class EditLeaderCard extends BorderPane {
    @FXML
    private VBox resourceRequirements;
    @FXML
    private VBox cardColorRequirements;
    @FXML
    private VBox cardLevelRequirements;
    @FXML
    private TextField victoryPoints;
    @FXML
    private ChoiceBox<String> specialAbilityType;
    @FXML
    private ChoiceBox<String> specialAbilityResource;

    private LeaderCardWidget leaderCardWidget;
    private final Map<Resource, BorderPane> resourceRequirementsControls;
    private final Map<CardColor, BorderPane> cardColorRequirementsControls;
    private final Map<CardColor, BorderPane> cardLevelRequirementsControls;

    /**
     * Constructs a new EditLeaderCard widget.
     *
     * @param leaderCardWidget the widget for the leader card to be edited
     */
    public EditLeaderCard(LeaderCardWidget leaderCardWidget) {
        this.leaderCardWidget = leaderCardWidget;
        this.resourceRequirementsControls = new HashMap<>();
        this.cardColorRequirementsControls = new HashMap<>();
        this.cardLevelRequirementsControls = new HashMap<>();

        FXMLUtils.loadEditorFXML(this);
    }

    @FXML
    private void initialize() {
        VBox box = new VBox(leaderCardWidget);
        VBox.setMargin(leaderCardWidget, new Insets(0, 20, 0, 20));
        box.setAlignment(Pos.CENTER);
        setLeft(box);

        constructRequirementsControls();

        victoryPoints.setTextFormatter(EditorGUIUtils.getNumberInputTextFormatter());
        victoryPoints.setText("" + leaderCardWidget.getLeaderCard().getVictoryPoints());

        List<String> abilityTypes = new ArrayList<>();
        Arrays.stream(SpecialAbilityType.values()).forEach(type -> abilityTypes.add(type.toString()));
        specialAbilityType.setItems(FXCollections.observableList(abilityTypes));
        specialAbilityType.getSelectionModel().select(leaderCardWidget.getLeaderCard().getSpecialAbility().getType().toString());

        List<String> abilityResources = new ArrayList<>();
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> abilityResources.add(resource.toString()));
        specialAbilityResource.setItems(FXCollections.observableList(abilityResources));
        specialAbilityResource.getSelectionModel().select(leaderCardWidget.getLeaderCard().getSpecialAbility().getTargetResource().toString());
    }

    /**
     * Navigates to the LeaderCards list.
     */
    @FXML
    private void goToEditLeaderCards() {
        GameConfigEditor.goToEditLeaderCards();
    }

    /**
     * Saves the edited LeaderCard to the custom GameConfig.
     */
    @FXML
    private void saveLeaderCard() {
        int victoryPoints = 0;
        Map<Resource, Integer> resourceRequirements = new HashMap<>();
        Map<CardColor, Integer> cardColorRequirements = new HashMap<>();
        Map<CardColor, Integer> cardLevelRequirements = new HashMap<>();
        SpecialAbilityType specialAbilityType;
        Resource targetResource;

        try {
            victoryPoints = Integer.parseInt(this.victoryPoints.getText());
        } catch (NumberFormatException ignored) {
        }

        resourceRequirementsControls.forEach((resource, control) -> {
            int quantity = EditorGUIUtils.getSelectedQuantityForControl(control);
            if (quantity != 0) resourceRequirements.put(resource, quantity);
        });

        cardLevelRequirementsControls.forEach((color, control) -> {
            int quantity = EditorGUIUtils.getSelectedQuantityForControl(control);
            if (quantity != 0) cardLevelRequirements.put(color, quantity);
        });

        cardColorRequirementsControls.forEach((color, control) -> {
            int quantity = EditorGUIUtils.getSelectedQuantityForControl(control);
            if (quantity != 0) cardColorRequirements.put(color, quantity);
        });

        specialAbilityType = SpecialAbilityType.valueOf(this.specialAbilityType.getSelectionModel().getSelectedItem());
        targetResource = Resource.valueOf(this.specialAbilityResource.getSelectionModel().getSelectedItem());

        LeaderCardRequirement requirement = new LeaderCardRequirement(resourceRequirements, cardColorRequirements,
                cardLevelRequirements);
        SpecialAbility specialAbility = new SpecialAbility(specialAbilityType, targetResource);

        LeaderCard modifiedCard = new LeaderCard(victoryPoints, requirement, specialAbility);
        LeaderCardWidget modifiedWidget = new LeaderCardWidget(modifiedCard);

        VBox box = new VBox(modifiedWidget);
        VBox.setMargin(modifiedWidget, new Insets(0, 20, 0, 20));
        box.setAlignment(Pos.CENTER);
        setLeft(box);

        GameConfigEditor.getGameConfig().modifyLeaderCard(leaderCardWidget.getLeaderCard(), modifiedCard);
        GameConfigEditor.setSavable(true);

        this.leaderCardWidget = modifiedWidget;
    }

    /**
     * Deletes the LeaderCard.
     */
    @FXML
    private void deleteLeaderCard() {
        Stage dialog = new Stage();

        dialog.initOwner(getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setHeight(300);
        dialog.setWidth(400);

        Scene scene = new Scene(new DeleteLeaderCard(this));
        dialog.setScene(scene);
        dialog.showAndWait();

        goToEditLeaderCards();
    }

    /**
     * Gets the LeaderCardWidget for the edited card.
     *
     * @return the leader card widget for the edited card
     */
    public LeaderCardWidget getLeaderCardWidget() {
        return leaderCardWidget;
    }

    /**
     * Builds the controls to modify the LeaderCard requirements.
     */
    private void constructRequirementsControls() {
        Map<Resource, Integer> resReq = leaderCardWidget.getLeaderCard().getCardRequirements().getResourceRequirements();
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            BorderPane control = EditorGUIUtils.buildControl(resource.toString(), resReq.getOrDefault(resource, -1));
            resourceRequirements.getChildren().add(control);
            resourceRequirementsControls.put(resource, control);
        });

        Map<CardColor, Integer> colorReq = leaderCardWidget.getLeaderCard().getCardRequirements().getCardColorRequirements();
        Arrays.stream(CardColor.values()).forEach(color -> {
            BorderPane control = EditorGUIUtils.buildControl(color.toString(), colorReq.getOrDefault(color, -1));
            cardColorRequirements.getChildren().add(control);
            cardColorRequirementsControls.put(color, control);
        });

        Map<CardColor, Integer> levelReq = leaderCardWidget.getLeaderCard().getCardRequirements().getCardLevelRequirements();
        Arrays.stream(CardColor.values()).forEach(color -> {
            BorderPane control = EditorGUIUtils.buildControl(color.toString(), levelReq.getOrDefault(color, -1));
            cardLevelRequirements.getChildren().add(control);
            cardLevelRequirementsControls.put(color, control);
        });
    }
}
