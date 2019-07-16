package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;

import static android.view.View.*;

public class SinglePlayerSelectActivity extends Activity {
    MediaPlayer mediaPlayer;
    Intent singlePlayerIntent;
    String firstSlimeText, secondSlimeText;
    TextView slimeName;
    ImageView firstSelector, secondSelector;
    Boolean isFirstPlayerSelected;
    Boolean isSecondPlayerSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mediaPlayer = MediaPlayer.create(this, R.raw.practice_song);
        mediaPlayer.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player_select);
        isFirstPlayerSelected = false;
        isSecondPlayerSelected = false;
        firstSelector = findViewById(R.id.first_selector);
        secondSelector = findViewById(R.id.second_selector);
        slimeName = findViewById(R.id.slime_name);
    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    public void onSlimeClick(View v) {
        if (!isFirstPlayerSelected && !isSecondPlayerSelected) {
            firstSelector.setX(v.getX() - 10);
            firstSelector.setY(v.getY() - 7);
            firstSelector.setVisibility(VISIBLE);
            firstSlimeText = (String) v.getTag();
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/Magenta.ttf");
            slimeName.setTypeface(face);
            slimeName.setText(firstSlimeText);
            if (firstSlimeText.equals("Random")) {
                String[] slimes = {"Classic", "Traffic", "Runner", "Alien", "Indian"};
                Random random = new Random();
                int randomNumber = random.nextInt(slimes.length);
                firstSlimeText = slimes[randomNumber];
            }
            isFirstPlayerSelected = true;
        } else if (isFirstPlayerSelected && !isSecondPlayerSelected) {
//            Bitmap bitmap = getResizedBitmap(((BitmapDrawable) secondSelector.getDrawable()).getBitmap(), v.getWidth(), v.getHeight());
//            Drawable drawble = new BitmapDrawable(getResources(), bitmap);
//            secondSelector.setImageDrawable(drawble);
            secondSelector.setX(v.getX() - 10);
            secondSelector.setY(v.getY() - 7);
            secondSelector.setVisibility(VISIBLE);
            slimeName = findViewById(R.id.slime_name);
            secondSlimeText = (String) v.getTag();
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/Magenta.ttf");
            slimeName.setTypeface(face);
            slimeName.setText(secondSlimeText);
            if (secondSlimeText.equals("Random")) {
                String[] slimes = {"Classic", "Traffic", "Runner", "Alien", "Indian"};
                Random random = new Random();
                int randomNumber = random.nextInt(slimes.length);
                secondSlimeText = slimes[randomNumber];
            }
            isSecondPlayerSelected = true;
        }
    }

    public void onBackClick(View v) {
        if (!isFirstPlayerSelected && !isSecondPlayerSelected) {
            super.onBackPressed();
        } else if (isFirstPlayerSelected && !isSecondPlayerSelected) {
            isFirstPlayerSelected = false;
            firstSelector.setVisibility(INVISIBLE);
            slimeName.setText("");
        } else if (isFirstPlayerSelected && isSecondPlayerSelected) {
            isSecondPlayerSelected = false;
            secondSelector.setVisibility(INVISIBLE);
            slimeName.setText(firstSlimeText);
        }
    }

    public void onPlayClick(View v) {
        if (isFirstPlayerSelected && isSecondPlayerSelected) {
            singlePlayerIntent = new Intent(this, SinglePlayerActivity.class);
            singlePlayerIntent.putExtra("LEFT_SLIME_NAME", firstSlimeText.toLowerCase());
            singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", secondSlimeText.toLowerCase());
            singlePlayerIntent.putExtra("GOAL_LIMIT", 1);
            startActivity(singlePlayerIntent);
        } else {
            slimeName = findViewById(R.id.slime_name);
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/Magenta.ttf");
            slimeName.setTypeface(face);
            slimeName.setText("Please Choose a Slime");
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}
