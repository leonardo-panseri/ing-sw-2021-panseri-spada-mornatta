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

/**
 * Home page.
 */
public class Home {
    @FXML
    private Button saveButton;

    /**
     * Navigates to the LeaderCards list.
     */
    @FXML
    private void goToEditLeaderCards() {
        GameConfigEditor.goToEditLeaderCards();
    }

    /**
     * Navigates to the DevelopmentCards list.
     */
    @FXML
    private void goToEditDevelopmentCards() {
        GameConfigEditor.goToEditDevelopmentCards();
    }

    /**
     * Navigates to the base production edit page.
     */
    @FXML
    private void goToEditBaseProduction() {
        GameConfigEditor.goToEditBaseProduction();
    }

    /**
     * Navigates to the faith track edit page.
     */
    @FXML
    private void goToEditFaithTrack() {
        GameConfigEditor.goToEditFaithTrack();
    }

    /**
     * Saves the custom GameConfig to a file that can be used to start a custom game.
     */
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
