package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class GameConfigSelection {
    @FXML
    public TextField config;
    @FXML
    public Button choose;

    private File selectedFile;

    @FXML
    private void initialize() {
        choose.setDisable(true);
        choose.setOnAction(actionEvent -> GUI.instance().getActionSender().setGameConfig(selectedFile));

        config.setOnMouseClicked(mouseEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Custom Config");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Config files", "*.json"));
            selectedFile = fileChooser.showOpenDialog(GUI.instance().getScene().getWindow());
            if(selectedFile != null) choose.setDisable(false);
        });
    }

    @FXML
    public void skip() {
        GUI.instance().getActionSender().setGameConfig(null);
    }
}
