package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.GUIUtils;
import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class EditFaithTrack {
    @FXML
    public HBox faithTrackDisplay;
    @FXML
    public VBox victoryPoints;
    @FXML
    public VBox popeReports;

    private final Map<Integer, BorderPane> victoryPointsControls;
    private final Map<Integer, BorderPane> popeReportsControls;

    public EditFaithTrack() {
        this.victoryPointsControls = new HashMap<>();
        this.popeReportsControls = new HashMap<>();
    }


    @FXML
    private void initialize() {
        Map<Integer, Integer> victoryPoints = GameConfigEditor.getGameConfig().getFaithTrackPoints();
        Map<Integer, List<Integer>> popeReports = GameConfigEditor.getGameConfig().getPopeReports();

        FaithTrackWidget faithTrackWidget = new FaithTrackWidget(popeReports, victoryPoints);
        faithTrackDisplay.getChildren().add(faithTrackWidget);

        List<BorderPane> vpControls = new ArrayList<>();
        List<BorderPane> prControls = new ArrayList<>();
        for(int i = 1; i < 25; i++) {
            BorderPane controlVP = GUIUtils.buildControl("" + i, victoryPoints.getOrDefault(i, -1));
            victoryPointsControls.put(i, controlVP);
            vpControls.add(controlVP);

            int popeReportVP = -1;
            int popeReportSize = 0;
            if(popeReports.containsKey(i)) {
                popeReportVP = popeReports.get(i).get(0);
                popeReportSize = popeReports.get(i).get(1);
            }

            CheckBox checkBox = new CheckBox("" + i);
            TextField popeReportVPControl = new TextField();
            popeReportVPControl.setTextFormatter(GUIUtils.getNumberInputTextFormatter());
            popeReportVPControl.setPrefWidth(30);
            TextField popeReportSizeControl = new TextField();
            popeReportSizeControl.setTextFormatter(GUIUtils.getNumberInputTextFormatter());
            popeReportSizeControl.setPrefWidth(30);

            checkBox.setOnAction(actionEvent -> {
                popeReportVPControl.setEditable(checkBox.isSelected());
                popeReportSizeControl.setEditable(checkBox.isSelected());
                if(!checkBox.isSelected()) {
                    popeReportVPControl.setText("" + 0);
                    popeReportSizeControl.setText("" + 0);
                }
            });

            if(popeReportVP != -1) {
                checkBox.setSelected(true);
                popeReportVPControl.setText("" + popeReportVP);
                popeReportSizeControl.setText("" + popeReportSize);
            } else {
                popeReportVPControl.setEditable(false);
                popeReportVPControl.setText("" + 0);
                popeReportSizeControl.setEditable(false);
                popeReportSizeControl.setText("" + 0);
            }

            HBox textBox = new HBox(popeReportVPControl, popeReportSizeControl);

            BorderPane box = new BorderPane();
            box.setPrefWidth(90);
            box.setLeft(checkBox);
            box.setRight(textBox);
            popeReportsControls.put(i, box);
            prControls.add(box);
        }

        this.victoryPoints.getChildren().add(new ListView<>(FXCollections.observableList(vpControls)));
        this.popeReports.getChildren().add(new ListView<>(FXCollections.observableList(prControls)));
    }

    @FXML
    public void goToHome() {
        GameConfigEditor.goToHome();
    }

    @FXML
    public void saveFaithTrack() {
        Map<Integer, Integer> newVictoryPoints = new HashMap<>();
        Map<Integer, List<Integer>> newPopeReports = new HashMap<>();

        victoryPointsControls.forEach((position, control) -> {
            int victoryPoints = GUIUtils.getSelectedQuantityForControl(control);
            if(victoryPoints != 0) newVictoryPoints.put(position, victoryPoints);
        });

        popeReportsControls.forEach((position, control) -> {
            CheckBox checkBox = (CheckBox) control.getLeft();
            if(checkBox.isSelected()) {
                HBox box = (HBox) control.getRight();
                TextField vpInput = (TextField) box.getChildren().get(0);
                TextField sizeInput = (TextField) box.getChildren().get(1);
                int vp = 0, size = 0;
                try {
                    vp = Integer.parseInt(vpInput.getText());
                    size = Integer.parseInt(sizeInput.getText());
                } catch (NumberFormatException ignored) {}
                newPopeReports.put(position, Arrays.asList(vp, size));
            }
        });

        FaithTrackWidget faithTrackWidget = new FaithTrackWidget(newPopeReports, newVictoryPoints);
        faithTrackDisplay.getChildren().setAll(faithTrackWidget);

        GameConfigEditor.getGameConfig().modifyFaithTrack(newPopeReports, newVictoryPoints);
        GameConfigEditor.setSavable(true);
    }
}

