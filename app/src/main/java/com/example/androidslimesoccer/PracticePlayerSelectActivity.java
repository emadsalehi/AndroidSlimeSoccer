package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;

import static android.view.View.*;

public class PracticePlayerSelectActivity extends Activity {

    Intent practiceIntent;
    String slimeText;
    TextView slimeName;
    ImageView selector;
    Boolean isPlayerSelected = false;
    Bitmap bitmapSelector;
    Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_player_select);
        selector = findViewById(R.id.first_selector);
        slimeName = findViewById(R.id.slime_name);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
    }

    public void onSlimeClick(View v) {
        selector.setX(v.getX() - 10);
        selector.setY(v.getY() - 7);
        selector.setVisibility(VISIBLE);
        slimeText = (String) v.getTag();
        slimeName.setTypeface(face);
        slimeName.setText(slimeText);
        if (slimeText.equals("Random")) {
            String[] slimes = {"Classic", "Traffic", "Runner", "Alien", "Indian"};
            Random random = new Random();
            int randomNumber = random.nextInt(slimes.length);
            slimeText = slimes[randomNumber];
        }
        isPlayerSelected = true;
    }

    public void onBackClick(View v) {
        if (!isPlayerSelected)
            super.onBackPressed();
        else if (isPlayerSelected) {
            isPlayerSelected = false;
            selector.setVisibility(INVISIBLE);
            slimeName.setText("");
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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}
