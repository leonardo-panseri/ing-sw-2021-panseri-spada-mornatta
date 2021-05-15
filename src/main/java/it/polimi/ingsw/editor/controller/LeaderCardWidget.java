package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.FXMLUtils;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.LeaderCardRequirement;
import it.polimi.ingsw.model.card.SpecialAbility;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaderCardWidget extends AnchorPane {
    @FXML
    public AnchorPane leaderPane;
    @FXML
    public FlowPane lcRequirements;
    @FXML
    public Label lcVictoryPoints;

    private final LeaderCard leaderCard;


    public LeaderCardWidget(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;

        FXMLUtils.loadFXML(this);
    }

    public LeaderCard getLeaderCard() {
        return leaderCard;
    }

    @FXML
    private void initialize() {
        LeaderCardRequirement requirements = leaderCard.getCardRequirements();
        requirements.getResourceRequirements().forEach((resource, quantity) ->
                lcRequirements.getChildren().add(buildRequirement("resources/" + resource.toString(), quantity))); //stone.png
        requirements.getCardColorRequirements().forEach((color, quantity) ->
                lcRequirements.getChildren().add(buildRequirement("leaders/flags/" + color.toString(), quantity))); //blue.png
        requirements.getCardLevelRequirements().forEach((color, level) ->
                lcRequirements.getChildren().add(buildRequirement("leaders/flags/" + color.toString() + level, 1))); //red1.png

        lcVictoryPoints.textProperty().set(Integer.toString(leaderCard.getVictoryPoints()));

        Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/images/leaders/" + leaderCard.getSpecialAbility().getType().toString().toLowerCase() + "Leader.png")),
                195.0, 294.0, true, true); //production.png
        Background background = new Background(new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        leaderPane.setBackground(background);
        List<ImageView> specialAbility = buildSpecialAbility(leaderCard.getSpecialAbility());
        leaderPane.getChildren().addAll(specialAbility);
    }

    private HBox buildRequirement(String imageName, int quantity) {
        HBox box = new HBox();
        box.getStyleClass().add("hbox");
        box.getChildren().add(new Label("" + quantity));

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        ImageView image = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + imageName + ".png"))));
        image.setFitHeight(36.0);
        image.setFitWidth(23.0);
        image.setPreserveRatio(true);
        imageBox.getChildren().add(image);
        box.getChildren().add(imageBox);
        return box;
    }

    private List<ImageView> buildSpecialAbility(SpecialAbility specialAbility) {
        String resource = specialAbility.getTargetResource().toString().toLowerCase();
        InputStream image = getClass().getResourceAsStream("/images/resources/" + resource + ".png");
        List<ImageView> results = new ArrayList<>();
        switch (specialAbility.getType()) {
            case PRODUCTION -> {
                ImageView input = new ImageView(new Image(image,30.0, 41.0, true, true));
                input.setLayoutX(35);
                input.setLayoutY(237);
                results.add(input);
            }
            case DISCOUNT -> {
                ImageView discount = new ImageView(new Image(image,22, 22, true, true));
                discount.setLayoutX(144);
                discount.setLayoutY(227);
                results.add(discount);
            }
            case DEPOT -> {
                Image img = new Image(image, 39, 41, true, true);
                ImageView depot1 = new ImageView(img);
                depot1.setLayoutX(42);
                depot1.setLayoutY(233);
                depot1.setOpacity(0.4);
                results.add(depot1);
                ImageView depot2 = new ImageView(img);
                depot2.setLayoutX(116);
                depot2.setLayoutY(233);
                depot2.setOpacity(0.4);
                results.add(depot2);
            }
            case EXCHANGE -> {
                ImageView exchange = new ImageView(new Image(image, 47, 55, true, true));
                exchange.setLayoutX(116);
                exchange.setLayoutY(227);
                results.add(exchange);
            }
        }
        return results;
    }
}
