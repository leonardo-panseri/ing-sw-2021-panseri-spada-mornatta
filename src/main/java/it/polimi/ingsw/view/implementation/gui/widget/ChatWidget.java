package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.view.beans.MockModel;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatWidget extends ScrollPane {

    private VBox chat;

    public ChatWidget() {

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {

        setFitToHeight(true);
        setPrefSize(150, 150);
        chat = new VBox();
        chat.setSpacing(10);
        chat.setPrefWidth(150);
        setContent(chat);

        for(String s : GUI.instance().getModel().getChatMessages()){
            showMessage(s);
        }
        GUI.instance().getModel().getChatMessages().addListener((ListChangeListener.Change<? extends String> c) -> {
            while (c.next()) {
                if(c.wasAdded()){
                    for(String s : c.getAddedSubList()){
                        showMessage(s);
                    }
                }
            }
        });
    }

    public void showMessage(String message) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(150);
        HBox content = new HBox(label);
        chat.getChildren().add(content);
    }
}
