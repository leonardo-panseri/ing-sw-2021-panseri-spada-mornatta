package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.view.beans.MockPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FaithTrackWidget extends GridPane {

    private final Map<Integer, List<Integer>> popeReports;

    private final Map<Integer, Integer> faithTrackPoints;

    private final MockPlayer player;

    private Background backgroundBasicTile;
    private Background backgroundCurrentTile;
    private Background backgroundGoldenPopeTile;
    private Background backgroundGoldenTile;
    private Background backgroundPopeFavour;
    private Background backgroundPopeTile;

    public FaithTrackWidget(Map<Integer, List<Integer>> popeReports, Map<Integer, Integer> faithTrackPoints, MockPlayer player) {
        this.faithTrackPoints = faithTrackPoints;
        this.popeReports = popeReports;
        this.player = player;
        this.backgroundBasicTile = null;
        this.backgroundGoldenPopeTile = null;
        this.backgroundCurrentTile = null;
        this.backgroundGoldenTile = null;
        this.backgroundPopeFavour = null;
        this.backgroundPopeTile = null;
        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {

        Image basicTile = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/images/faithTrack/basicTile.png")), 25, 25, false, false);
        backgroundBasicTile = new Background(new BackgroundImage
                (basicTile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

        Image currentTile = new Image(Objects.requireNonNull(getClass().
                getResourceAsStream("/images/faithTrack/currentTile.png")), 25, 25, false, false);
        backgroundCurrentTile = new Background(
                new BackgroundImage
                        (currentTile,
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

        Image goldenPopeTile = new Image(Objects.requireNonNull(getClass().
                getResourceAsStream("/images/faithTrack/goldenPopeTile.png")), 25, 38, false, false);
        backgroundGoldenPopeTile = new Background(
                new BackgroundImage(goldenPopeTile, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

        Image goldenTile = new Image(Objects.requireNonNull(getClass().
                getResourceAsStream("/images/faithTrack/goldenTile.png")), 25, 25, false, false);
        backgroundGoldenTile = new Background(
                new BackgroundImage(goldenTile, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

        Image popeFavour = new Image(Objects.requireNonNull(getClass().
                getResourceAsStream("/images/faithTrack/popeFavour.png")), 25, 25, false, false);
        backgroundPopeFavour = new Background(
                new BackgroundImage
                        (popeFavour, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));


        Image popeTile = new Image(Objects.requireNonNull(getClass().
                getResourceAsStream("/images/faithTrack/popeTile.png")), 25, 25, false, false);
        backgroundPopeTile = new Background(
                new BackgroundImage(popeTile, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

        createFaithTrack(0);

        if(player != null) {
            player.faithPointsProperty().addListener((change, oldValue, newValue) -> {
                updateFaithPoints(newValue.intValue());
            });
        }
    }

    private int isInRange(int index) {
        if (index == 0) return -1;
        for (Integer popeReport : popeReports.keySet()) {
            int range = popeReports.get(popeReport).get(1);
            for (int i = popeReport; i > popeReport - range; i--) {
                if (index == i) {
                    if (i == popeReport) return 2;
                    if (i == popeReport - range + 1) return 0;
                    else return 1;
                }
            }
        }
        return -1;
    }

    private void updateFaithPoints(int currentFaithPoints) {
        Platform.runLater(() -> {
            this.getChildren().removeAll(getChildren());
            createFaithTrack(currentFaithPoints);
        });
    }

    private void createFaithTrack(int currentPosition) {
        for (int i = 0; i < 25; i++) {
            VBox box = new VBox();
            box.setPrefWidth(25);
            box.setAlignment(Pos.CENTER);
            String labelText = "";
            String additionalLabelText = "";
            String labelClass = "";
            String boxClass = "";

            if (i == currentPosition) {
                box.setBackground(backgroundCurrentTile);
            } else if (faithTrackPoints.containsKey(i) && popeReports.containsKey(i)) {
                box.setBackground(backgroundGoldenPopeTile);
                labelText = faithTrackPoints.get(i).toString();
                additionalLabelText = popeReports.get(i).get(0).toString();
                labelClass = "special-tile";
            } else if (faithTrackPoints.containsKey(i)) {
                box.setBackground(backgroundGoldenTile);
                labelText = faithTrackPoints.get(i).toString();
                labelClass = "special-tile";
            } else if (popeReports.containsKey(i)) {
                box.setBackground(backgroundPopeTile);
                labelText = popeReports.get(i).get(0).toString();
                labelClass = "special-tile";
            } else {
                box.setBackground(backgroundBasicTile);
                labelText = "" + i;
            }

            int rangeResult = isInRange(i);
            if (rangeResult == 0) {
                boxClass = "last-in-range";
            } else if (rangeResult == 1) {
                boxClass = "in-range";
            } else if (rangeResult == 2) {
                boxClass = "first-in-range";
            }

            if (i != 0) {
                Label label = new Label(labelText);
                if (!labelClass.isBlank())
                    label.getStyleClass().add(labelClass);
                box.getChildren().add(label);

                if (!additionalLabelText.isBlank()) {
                    Label additionalLabel = new Label(labelText);
                    if (!labelClass.isBlank())
                        additionalLabel.getStyleClass().add(labelClass);
                    box.getChildren().add(additionalLabel);
                }
                box.getStyleClass().add(boxClass);
            }
            addColumn(i, box);
        }
    }

}
