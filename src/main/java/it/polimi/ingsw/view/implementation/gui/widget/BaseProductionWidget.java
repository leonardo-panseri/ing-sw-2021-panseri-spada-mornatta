package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.BaseProductionPower;
import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class BaseProductionWidget extends AnchorPane {
    @FXML
    private VBox inputBox;
    @FXML
    private VBox outputBox;

    private final BaseProductionPower baseProduction;

    public BaseProductionWidget(BaseProductionPower baseProduction) {
        this.baseProduction = baseProduction;

        FXMLUtils.loadWidgetFXML(this);
    }

    public BaseProductionPower getBaseProduction() {
        return baseProduction;
    }

    @FXML
    private void initialize() {
        baseProduction.getInputMap().forEach((resource, quantity) -> inputBox.getChildren().add(buildResourceDisplay(resource, quantity)));

        baseProduction.getOutputMap().forEach((resource, quantity) -> outputBox.getChildren().add(buildResourceDisplay(resource, quantity)));
    }

    private HBox buildResourceDisplay(Resource resource, int quantity) {
        Label quantityLabel = new Label("" + quantity);
        String imgPath = resource == null ? "/images/others/any.png" : "/images/resources/" + resource.toString().toLowerCase() + ".png";
        ImageView resourceImg = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(imgPath)),
                79, 81, true, true));
        HBox box = new HBox(quantityLabel, resourceImg);
        box.setPrefWidth(106);
        box.setPrefHeight(82);
        return box;
    }
}
