package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GameConfigEditor;
import it.polimi.ingsw.server.GameConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Home {
    @FXML
    private Button saveButton;

    @FXML
    private void goToEditLeaderCards() {
        GameConfigEditor.goToEditLeaderCards();
    }

    @FXML
    private void goToEditDevelopmentCards() {
        GameConfigEditor.goToEditDevelopmentCards();
    }

    @FXML
    private void goToEditBaseProduction() {
        GameConfigEditor.goToEditBaseProduction();
    }

    @FXML
    private void goToEditFaithTrack() {
        GameConfigEditor.goToEditFaithTrack();
    }

    @FXML
    private void saveConfig() {
        File config = new File("custom_config_" + System.currentTimeMillis() + ".json");
        try {
            if (!config.createNewFile()) {
                System.out.println("File already existing.");
            } else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(config));
                writer.write(GameConfig.serialize(GameConfigEditor.getGameConfig()));
                writer.close();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Ooops, unable to save custom configuration!");

            alert.showAndWait();
            return;
        }

        GameConfigEditor.setSavable(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Configuration saved to path: " + config.getAbsolutePath());

        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        saveButton.setDisable(true);
    }
}
