package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.Objects;

public class DevelopmentCardWidget extends AnchorPane {
    @FXML
    private FlowPane cost;
    @FXML
    private VBox input;
    @FXML
    private VBox output;
    @FXML
    private Label victoryPoints;

    private final DevelopmentCard developmentCard;

    public DevelopmentCardWidget(DevelopmentCard developmentCard) {
        this.developmentCard = developmentCard;

        FXMLUtils.loadWidgetFXML(this);
    }

    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

    @FXML
    private void initialize() {
        developmentCard.getCost().forEach((resource, quantity) ->
            cost.getChildren().add(GUIUtils.buildResourceDisplay("resources/" + resource.toString().toLowerCase(), quantity)));

        developmentCard.getProductionInput().forEach((resource, quantity) ->
                input.getChildren().add(GUIUtils.buildResourceDisplay("resources/" + resource.toString().toLowerCase(), quantity)));
        developmentCard.getProductionOutput().forEach((resource, quantity) ->
                output.getChildren().add(GUIUtils.buildResourceDisplay("resources/" + resource.toString().toLowerCase(), quantity)));

        victoryPoints.textProperty().set(Integer.toString(developmentCard.getVictoryPoints()));

        Image bgImage = new Image(Objects.requireNonNull(DevelopmentCardWidget.class.getResourceAsStream(
                "/images/devCards/" + developmentCard.getColor().toString().toLowerCase() + developmentCard.getLevel() + ".png")),
                195.0, 294.0, true, true);
        Background background = new Background(new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        setBackground(background);
    }
}
