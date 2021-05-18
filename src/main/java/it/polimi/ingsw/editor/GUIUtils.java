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

    public static HBox buildResourceDisplay(String imageName, int quantity) {
        HBox box = new HBox();
        box.getStyleClass().add("hbox");
        box.getChildren().add(new Label("" + quantity));

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        ImageView image = new ImageView(new Image(Objects.requireNonNull(GUIUtils.class.getResourceAsStream("/images/" + imageName + ".png"))));
        image.setFitHeight(36.0);
        image.setFitWidth(23.0);
        image.setPreserveRatio(true);
        imageBox.getChildren().add(image);
        box.getChildren().add(imageBox);
        return box;
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
