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

public class PracticePlayerSelectActivity extends Activity {

    Intent practiceIntent;
    String slimeText;
    TextView slimeName;
    Boolean isPlayerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_player_select);
    }

    public void onSlimeClick(View v) {
        isPlayerSelected = true;
        ImageView selector = findViewById(R.id.first_selector);
        selector.setX(v.getX() - 10);
        selector.setY(v.getY() - 10);
        selector.setVisibility(VISIBLE);
        slimeName = findViewById(R.id.slime_name);
        slimeText = (String) v.getTag();
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        slimeName.setTypeface(face);
        slimeName.setText(slimeText);
        if (slimeText.equals("Random")) {
            String[] slimes = {"Classic", "Traffic", "Runner", "Alien", "Indian"};
            Random random = new Random();
            int randomNumber = random.nextInt(slimes.length);
            slimeText = slimes[randomNumber];
        }
    }

    public void onPlayClick(View v) {
        if (isPlayerSelected) {
            practiceIntent = new Intent(this, PracticeActivity.class);
            practiceIntent.putExtra("SLIME_NAME", slimeText.toLowerCase());
            startActivity(practiceIntent);
        } else {
            slimeName = findViewById(R.id.slime_name);
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/Magenta.ttf");
            slimeName.setTypeface(face);
            slimeName.setText("Please Choose a Slime");
        }
    }
}
