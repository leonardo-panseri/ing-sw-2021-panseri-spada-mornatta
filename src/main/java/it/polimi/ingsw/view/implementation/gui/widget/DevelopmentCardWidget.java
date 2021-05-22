package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.editor.GUIUtils;
import it.polimi.ingsw.model.card.DevelopmentCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.Objects;

public class DevelopmentCardWidget extends AnchorPane {
    @FXML
    public FlowPane cost;
    @FXML
    public VBox input;
    @FXML
    public VBox output;
    @FXML
    public Label victoryPoints;

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
            cost.getChildren().add(GUIUtils.buildResourceDisplay("resources/" + resource.toString(), quantity)));

        developmentCard.getProductionInput().forEach((resource, quantity) ->
                input.getChildren().add(GUIUtils.buildResourceDisplay("resources/" + resource.toString(), quantity)));
        developmentCard.getProductionOutput().forEach((resource, quantity) ->
                output.getChildren().add(GUIUtils.buildResourceDisplay("resources/" + resource.toString(), quantity)));

        victoryPoints.textProperty().set(Integer.toString(developmentCard.getVictoryPoints()));

        Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/images/devCards/" + developmentCard.getColor().toString().toLowerCase() + developmentCard.getLevel() + ".png")),
                195.0, 294.0, true, true);
        Background background = new Background(new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        setBackground(background);
    }
}
