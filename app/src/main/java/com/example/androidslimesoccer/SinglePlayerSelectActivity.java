package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import static android.view.View.VISIBLE;

public class SinglePlayerSelectActivity extends Activity {

    Intent singlePlayerIntent;
    String firstSlimeText, secondSlimeText;
    Boolean isFirstPlayerSelected, isSecondPlayerSelected = false;
    TextView slimeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player_select);
    }

    public void onSlimeClick(View v) {
        if (!(isFirstPlayerSelected || isSecondPlayerSelected)) {
            isFirstPlayerSelected = true;
            ImageView firstSelector = findViewById(R.id.selector);
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
    }
}
