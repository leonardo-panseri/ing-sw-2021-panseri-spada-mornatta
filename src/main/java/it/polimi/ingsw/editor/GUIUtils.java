package it.polimi.ingsw.editor;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class GUIUtils {
    public static BorderPane buildControl(String name, int quantity) {
        CheckBox checkBox = new CheckBox(name);
        TextField input = new TextField();
        input.setTextFormatter(getNumberInputTextFormatter());
        input.setPrefWidth(30);

        checkBox.setOnAction(actionEvent -> {
            input.setEditable(checkBox.isSelected());
            if(!checkBox.isSelected()) input.setText("" + 0);
        });

        if(quantity != -1) {
            checkBox.setSelected(true);
            input.setText("" + quantity);
        } else {
            input.setEditable(false);
            input.setText("" + 0);
        }

        BorderPane box = new BorderPane();
        box.setPrefWidth(70);
        box.setLeft(checkBox);
        box.setRight(input);
        return box;
    }

    public static int getSelectedQuantityForControl(BorderPane control) {
        int quantity = 0;
        if (((CheckBox)control.getLeft()).isSelected()) {
            TextField textField = (TextField) control.getRight();
            try {
                quantity = Integer.parseInt(textField.getText());
            } catch (NumberFormatException ignored) {}
        }
        return quantity;
    }

    public static TextFormatter<String> getNumberInputTextFormatter() {
        return new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        });
    }
}
