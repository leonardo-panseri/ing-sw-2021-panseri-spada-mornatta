package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.FXMLUtils;
import it.polimi.ingsw.model.BaseProductionPower;
import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class BaseProductionWidget extends AnchorPane {
    @FXML
    public VBox inputBox;
    @FXML
    public VBox outputBox;

    private final BaseProductionPower baseProduction;

    public BaseProductionWidget(BaseProductionPower baseProduction) {
        this.baseProduction = baseProduction;

        FXMLUtils.loadFXML(this);
    }

    public BaseProductionPower getBaseProduction() {
        return baseProduction;
    }

    @FXML
    private void initialize() {
        baseProduction.getInputMap().forEach((resource, quantity) -> {
            inputBox.getChildren().add(buildResourceDisplay(resource, quantity));
        });

        baseProduction.getOutputMap().forEach((resource, quantity) -> {
            outputBox.getChildren().add(buildResourceDisplay(resource, quantity));
        });
    }

    private HBox buildResourceDisplay(Resource resource, int quantity) {
        Label quantityLabel = new Label("" + quantity);
        String imgPath = resource == null ? "/images/others/any.png" : "/images/resources/" + resource.toString().toLowerCase() + ".png";
        ImageView resourceImg = new ImageView(new Image(
                getClass().getResourceAsStream(imgPath),
                79, 81, true, true));
        HBox box = new HBox(quantityLabel, resourceImg);
        box.setPrefWidth(106);
        box.setPrefHeight(82);
        return box;
    }
}