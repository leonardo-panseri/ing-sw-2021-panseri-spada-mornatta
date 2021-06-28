package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Map;

/**
 * Widget that shows the rank when a game ends.
 */

public class EndGameWidget extends VBox {
    @FXML
    private FlowPane scoresBox;

    private final Label winnerLabel;
    private Label loseReasonLabel;
    private Label scoreLabel;
    private GridPane scoreList;

    /**
     * Creates a new EndGameWidget for multiplayer games.
     * @param scores a map containing the score for each player
     * @param winnerName nick of the winner
     */
    public EndGameWidget(Map<String, Integer> scores, String winnerName) {
        winnerLabel = new Label(winnerName + " is the true Master of Renaissance!");
        scoreList = new GridPane();
        int i = 0;
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            scoreList.addRow(i, new Label(entry.getKey()), new Label(entry.getValue().toString()));
            i++;
        }
        scoreList.setPrefWidth(200);
        scoreList.getColumnConstraints().add(new ColumnConstraints(150));
        scoreList.getColumnConstraints().add(new ColumnConstraints(50));

        FXMLUtils.loadWidgetFXML(this);
    }

    /**
     * Creates a new EndGameWidget for single player games.
     * @param lorenzoWin true if lorenzo won
     * @param loseReason description of the reason why the player won
     * @param playerScore the score of the player, displayed only if the player won
     */
    public EndGameWidget(boolean lorenzoWin, String loseReason, int playerScore) {
        if (lorenzoWin) {
            winnerLabel = new Label("Lorenzo is still the true Master of Renaissance!");
            loseReasonLabel = new Label(loseReason);
        } else {
            winnerLabel = new Label("You are the true Master of Renaissance!");
            scoreLabel = new Label("Your score: " + playerScore + " victory points");
        }

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {
        FlowPane.setMargin(winnerLabel, new Insets(0, 0, 20, 0));
        scoresBox.getChildren().add(winnerLabel);
        if (scoreList != null) {
            HBox centeredList = new HBox(scoreList);
            centeredList.setAlignment(Pos.CENTER);
            scoresBox.getChildren().add(centeredList);
        } else {
            if (loseReasonLabel != null) {
                HBox centeredReason = new HBox(loseReasonLabel);
                centeredReason.setAlignment(Pos.CENTER);
                scoresBox.getChildren().add(centeredReason);
            } else {
                scoresBox.getChildren().add(scoreLabel);
            }
        }

        Button button = new Button("Back To Home");
        button.setOnAction(actionEvent -> GUI.instance().resetAndGoHome());
        HBox box = new HBox(button);
        FlowPane.setMargin(box, new Insets(50, 0, 0, 0));
        box.setAlignment(Pos.CENTER);
        scoresBox.getChildren().add(box);
    }
}
