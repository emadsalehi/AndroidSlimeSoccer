package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;

import static android.view.View.VISIBLE;

public class SinglePlayerSelectActivity extends Activity {

    Intent singlePlayerIntent;
    String firstSlimeText, secondSlimeText;
    TextView slimeName;
    Boolean isFirstPlayerSelected = false;
    Boolean isSecondPlayerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player_select);
    }

    public void onSlimeClick(View v) {
        if (!isFirstPlayerSelected && !isSecondPlayerSelected) {
            isFirstPlayerSelected = true;
            ImageView firstSelector = findViewById(R.id.first_selector);
            firstSelector.setX(v.getX() - 10);
            firstSelector.setY(v.getY() - 10);
            firstSelector.setVisibility(VISIBLE);
            slimeName = findViewById(R.id.slime_name);
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
        }
        if (isFirstPlayerSelected && !isSecondPlayerSelected) {
            isSecondPlayerSelected = true;
            ImageView secondSelector = findViewById(R.id.second_selector);
            secondSelector.setX(v.getX() - 10);
            secondSelector.setY(v.getY() - 10);
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
        }
    }

    public void onPlayClick(View v) {
        if (isFirstPlayerSelected && isSecondPlayerSelected) {
            singlePlayerIntent = new Intent(this, SinglePlayerActivity.class);
            singlePlayerIntent.putExtra("LEFT_SLIME_NAME", firstSlimeText.toLowerCase());
            singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", secondSlimeText.toLowerCase());
            startActivity(singlePlayerIntent);
        } else {
            slimeName = findViewById(R.id.slime_name);
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/Magenta.ttf");
            slimeName.setTypeface(face);
            slimeName.setText("Please Choose a Slime");
        }
    }
}
