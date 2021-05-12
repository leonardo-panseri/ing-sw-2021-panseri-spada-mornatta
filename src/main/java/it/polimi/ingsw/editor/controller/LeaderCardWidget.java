package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.FXMLUtils;
import it.polimi.ingsw.model.card.LeaderCard;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LeaderCardWidget extends AnchorPane {
    @FXML
    public FlowPane lcRequirements;
    @FXML
    public Label lcVictoryPoints;
    @FXML
    public AnchorPane lcSpecialAbility;

    private final LeaderCard leaderCard;

    public LeaderCardWidget(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;

        FXMLUtils.loadFXML(this);
    }

    @FXML
    private void initialize() {
        leaderCard.getCardRequirements().getResourceRequirements().forEach(
                (resource, quantity) -> lcRequirements.getChildren().add(buildRequirement(resource.toString(), quantity)));
        lcVictoryPoints.textProperty().set(Integer.toString(leaderCard.getVictoryPoints()));
    }

    private HBox buildRequirement(String imageName, int quantity) {
        HBox box = new HBox();
        box.getStyleClass().add("hbox");
        box.getChildren().add(new Label("" + quantity));

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/images/" + imageName + ".png")));
        image.setFitHeight(36.0);
        image.setFitWidth(23.0);
        image.setPreserveRatio(true);
        imageBox.getChildren().add(image);
        box.getChildren().add(imageBox);
        return box;
    }
}
