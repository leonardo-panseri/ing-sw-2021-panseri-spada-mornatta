package it.polimi.ingsw.view.implementation.gui;

import it.polimi.ingsw.view.Renderer;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class GUIRenderer extends Renderer {
    protected GUIRenderer(View view) {
        super(view);
    }

    @Override
    public void showErrorMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);

            alert.showAndWait();
        });
    }
}
