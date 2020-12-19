package com.android.cmpt276as3.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.cmpt276as3.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Class for Welcome Screen UI
 *
 * Introduces the name of the application and name of the group members.
 * Has an animation of pictures of Eevee and 3 of it's evolutions coming into the screen from different angles.
 * Includes a custom made button to go to the Main Screen.
 */

public class WelcomeScreen extends AppCompatActivity {
    private GifImageView gif;
    private boolean isButtonClicked = false;

    Animation topAnim, bottomAnim, rightAnim, leftAnim;
    ImageView imgFirstEevee, imgSecondEevee, imgThirdEevee, imgFourthEevee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        //delete the bar on top
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gif = findViewById(R.id.loading_pokeball);

        final ImageButton buttonGoMain = findViewById(R.id.button_gotomain);
        buttonGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isButtonClicked) {
                    isButtonClicked = true;
                    gif.setAlpha(1f);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = MainMenu.makeLaunchIntent(WelcomeScreen.this);
                            startActivity(i);
                        }
                    }, 500);
                }
            }
        });

        //Animation
        setupAnimation();
    }

    private void setupAnimation() {
        //Code from https://www.youtube.com/watch?v=JLIFqqnSNmg
        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        leftAnim = AnimationUtils.loadAnimation(this,R.anim.left_animation);
        rightAnim = AnimationUtils.loadAnimation(this,R.anim.right_animation);

        //Hook
        imgFirstEevee = findViewById(R.id.imageFirstEevee);
        imgSecondEevee = findViewById(R.id.imageSecondEevee);
        imgThirdEevee = findViewById(R.id.imageThirdEevee);
        imgFourthEevee = findViewById(R.id.imageFourthEevee);

        imgFirstEevee.setAnimation(leftAnim);
        imgSecondEevee.setAnimation(bottomAnim);
        imgThirdEevee.setAnimation(topAnim);
        imgFourthEevee.setAnimation(rightAnim);
    }

    @Override
    protected void onStart() {
        super.onStart();

        gif.setAlpha(0f);
        isButtonClicked = false;
    }
}