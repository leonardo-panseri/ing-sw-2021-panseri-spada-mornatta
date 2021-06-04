package it.polimi.ingsw.view.implementation.gui.widget;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.view.implementation.gui.GUI;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatWidget extends VBox {
    @FXML
    private VBox chat;
    private TextField textInput;


    public ChatWidget() {

        FXMLUtils.loadWidgetFXML(this);
    }

    @FXML
    private void initialize() {

        setMaxWidth(150);
        ScrollPane scrollPane = new ScrollPane();
        chat.setSpacing(10);
        chat.setPrefWidth(150);
        getChildren().add(scrollPane);
        textInput = new TextField();
        HBox hbox = new HBox();
        hbox.getChildren().add(textInput);
        getChildren().add(hbox);
        textInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                GUI.instance().getActionSender().sendChatMessage(textInput.getText());
                textInput.clear();
            }
        });
        hbox.getStyleClass().add("chat-box");
        scrollPane.setContent(chat);
        scrollPane.setPrefHeight(150);
        scrollPane.setMaxHeight(150);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("chat-scroll-pane");
        hbox.getStyleClass().add("chat-box");
        textInput.getStyleClass().add("chat-input");

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
            label.getStyleClass().add("chat-label");
            HBox content = new HBox(label);
            content.getStyleClass().add("chat-box");
            chat.getChildren().add(content);
        });
    }

}
