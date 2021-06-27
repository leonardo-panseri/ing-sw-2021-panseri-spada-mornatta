package it.polimi.ingsw.editor;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;

/**
 * Utils class for the editor.
 */
public class EditorGUIUtils {
    /**
     * Builds a control with a CheckBox and a TextField.
     *
     * @param name the label for the checkbox
     * @param quantity the initial quantity of the text field
     * @return a border pane containing the control
     */
    public static BorderPane buildControl(String name, int quantity) {
        CheckBox checkBox = new CheckBox(name);
        TextField input = new TextField();
        input.setTextFormatter(getNumberInputTextFormatter());
        input.setPrefWidth(30);

        checkBox.setOnAction(actionEvent -> {
            input.setEditable(checkBox.isSelected());
            if (!checkBox.isSelected()) input.setText("" + 0);
        });

        if (quantity != -1) {
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

    /**
     * Gets the selected quantity from the given control built with {@link #buildControl(String, int)}.
     *
     * @param control the control to check
     * @return the selected quantity in the given control
     */
    public static int getSelectedQuantityForControl(BorderPane control) {
        int quantity = 0;
        if (((CheckBox) control.getLeft()).isSelected()) {
            TextField textField = (TextField) control.getRight();
            try {
                quantity = Integer.parseInt(textField.getText());
            } catch (NumberFormatException ignored) {
            }
        }
        return quantity;
    }

    /**
     * Gets a TextFormatter that accepts only numbers.
     *
     * @return the text formatter that accepts only numbers
     */
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
