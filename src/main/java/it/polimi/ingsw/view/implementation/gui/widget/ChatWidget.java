package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.view.beans.MockModel;
import it.polimi.ingsw.view.beans.MockPlayer;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.application.Platform;
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
    @FXML
    private VBox chat;
    private Button button;
    private TextField textInput;
    private ScrollPane scrollPane;


    public ChatWidget() {

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {

        setMaxWidth(150);

        scrollPane = new ScrollPane();
        button = new Button();
        chat.setSpacing(10);
        chat.setPrefWidth(150);
        getChildren().add(scrollPane);
        textInput = new TextField();
        HBox hbox = new HBox();
        hbox.getChildren().add(button);
        button.getStyleClass().add("chat-button");
        hbox.getChildren().add(textInput);
        getChildren().add(hbox);


        button.setOnAction(e -> {
            if (!textInput.getText().isBlank()) {
                button.setDisable(true);
                GUI.instance().getActionSender().sendChatMessage(textInput.getText());
                textInput.clear();
                button.setDisable(false);
            }
        });

        scrollPane.setContent(chat);
        scrollPane.setMaxHeight(150);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


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
        Platform.runLater(() -> {
            Label label = new Label(message);
            label.setWrapText(true);
            label.setMaxWidth(150);
            HBox content = new HBox(label);
            chat.getChildren().add(content);
        });
    }

}
