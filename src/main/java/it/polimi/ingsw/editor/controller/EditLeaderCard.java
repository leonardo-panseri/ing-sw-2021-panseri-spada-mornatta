package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.FXMLUtils;
import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class EditLeaderCard extends BorderPane {
    @FXML
    public VBox resourceRequirements;
    @FXML
    public VBox cardColorRequirements;
    @FXML
    public VBox cardLevelRequirements;
    @FXML
    public TextField victoryPoints;
    @FXML
    public ChoiceBox<String> specialAbilityType;
    @FXML
    public ChoiceBox<String> specialAbilityResource;


    private LeaderCardWidget leaderCardWidget;
    private final Map<Resource, BorderPane> resourceRequirementsControls;
    private final Map<CardColor, BorderPane> cardColorRequirementsControls;
    private final Map<CardColor, BorderPane> cardLevelRequirementsControls;

    public EditLeaderCard(LeaderCardWidget leaderCardWidget) {
        this.leaderCardWidget = leaderCardWidget;
        this.resourceRequirementsControls = new HashMap<>();
        this.cardColorRequirementsControls = new HashMap<>();
        this.cardLevelRequirementsControls = new HashMap<>();

        FXMLUtils.loadFXML(this);
    }

    @FXML
    private void initialize() {
        VBox box = new VBox(leaderCardWidget);
        VBox.setMargin(leaderCardWidget, new Insets(0, 20, 0, 20));
        box.setAlignment(Pos.CENTER);
        setLeft(box);

        constructRequirementsControls();

        victoryPoints.setTextFormatter(GameConfigEditor.getNumberInputTextFormatter());
        victoryPoints.setText("" + leaderCardWidget.getLeaderCard().getVictoryPoints());

        List<String> abilityTypes = new ArrayList<>();
        Arrays.stream(SpecialAbilityType.values()).forEach(type -> {
            abilityTypes.add(type.toString());
        });
        specialAbilityType.setItems(FXCollections.observableList(abilityTypes));
        specialAbilityType.getSelectionModel().select(leaderCardWidget.getLeaderCard().getSpecialAbility().getType().toString());

        List<String> abilityResources = new ArrayList<>();
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            abilityResources.add(resource.toString());
        });
        specialAbilityResource.setItems(FXCollections.observableList(abilityResources));
        specialAbilityResource.getSelectionModel().select(leaderCardWidget.getLeaderCard().getSpecialAbility().getTargetResource().toString());
    }

    @FXML
    public void goToEditLeaderCards() {
        GameConfigEditor.goToEditLeaderCards();
    }

    @FXML
    public void saveLeaderCard() {
        int victoryPoints = 0;
        Map<Resource, Integer> resourceRequirements = new HashMap<>();
        Map<CardColor, Integer> cardColorRequirements = new HashMap<>();
        Map<CardColor, Integer> cardLevelRequirements = new HashMap<>();
        SpecialAbilityType specialAbilityType;
        Resource targetResource;

        try {
            victoryPoints = Integer.parseInt(this.victoryPoints.getText());
        }catch (NumberFormatException ignored) {}

        resourceRequirementsControls.forEach((resource, control) -> {
            int quantity = saveQuantity(control);
            if (quantity!=0) resourceRequirements.put(resource, quantity);
        });

        cardLevelRequirementsControls.forEach((color, control) -> {
            int quantity = saveQuantity(control);
            if (quantity!=0) cardLevelRequirements.put(color, quantity);
        });

        cardColorRequirementsControls.forEach((color, control) -> {
            int quantity = saveQuantity(control);
            if (quantity!=0) cardColorRequirements.put(color, quantity);
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
        GameConfigEditor.setSavable();

        this.leaderCardWidget = modifiedWidget;
    }

    private void constructRequirementsControls() {
        Map<Resource, Integer> resReq = leaderCardWidget.getLeaderCard().getCardRequirements().getResourceRequirements();
        Arrays.stream(Resource.values()).filter(resource -> resource != Resource.FAITH).forEach(resource -> {
            BorderPane control = buildRequirementControl(resource.toString(), resReq.getOrDefault(resource, -1));
            resourceRequirements.getChildren().add(control);
            resourceRequirementsControls.put(resource, control);
        });

        Map<CardColor, Integer> colorReq = leaderCardWidget.getLeaderCard().getCardRequirements().getCardColorRequirements();
        Arrays.stream(CardColor.values()).forEach(color -> {
            BorderPane control = buildRequirementControl(color.toString(), colorReq.getOrDefault(color, -1));
            cardColorRequirements.getChildren().add(control);
            cardColorRequirementsControls.put(color, control);
        });

        Map<CardColor, Integer> levelReq = leaderCardWidget.getLeaderCard().getCardRequirements().getCardLevelRequirements();
        Arrays.stream(CardColor.values()).forEach(color -> {
            BorderPane control = buildRequirementControl(color.toString(), levelReq.getOrDefault(color, -1));
            cardLevelRequirements.getChildren().add(control);
            cardLevelRequirementsControls.put(color, control);
        });
    }

    private BorderPane buildRequirementControl(String name, int quantity) {
        CheckBox checkBox = new CheckBox(name);
        TextField input = new TextField();
        input.setTextFormatter(GameConfigEditor.getNumberInputTextFormatter());
        input.setPrefWidth(25);

        checkBox.setOnAction(actionEvent -> {
            input.setEditable(checkBox.isSelected());
            if(!checkBox.isSelected()) input.setText("" + 0);
        });

        if(quantity != -1) {
            checkBox.setSelected(true);
            input.setText("" + quantity);
        } else {
            input.setEditable(false);
            input.setText("" + 0);
        }

        BorderPane box = new BorderPane();
        box.setPrefWidth(70);
        box.setLeft(checkBox);
        box.setRight(input);
        return box;
    }

    private int saveQuantity(BorderPane control) {
        int quantity = 0;
        if (((CheckBox)control.getLeft()).isSelected()) {
            TextField textField = (TextField) control.getRight();
            try {
                quantity = Integer.parseInt(textField.getText());
            } catch (NumberFormatException ignored) {
            }
        }
        return quantity;
    }

}
