package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Market {
    private Box[][] grid;
    private Resource slideResource;

    public Market(){
        grid = new Box[3][4];

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

        for (int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++) {
                int randomIndex;
                grid[i][j] = new Box();
                if(blankCounter < 4){
                    randomIndex = (int) (Math.random() * (types.size()+1));
                    while(randomIndex == types.size()+1) randomIndex = (int) (Math.random() * (types.size()+1));
                } else{
                    randomIndex = (int) (Math.random() * (types.size()));
                    while(randomIndex == types.size()) randomIndex = (int) (Math.random() * (types.size()));
                }

                //If generated number is equal to size, leave the box with null, indicating a white ball
                if(randomIndex < types.size()){
                    grid[i][j].setResource(types.get(randomIndex));
                }
                if(grid[i][j].getResource() == null) blankCounter++;
                else{
                    switch (grid[i][j].getResource()){
                        case STONE:
                            stoneCounter++;
                            if (stoneCounter >= 2) types.remove(Resource.STONE);
                            break;

                        case COIN:
                            coinCounter++;
                            if (coinCounter >= 2) types.remove(Resource.COIN);
                            break;

                        case SERVANT:
                            servantCounter++;
                            if (servantCounter >= 2) types.remove(Resource.SERVANT);
                            break;

                        case SHIELD:
                            shieldCounter++;
                            if (shieldCounter >= 2) types.remove(Resource.SHIELD);
                            break;

                        case FAITH:
                            types.remove(Resource.FAITH);
                            break;

                    }
                }
            }
        }

        //Set the last remaining resource as the slide resource
        if(types.size() != 0) slideResource = types.get(0);
    }

    public List<Resource> getRow(int row) {
        List<Resource> result = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            result.add(grid[row][i].getResource());
        }
        return result;
    }

    public List<Resource> getColumn(int column) {
        List<Resource> result = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            result.add(grid[i][column].getResource());
        }
        return result;
    }

    public void shiftRow(int row) {
        Resource temp = grid[row][0].getResource();
        for (int i = 0; i < 3; i++){
            grid[row][i].setResource(grid[row][i+1].getResource());
        }
        grid[row][3].setResource(slideResource);
        slideResource = temp;
    }

    public void shiftColumn(int column) {
        Resource temp = grid[0][column].getResource();
        for (int i = 0; i < 2; i++){
            grid[i][column].setResource(grid[i+1][column].getResource());
        }
        grid[2][column].setResource(slideResource);
        slideResource = temp;
    }

    public Resource getSlideResource() {
        return slideResource;
    }
}
