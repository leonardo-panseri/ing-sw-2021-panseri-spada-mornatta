package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.view.beans.MockModel;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ChatWidget extends VBox {

    private VBox chat;
    private Button button;
    private TextField textInput;


    public ChatWidget() {

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {

        ScrollPane scrollPane = new ScrollPane();
        chat = new VBox();
        chat.setSpacing(10);
        chat.setPrefWidth(150);
        getChildren().add(scrollPane);
        scrollPane.setContent(chat);
        button = new Button("Send");
        textInput = new TextField();
        getChildren().add(textInput);
        getChildren().add(button);

        button.setOnAction(e -> {
            button.setDisable(true);
            GUI.instance().getActionSender().sendChatMessage(textInput.getText());
            textInput.clear();
            button.setDisable(false);
        });


        for (String s : GUI.instance().getModel().getChatMessages()) {
            showMessage(s);
        }
        GUI.instance().getModel().getChatMessages().addListener((ListChangeListener.Change<? extends String> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (String s : c.getAddedSubList()) {
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
    /*
        TextField field = new TextField();
        Button button = new Button();
        button.setOnAction(e -> {

        });
        chatDisplay.getChildren().addAll(button,field);
     */
}
