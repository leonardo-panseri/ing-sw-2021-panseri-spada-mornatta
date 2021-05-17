package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.FXMLUtils;
import it.polimi.ingsw.editor.GUIUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import it.polimi.ingsw.model.Game;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FaithTrackWidget extends GridPane {

    private final Map<Integer, List<Integer>> popeReports;

    private final Map<Integer, Integer> faithTrackPoints;

    public FaithTrackWidget(Map<Integer, List<Integer>> popeReports, Map<Integer, Integer> faithTrackPoints) {
        this.faithTrackPoints = faithTrackPoints;
        this.popeReports = popeReports;
        FXMLUtils.loadFXML(this);
    }

    public Map<Integer, List<Integer>> getPopeReports() {
        return popeReports;
    }

    public Map<Integer, Integer> getFaithTrackPoints() {
        return faithTrackPoints;
    }

    @FXML
    private void initialize() {
        Image img = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/images/others/faithBox.jpg")), 25, 25, false, false);
        Background background = new Background(new BackgroundImage
                (img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));


        for (int i = 0; i < 25; i++) {
            HBox box = new HBox();
            box.setPrefWidth(25);
            box.setPrefHeight(25);
            box.setAlignment(Pos.CENTER);
            box.setBackground(background);
            if (i != 0) {
                Label label = new Label("" + i);
                box.getChildren().add(label);
            }
            addColumn(i, box);
        }
    }

}
