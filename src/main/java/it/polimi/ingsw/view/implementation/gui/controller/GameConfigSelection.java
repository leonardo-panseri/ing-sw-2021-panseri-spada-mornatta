package it.polimi.ingsw.view.implementation.gui.controller;

import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class GameConfigSelection {
    @FXML
    private TextField config;
    @FXML
    private Button choose;

    private File selectedFile;

    @FXML
    private void initialize() {
        choose.setDisable(true);
        choose.setOnAction(actionEvent -> GUI.instance().getActionSender().setGameConfig(selectedFile));

        config.setEditable(false);
        config.setOpacity(0.6);
        config.setText("Click here to choose the file");
        config.setOnMouseClicked(mouseEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Custom Config");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Config files", "*.json"));
            selectedFile = fileChooser.showOpenDialog(GUI.instance().getScene().getWindow());
            if (selectedFile != null) {
                choose.setDisable(false);
                config.setText(selectedFile.getName());
            }
        });
    }

    @FXML
    private void skip() {
        GUI.instance().getActionSender().setGameConfig(null);
    }
}
