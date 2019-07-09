package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.VISIBLE;

//TODO Will Be Completed By "ASHKAN"

public class PracticePlayerSelectActivity extends Activity {


    Intent practiceIntent;
    String slimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_player_select);
    }

    public void onSlimeClick(View v) {
        ImageView selector = findViewById(R.id.selector);
        selector.setX(v.getX() - 10);
        selector.setY(v.getY() - 10);
        selector.setVisibility(VISIBLE);
        TextView slimeName = findViewById(R.id.slime_name);
        slimeText = (String) v.getTag();
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        slimeName.setTypeface(face);
        slimeName.setText(slimeText);
    }

    public void onPlayClick(View v) {
        practiceIntent = new Intent(this, PracticeActivity.class);
        practiceIntent.putExtra("SLIME_NAME", slimeText.toLowerCase());
        startActivity(practiceIntent);
    }
}
