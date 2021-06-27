package it.polimi.ingsw.editor.controller;

import it.polimi.ingsw.FXMLUtils;
import it.polimi.ingsw.editor.GameConfigEditor;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * Widget to confirm deletion of a LeaderCard.
 */
public class DeleteLeaderCard extends VBox {
    private final EditLeaderCard cardEditor;

    /**
     * Constructs a new DeleteLeaderCard widget.
     *
     * @param cardEditor the widget for editing the leader card
     */
    public DeleteLeaderCard(EditLeaderCard cardEditor) {
        this.cardEditor = cardEditor;
        FXMLUtils.loadEditorFXML(this);
    }

    /**
     * Deletes the LeaderCard.
     */
    @FXML
    private void accept() {
        GameConfigEditor.getGameConfig().deleteLeaderCard(cardEditor.getLeaderCardWidget().getLeaderCard());
        getScene().getWindow().hide();
    }

    /**
     * Cancel the deletion.
     */
    @FXML
    private void refuse() {
        getScene().getWindow().hide();
    }
}
