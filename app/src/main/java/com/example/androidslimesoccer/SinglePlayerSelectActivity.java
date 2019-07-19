package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;

public class SinglePlayerSelectActivity extends Activity {

    MediaPlayer mediaPlayer;
    Intent singlePlayerIntent;
    String firstSlimeText, firstSlimeTextPrime, secondSlimeText, secondSlimeTextPrime = "";
    TextView slimeName1, slimeName2;
    ImageView firstSelector, secondSelector;
    Boolean isFirstPlayerSelected, isSecondPlayerSelected;
    Typeface face;
    Boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player_select);
        isFirstPlayerSelected = false;
        isSecondPlayerSelected = false;
        isPaused = getIntent().getBooleanExtra("isPaused", false);
        slimeName1 = findViewById(R.id.slime_name_1);
        slimeName2 = findViewById(R.id.slime_name_2);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
    }

    @Override
    protected void onResume() {
        mediaPlayer = MediaPlayer.create(this, R.raw.practice_song);
        mediaPlayer.setLooping(true);
        if (!isPaused) {
            mediaPlayer.start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    public void onSlimeClick(View v) {
        if (!isFirstPlayerSelected && !isSecondPlayerSelected) {
            firstSlimeText = (String) v.getTag();
            firstSlimeTextPrime = firstSlimeText;
            slimeName1.setTypeface(face);
            slimeName1.setText(firstSlimeText);
            if (firstSlimeText.equals("Random")) {
                String[] slimes = {"Classic", "Traffic", "Runner", "Alien", "Indian"};
                Random random = new Random();
                int randomNumber = random.nextInt(slimes.length);
                firstSlimeText = slimes[randomNumber];
            }
            v.setScaleX((float) 1.5);
            v.setScaleY((float) 1.5);
            isFirstPlayerSelected = true;
        } else if (isFirstPlayerSelected && !isSecondPlayerSelected) {
            secondSlimeText = (String) v.getTag();
            secondSlimeTextPrime = secondSlimeText;
            slimeName2.setTypeface(face);
            slimeName2.setText(secondSlimeText);
            if (secondSlimeText.equals("Random")) {
                String[] slimes = {"Classic", "Traffic", "Runner", "Alien", "Indian"};
                Random random = new Random();
                int randomNumber = random.nextInt(slimes.length);
                secondSlimeText = slimes[randomNumber];
            }
            v.setScaleX((float) 1.5);
            v.setScaleY((float) 1.5);
            isSecondPlayerSelected = true;
        }
    }

    public void onBackClick(View v) {
        if (!isFirstPlayerSelected && !isSecondPlayerSelected) {
            super.onBackPressed();
        } else if (isFirstPlayerSelected && !isSecondPlayerSelected) {
            isFirstPlayerSelected = false;
            firstSelector = findViewById(R.id.single_player_select).findViewWithTag(firstSlimeTextPrime);
            firstSelector.setScaleX((float) 1.0);
            firstSelector.setScaleY((float) 1.0);
            slimeName1.setText("");
        } else if (isFirstPlayerSelected && isSecondPlayerSelected) {
            isSecondPlayerSelected = false;
            if (!firstSlimeTextPrime.equals(secondSlimeTextPrime)) {
                secondSelector = findViewById(R.id.single_player_select).findViewWithTag(secondSlimeTextPrime);
                secondSelector.setScaleX((float) 1.0);
                secondSelector.setScaleY((float) 1.0);
            }
            slimeName2.setText("");
        }
    }

    public void onPlayClick(View v) {
        if (isFirstPlayerSelected && isSecondPlayerSelected) {
            singlePlayerIntent = new Intent(this, SinglePlayerActivity.class);
            singlePlayerIntent.putExtra("LEFT_SLIME_NAME", firstSlimeText.toLowerCase());
            singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", secondSlimeText.toLowerCase());
            singlePlayerIntent.putExtra("GOAL_LIMIT", 1);
            mediaPlayer.stop();
            startActivity(singlePlayerIntent);
        } else {
            slimeName1 = findViewById(R.id.slime_name_1);
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/Magenta.ttf");
            slimeName1.setTypeface(face);
            slimeName1.setText("Please Choose a Slime");
        }
    }
}
