package it.polimi.ingsw.model;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.model.messages.CreateMarketUpdate;
import it.polimi.ingsw.model.messages.MarketUpdate;
import it.polimi.ingsw.server.IServerPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing the game market. Notifies all registered observer of state updates.
 */
public class Market extends Observable<IServerPacket> {
    private final Box[][] grid;
    private Resource slideResource;

    /**
     * Constructs an empty market.
     */
    public Market() {
        grid = new Box[3][4];
    }

    /**
     * Initializes the market with randomized marbles in each box. The market always contains the following marbles:
     * <ul>
     *     <li>2 COIN</li>
     *     <li>2 STONE</li>
     *     <li>2 SHIELD</li>
     *     <li>2 SERVANT</li>
     *     <li>1 FAITH</li>
     *     <li>4 WHITE</li>
     * </ul>
     * Each marble represents a {@link Resource}, except for WHITE that represent no resource (in the data structure
     * this is stored as <code>null</code>).
     */
    public void initializeMarket() {
        int stoneCounter = 0;
        int coinCounter = 0;
        int servantCounter = 0;
        int shieldCounter = 0;
        int blankCounter = 0;
        List<Resource> types = new ArrayList<>();
        types.add(Resource.STONE);
        types.add(Resource.COIN);
        types.add(Resource.SERVANT);
        types.add(Resource.SHIELD);
        types.add(Resource.FAITH);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int randomIndex;
                grid[i][j] = new Box();
                if (blankCounter < 4) {
                    randomIndex = (int) (Math.random() * (types.size() + 1));
                    while (randomIndex == types.size() + 1) randomIndex = (int) (Math.random() * (types.size() + 1));
                } else {
                    randomIndex = (int) (Math.random() * (types.size()));
                    while (randomIndex == types.size()) randomIndex = (int) (Math.random() * (types.size()));
                }

                //If generated number is equal to size, leave the box with null, indicating a white ball
                if (randomIndex < types.size()) {
                    grid[i][j].setResource(types.get(randomIndex));
                }
                if (grid[i][j].getResource() == null) blankCounter++;
                else {
                    switch (grid[i][j].getResource()) {
                        case STONE -> {
                            stoneCounter++;
                            if (stoneCounter >= 2) types.remove(Resource.STONE);
                        }
                        case COIN -> {
                            coinCounter++;
                            if (coinCounter >= 2) types.remove(Resource.COIN);
                        }
                        case SERVANT -> {
                            servantCounter++;
                            if (servantCounter >= 2) types.remove(Resource.SERVANT);
                        }
                        case SHIELD -> {
                            shieldCounter++;
                            if (shieldCounter >= 2) types.remove(Resource.SHIELD);
                        }
                        case FAITH -> types.remove(Resource.FAITH);
                    }
                }
            }
        }

        //Set the last remaining resource as the slide resource
        if (types.size() != 0) slideResource = types.get(0);

        notifyCreation();
    }

    /**
     * Gets a List of resources corresponding to the marbles in the given row.
     *
     * @param row the index of the row, valid values are between 0 and 2, where 0 represents the top row and 2 the
     *            bottom row
     * @return a list of the resources contained in the given row
     */
    public List<Resource> getRow(int row) {
        List<Resource> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            result.add(grid[row][i].getResource());
        }
        return result;
    }

    /**
     * Gets a List of resources corresponding to the marbles in the given column.
     *
     * @param column the index of the column, valid values are between 0 and 3, where 0 represents the leftmost column
     *               and 3 the rightmost column
     * @return a list of the resources contained in the given column
     */
    public List<Resource> getColumn(int column) {
        List<Resource> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            result.add(grid[i][column].getResource());
        }
        return result;
    }

    /**
     * Shifts the given row to the left, inserting the slide marble in the rightmost position and moving the
     * marble in the leftmost position in the slide.
     *
     * @param row the index of the row to shift, valid values are between 0 and 2, where 0 represents the top row and 2
     *            the bottom row
     */
    public void shiftRow(int row) {
        Resource temp = grid[row][0].getResource();
        for (int i = 0; i < 3; i++) {
            grid[row][i].setResource(grid[row][i + 1].getResource());
        }
        grid[row][3].setResource(slideResource);
        slideResource = temp;
        notify(new MarketUpdate(row + 4, getRow(row), getSlideResource()));
    }

    /**
     * Shifts the given column up, inserting the slide marble in the bottom position and moving the marble in the
     * top position in the slide.
     *
     * @param column the index of the column to shift, valid values are between 0 and 3, where 0 represents the leftmost
     *               column and 3 the rightmost column
     */
    public void shiftColumn(int column) {
        Resource temp = grid[0][column].getResource();
        for (int i = 0; i < 2; i++) {
            grid[i][column].setResource(grid[i + 1][column].getResource());
        }
        grid[2][column].setResource(slideResource);
        slideResource = temp;
        notify(new MarketUpdate(column, getColumn(column), getSlideResource()));
    }

    /**
     * Gets the resource corresponding to the marble in the slide.
     *
     * @return the resource corresponding to the marble in the slide
     */
    synchronized Resource getSlideResource() {
        return slideResource;
    }

    public void notifyCreation() {
        List<List<Resource>> update = new ArrayList<>();
        update.add(getRow(0));
        update.add(getRow(1));
        update.add(getRow(2));

        notify(new CreateMarketUpdate(update, slideResource));
    }
}
