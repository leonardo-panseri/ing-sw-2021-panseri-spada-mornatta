module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens it.polimi.ingsw to javafx.fxml;
    opens it.polimi.ingsw.editor.controller to javafx.fxml;
    opens it.polimi.ingsw.view.implementation.gui.controller to javafx.fxml;
    opens it.polimi.ingsw.server to com.google.gson;
    opens it.polimi.ingsw.model to com.google.gson;
    opens it.polimi.ingsw.model.card to com.google.gson;
    exports it.polimi.ingsw;
    opens it.polimi.ingsw.view.implementation.gui.widget to javafx.fxml;
}