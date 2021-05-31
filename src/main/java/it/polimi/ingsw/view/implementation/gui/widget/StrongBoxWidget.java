package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUIUtils;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.util.Map;

public class StrongBoxWidget extends FlowPane {

    private Map <Resource,Integer> strongBox;
    private final MockPlayer player;

    public StrongBoxWidget(MockPlayer player) {
        this.player = player;
        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize(){
       player.getDeposit().strongBoxProperty().addListener((MapChangeListener<? super Resource, ? super Integer>) change -> {
           if(change.wasAdded() && !change.wasRemoved()) {

           } else if (change.wasRemoved() && change.wasAdded()){

           } else{

           }
       });

    }

    public void updateStrongBox(){

        Platform.runLater(() -> {

        });

    }

    private ImageView buildEmptyImage() {
        ImageView img = new ImageView();
        img.setFitHeight(39);
        img.setFitHeight(33);
        return img;
    }
}
