package com.android.cmpt276as3.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.android.cmpt276as3.R;

/**
 * Class for Help Screen UI
 *
 * This screen explains to the user what the goal of the game is along with information explaining the basics
 * about the game in the theme of catching pokemon.
 * It displays our names (Artun & Thomas) in the about section and a hyperlink to the CMPT 276 home-page
 * Has citations and links for all images, gifs, sounds used in the game.
 * Pressing the Android back button on the Help screen returns to the Main Menu.
 */

public class HelpScreen extends AppCompatActivity {
    public static Intent makeLaunchIntent(Context context) {
        return new Intent(context, HelpScreen.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        //delete the bar on top
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}