package com.android.cmpt276as3.model;

import java.util.Random;

/**
 * A model class for Game Screen.
 *
 * Contains the state of the game board. Keeps track of where the pokemon are, whether a button was clicked
 * and whether a button was scanned.
 *
 * Includes functions like getters and setters for the matrices
 */

public class GameState {
    private final int NUM_ROWS = OptionsManager.getGameBoardRows();
    private final int NUM_COLS = OptionsManager.getGameBoardCols();
    private int NUM_POKEMON = OptionsManager.getNumWildPokemon();

    int [][] tableForPokemon = new int[NUM_ROWS][NUM_COLS];
    boolean [][] clickedButtons = new boolean[NUM_ROWS][NUM_COLS];
    boolean [][] scannedButtons = new boolean[NUM_ROWS][NUM_COLS];

    int numberOfPokemonFound = 0;
    int countScan = 0;

    //Create the table and populate Pokemons
    public void setTable(){
        // Got the idea from https://www.youtube.com/watch?v=nORt4szAmkI
        // Random for generating numbers
        Random r = new Random();

        //Populate Pokemon
        while(NUM_POKEMON > 0){
            int row = r.nextInt(NUM_ROWS);
            int col = r.nextInt(NUM_COLS);

            //-1 is the Pokemon
            if(tableForPokemon[row][col] != -1){
                tableForPokemon[row][col] = -1;
                NUM_POKEMON--;
            }
        }
    }

    public boolean isButtonClicked(int row, int col){
        //keep track of the buttons that are clicked
        return clickedButtons[row][col];
    }

    public void setClickedButtons(int row, int col){
        clickedButtons[row][col] = true;
    }

    public int updatePokemonNumber(int row, int col){
        int count = 0;
        //Check its column
        for(int i = 0; i < NUM_ROWS ; i ++){
            if(isButtonPokemon(i,col)){
                if(!isButtonClicked(i,col)) { //pokemon is not clicked yet
                    count++;
                }
            }
        }
        //Check its row
        for(int i = 0; i < NUM_COLS; i++){
            if(isButtonPokemon(row,i)){
                if(!isButtonClicked(row,i)) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getCountScan(){
        countScan = 0;
        for(int row = 0; row < NUM_ROWS ; row++){
            for(int col = 0; col < NUM_COLS; col++){
                if(isButtonScanned(row,col)){
                    countScan ++;
                }
            }
        }
        return countScan;
    }

    public int getNumberOfPokemonFound() {
        numberOfPokemonFound = 0;
        for(int row = 0; row < NUM_ROWS ; row++){
            for(int col = 0; col < NUM_COLS; col++){
                if(isButtonPokemon(row,col) && isButtonClicked(row,col)){
                    numberOfPokemonFound++;
                }
            }
        }
        return numberOfPokemonFound;
    }

    public boolean isButtonScanned(int row, int col) {
        return scannedButtons[row][col];
    }

    public void setScannedButtons(int row, int col) {
        //if it is a grass
        if(!isButtonPokemon(row,col)){
            scannedButtons[row][col] = true;
        }
        //if it is a pokemon
        if(isButtonPokemon(row,col)){
            //if it is clicked, scan
            if(isButtonClicked(row,col)){
                scannedButtons[row][col] = true;
            }
            //if it is not clicked, don't scan
        }
    }

    public boolean isButtonPokemon(int row, int col) {
        //If the table has Pokemon, return true;
        return tableForPokemon[row][col] == -1;
    }
}
