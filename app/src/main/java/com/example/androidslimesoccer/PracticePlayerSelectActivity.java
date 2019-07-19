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

public class PracticePlayerSelectActivity extends Activity {

    MediaPlayer mediaPlayer;
    Intent practiceIntent;
    ImageView selectedSlime;
    String slimeText, slimeTextPrime = "";
    TextView slimeName;
    Boolean isPlayerSelected = false;
    Typeface face;
    Boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_player_select);
        slimeName = findViewById(R.id.slime_name_1);
        isPaused = getIntent().getBooleanExtra("isPaused", false);
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
        if (!slimeTextPrime.equals("")) {
            selectedSlime = findViewById(R.id.practice_player_select).findViewWithTag(slimeTextPrime);
            selectedSlime.setScaleX((float) 1);
            selectedSlime.setScaleY((float) 1);
        }
        slimeText = (String) v.getTag();
        slimeTextPrime = slimeText;
        slimeName.setTypeface(face);
        slimeName.setText(slimeText);
        if (slimeText.equals("Random")) {
            String[] slimes = {"Classic", "Traffic", "Runner", "Alien", "Indian"};
            Random random = new Random();
            int randomNumber = random.nextInt(slimes.length);
            slimeText = slimes[randomNumber];
        }
        v.setScaleX((float) 1.5);
        v.setScaleY((float) 1.5);
        isPlayerSelected = true;
    }

    public void onBackClick(View v) {
        if (!isPlayerSelected) {
            onBackPressed();
        }
        else if (isPlayerSelected) {
            isPlayerSelected = false;
            slimeName.setText("");
            selectedSlime = findViewById(R.id.practice_player_select).findViewWithTag(slimeTextPrime);
            selectedSlime.setScaleX((float) 1);
            selectedSlime.setScaleY((float) 1);
        }
    }

    public void onPlayClick(View v) {
        if (isPlayerSelected) {
            practiceIntent = new Intent(this, PracticeActivity.class);
            practiceIntent.putExtra("SLIME_NAME", slimeText.toLowerCase());
            practiceIntent.putExtra("isPaused", isPaused);
            mediaPlayer.stop();
            startActivity(practiceIntent);
        } else {
            slimeName = findViewById(R.id.slime_name_1);
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/Magenta.ttf");
            slimeName.setTypeface(face);
            slimeName.setText("Please Choose a Slime");
        }
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }
}
