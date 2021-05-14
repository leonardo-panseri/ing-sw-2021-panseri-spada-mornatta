package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.editor.FXMLUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class EditLeaderCard extends BorderPane {
    private final LeaderCardWidget leaderCardWidget;

    public EditLeaderCard(LeaderCardWidget leaderCardWidget) {
        this.leaderCardWidget = leaderCardWidget;
        FXMLUtils.loadFXML(this);
        VBox box = new VBox(leaderCardWidget);
        VBox.setMargin(leaderCardWidget, new Insets(0, 20, 0, 20));
        box.setAlignment(Pos.CENTER);
        setLeft(box);
    }


}
