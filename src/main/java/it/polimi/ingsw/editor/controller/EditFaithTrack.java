package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class EditFaithTrack {
    @FXML
    public AnchorPane pane;

    @FXML
    public HBox box;

    @FXML
    private void initialize() {
        FaithTrackWidget faithTrackWidget = new FaithTrackWidget(GameConfigEditor.getGameConfig().getPopeReports(),
                GameConfigEditor.getGameConfig().getFaithTrackPoints());

        box.getChildren().add(faithTrackWidget);
    }
}

