package it.polimi.ingsw.editor.controller;
import it.polimi.ingsw.editor.FXMLUtils;
import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;


public class DeleteLeaderCard extends VBox {

    EditLeaderCard cardEditor;

    public DeleteLeaderCard(EditLeaderCard cardEditor) {
        this.cardEditor = cardEditor;
        FXMLUtils.loadFXML(this);
    }

    @FXML
    public void accept() {
        GameConfigEditor.getGameConfig().deleteLeaderCard(cardEditor.getLeaderCardWidget().getLeaderCard());
        getScene().getWindow().hide();
    }

    @FXML
    public void refuse() {
        getScene().getWindow().hide();
    }
}
