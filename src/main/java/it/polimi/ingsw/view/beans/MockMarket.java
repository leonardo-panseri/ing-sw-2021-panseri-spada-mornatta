package it.polimi.ingsw.view.beans;

import it.polimi.ingsw.model.Resource;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Local copy of the game market.
 */
public class MockMarket {
    private final ObservableList<List<Resource>> grid;
    private final ObjectProperty<Resource> slideResource;

    public MockMarket() {
        this.grid = FXCollections.observableArrayList();
        this.slideResource = new SimpleObjectProperty<>();
    }

    /**
     * Gets the grid of the market.
     *
     * @return a list of lists representing the market
     */
    public List<List<Resource>> getGrid() {
        return grid;
    }

    public ObservableList<List<Resource>> gridProperty() {
        return grid;
    }

    /**
     * Sets the grid of the market.
     *
     * @param grid list of lists representing the market
     */
    public void setGrid(List<List<Resource>> grid) {
        this.grid.setAll(grid);
    }

    /**
     * Gets the Resource that is in the slide of this market.
     *
     * @return the resource in the slide
     */
    public Resource getSlideResource() {
        return slideResource.get();
    }

    public ObjectProperty<Resource> slideResourceProperty() {
        return slideResource;
    }

    /**
     * Sets the Resource in the slide of this market.
     *
     * @param slideResource the resource that will be set as the slide resource
     */
    public void setSlideResource(Resource slideResource) {
        this.slideResource.setValue(slideResource);
    }

    /**
     * Updates a row of the market.
     *
     * @param index the index representing the row that needs to be changed
     * @param changes a List containing the resources that will be associated to the new row
     */
    public void updateMarketRow(int index, List<Resource> changes) {
        this.grid.set(index, changes);
    }

    /**
     * Update a column of the market.
     *
     * @param index the index representing the column that needs to be changed
     * @param changes a List containing the resources that will be associated to the new column
     */
    public void updateMarketColumn(int index, List<Resource> changes) {
        int i = 0;
        for (List<Resource> row : grid) {
            row.remove(index);
            row.add(index, changes.get(getGrid().indexOf(row)));

            grid.set(i, row); //Triggers the event listeners for this property
            i++;
        }
    }

    /**
     * Counts the occurrences of a white resource in a given row or column of the market.
     *
     * @param index the index representing the row or column of the market
     * @return the occurrences of a white resource in the row or column of the market
     */
    public int countWhiteResources(int index) {
        int result = 0;
        if (index > 0 && index < 5) {
            for (int i = 0; i < 3; i++) {
                if (grid.get(i).get(index - 1) == null) result++;
            }
        } else if (index > 4 && index < 8) {
            for (List<Resource> row : grid) {
                for (Resource res : row) {
                    if (res == null) result++;
                }
            }
        }
        return result;
    }
}
