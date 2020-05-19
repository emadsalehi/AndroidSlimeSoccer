package com.example.androidslimesoccer.single_player.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidslimesoccer.R;

import java.util.Random;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SinglePlayerSelectActivity extends Activity {

    MediaPlayer mediaPlayer;
    Intent attributeSelectIntent;
    String firstSlimeText, firstSlimeTextPrime, secondSlimeText, secondSlimeTextPrime = "";
    TextView firstSlimeName, secondSlimeName, chooseSlimes, next;
    ImageView firstSlimeSelector, secondSlimeSelector;
    Boolean isFirstPlayerSelected, isSecondPlayerSelected, isPaused;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player_select);
        isFirstPlayerSelected = false;
        isSecondPlayerSelected = false;
        firstSlimeName = findViewById(R.id.first_slime_name);
        secondSlimeName = findViewById(R.id.second_slime_name);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        setChooseSlimes();
        setNext();
        isPaused = getIntent().getBooleanExtra("isPaused", false);
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
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
        chooseSlimes.setVisibility(INVISIBLE);
        firstSlimeName.setVisibility(VISIBLE);
        secondSlimeName.setVisibility(VISIBLE);
        if (!isFirstPlayerSelected && !isSecondPlayerSelected) {
            firstSlimeText = (String) v.getTag();
            firstSlimeTextPrime = firstSlimeText;
            firstSlimeName.setTypeface(typeface);
            firstSlimeName.setText(firstSlimeText);
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
            secondSlimeName.setTypeface(typeface);
            secondSlimeName.setText(secondSlimeText);
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
            onBackPressed();
        } else if (isFirstPlayerSelected && !isSecondPlayerSelected) {
            isFirstPlayerSelected = false;
            firstSlimeSelector = findViewById(R.id.single_player_select).findViewWithTag(firstSlimeTextPrime);
            firstSlimeSelector.setScaleX((float) 1.0);
            firstSlimeSelector.setScaleY((float) 1.0);
            firstSlimeName.setText("");
        } else if (isFirstPlayerSelected) {
            isSecondPlayerSelected = false;
            if (!firstSlimeTextPrime.equals(secondSlimeTextPrime)) {
                secondSlimeSelector = findViewById(R.id.single_player_select).findViewWithTag(secondSlimeTextPrime);
                secondSlimeSelector.setScaleX((float) 1.0);
                secondSlimeSelector.setScaleY((float) 1.0);
            }
            secondSlimeName.setText("");
        }
    }

    public void onNextClick(View v) {
        if (isFirstPlayerSelected && isSecondPlayerSelected) {
            attributeSelectIntent = new Intent(this, AttributeSelectActivity.class);
            attributeSelectIntent.putExtra("LEFT_SLIME_NAME", firstSlimeText.toLowerCase());
            attributeSelectIntent.putExtra("RIGHT_SLIME_NAME", secondSlimeText.toLowerCase());
            attributeSelectIntent.putExtra("isPaused", isPaused);
            startActivity(attributeSelectIntent);
        } else if (isFirstPlayerSelected) {
            chooseSlimes.setVisibility(VISIBLE);
            firstSlimeName.setVisibility(INVISIBLE);
            secondSlimeName.setVisibility(INVISIBLE);
        } else if (!isSecondPlayerSelected) {
            chooseSlimes.setVisibility(VISIBLE);
            firstSlimeName.setVisibility(INVISIBLE);
            secondSlimeName.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    public void setChooseSlimes() {
        chooseSlimes = findViewById(R.id.choose_slime);
        chooseSlimes.setTypeface(typeface);
        chooseSlimes.setText(R.string.choose_slimes);
        chooseSlimes.setTextSize(24);
        chooseSlimes.setTextColor(Color.WHITE);
        chooseSlimes.setVisibility(INVISIBLE);
    }

    public void setNext() {
        next = findViewById(R.id.next);
        next.setTypeface(typeface);
        next.setText(R.string.next);
        next.setTextSize(28);
        next.setTextColor(Color.WHITE);
    }
}
