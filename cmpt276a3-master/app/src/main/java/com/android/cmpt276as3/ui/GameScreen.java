package com.android.cmpt276as3.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.cmpt276as3.R;
import com.android.cmpt276as3.model.GameState;
import com.android.cmpt276as3.model.GetRandPokemonId;
import com.android.cmpt276as3.model.MusicPlayer;
import com.android.cmpt276as3.model.OptionsManager;

/**
 *  Class for Game Screen UI
 *
 *  Sets up and displays a grid of buttons, the size of which is set by the options screen. A number of pokemon
 *  are also populated (and hidden) in this grid, the number which is also determined by the options screen.
 *
 *  Displays text stating how many total wild pokemon there are in the grass (game board), and how many have been found.
 *  Displays text stating how many scans it has taken the user so far this game.
 *  Display text stating the total number of games started (saved between application launches),
 *  and best score so far of any completed game of this specific configuration. Best scores are saved.
 *
 *  Tapping a cell (grass) investigates the cell, which either, reveals a pokemon if one is present or
 *  performs a scan if either pokemon not present, or if pokemon has already been revealed. Pokemon image
 *  is randomly generated to add randomness to what you will find.
 *
 *  When a scan is performed, the count of hidden mines in the row and column is displayed in that button.
 *  When a pokemon is revealed, this class updates the count of hidden pokemon
 *  Plays a corresponding sound depending on if a scan was performed or pokemon was found
 */

public class GameScreen extends AppCompatActivity {
    private final int NUM_ROWS = OptionsManager.getGameBoardRows();
    private final int NUM_COLS = OptionsManager.getGameBoardCols();
    private final int NUM_POKEMON = OptionsManager.getNumWildPokemon();

    //this must stay here so that it doesn't create a new table over and over again whenever the button is clicked
    GameState gameState = new GameState();

    Button[][] buttons = new Button[NUM_ROWS][NUM_COLS];

    public static Intent makeLaunchIntent(Context context) {
        return new Intent(context, GameScreen.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        //delete the bar on top
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView plays = findViewById(R.id.textNumberOfTimesPlayed);
        plays.setText(getString(R.string.number_of_games_played, OptionsManager.getNumPlays(this)));

        TextView score = findViewById(R.id.textHighScores);
        if(OptionsManager.getHighScore(this) == Integer.MAX_VALUE) { score.setText(R.string.no_high_score); }
        else { score.setText(getString(R.string.high_score,OptionsManager.getHighScore(this))); }

        populateButtons();

        displayNumberOfPokemonsLeft();
        displayNumberOfTimeScanned();
    }

    private void setupCongratulationMessage() {
        //Disable Users' touch
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        FragmentManager manager = getSupportFragmentManager();
        CongratulationMessageFragment dialog = new CongratulationMessageFragment();
        dialog.show(manager, "CongratulationMessageDialog");
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForButtons);

        //populating buttons in table
        for(int row = 0; row < NUM_ROWS; row ++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);

            for(int col = 0 ; col < NUM_COLS; col++){
                final int FINAL_ROW = row;
                final int FINAL_COL = col;

                Button btn = new Button(this);
                btn.setBackgroundResource(R.drawable.tile_tall_grass);
                btn.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));

                //set text size in button
                int textSize = 0;
                if(NUM_COLS*NUM_ROWS < 25){
                    textSize = 50;
                }
                else if(NUM_COLS*NUM_ROWS < 50){
                    textSize = 45;
                }
                else if(NUM_COLS*NUM_ROWS < 100){
                    textSize = 25;
                }
                btn.setTextSize( textSize);
                //set text color
                btn.setTextColor(getApplication().getResources().getColor(R.color.red, getTheme()));

                //when the button is clicked,
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        populatePokemon(FINAL_ROW, FINAL_COL);
                        scanPokemon(FINAL_ROW, FINAL_COL);
                        displayNumberOfPokemonsLeft();
                        displayNumberOfTimeScanned();

                        //if the user wins
                        if(gameState.getNumberOfPokemonFound() == NUM_POKEMON){
                            if(gameState.getCountScan() < OptionsManager.getHighScore(GameScreen.this)) {
                                OptionsManager.setHighScore(GameScreen.this, gameState.getCountScan());
                            }
                            setupCongratulationMessage();
                        }
                    }
                });

                tableRow.addView(btn);
                buttons[row][col] = btn;
            }
        }
    }

    private void populatePokemon(int row, int col) {
        Button btn = buttons[row][col];

        //Lock Button Sizes:
        lockButtonSizes();

        //Set the pokemon table
        gameState.setTable();

        //If the button is Pokemon, set the backGround to pokemon
        if(gameState.isButtonPokemon(row, col) && !gameState.isButtonClicked(row, col)) {
            int newWidth = btn.getWidth();
            int newHeight = btn.getHeight();
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), GetRandPokemonId.getId());
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
            Resources resource = getResources();
            btn.setBackground(new BitmapDrawable(resource, scaledBitmap));
            MusicPlayer.playPokemonSound(this);
        }
    }

    private void scanPokemon(int row, int col) {
        if(!gameState.isButtonPokemon(row, col) && !gameState.isButtonClicked(row, col)) {
            MusicPlayer.playScanSound(this);
        }

        //Calculate Scans
        gameState.setScannedButtons(row,col);
        //Let gameState know that the buttons is clicked
        gameState.setClickedButtons(row,col);

        //Display Clicked(Scanned) Button Numbers
        displayNumbers();
    }

    private void displayNumberOfPokemonsLeft() {
        int numberOfPokemonFound = gameState.getNumberOfPokemonFound();
        TextView textNumberOfPokemonsFound = findViewById(R.id.textNumberOfPokemonFound);
        textNumberOfPokemonsFound.setText(getString(R.string.numberOfPokemonFound,numberOfPokemonFound,NUM_POKEMON));
    }

    private void displayNumberOfTimeScanned() {
        int numberOfTimeScanned = gameState.getCountScan();
        TextView textNumberOfTimeScanned = findViewById(R.id.textNumberOfTimesScanned);
        textNumberOfTimeScanned.setText(getString(R.string.numberOfTimesScanned,numberOfTimeScanned));
    }

    private void displayNumbers() {
        for(int row = 0; row < NUM_ROWS; row++){
            for(int col = 0; col < NUM_COLS; col++){
                if(gameState.isButtonScanned(row,col)) {
                    buttons[row][col].setText(getString(R.string.updatePokemonNumbers,gameState.updatePokemonNumber(row, col)));
                }
            }
        }
    }

    private void lockButtonSizes() {
        for(int row = 0; row < NUM_ROWS; row++){
            for(int col = 0; col < NUM_COLS; col++){
                Button btn = buttons[row][col];

                int width = btn.getWidth();
                btn.setMinWidth(width);
                btn.setMaxWidth(width);

                int height = btn.getHeight();
                btn.setMinHeight(height);
                btn.setMaxHeight(height);
            }
        }
    }
}