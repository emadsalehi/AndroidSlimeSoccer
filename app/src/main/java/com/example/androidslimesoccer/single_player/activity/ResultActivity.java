package com.example.androidslimesoccer.single_player.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidslimesoccer.R;
import com.example.androidslimesoccer.view.MenuActivity;

import static android.view.Gravity.CENTER;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ResultActivity extends Activity {

    boolean isWon;
    TextView resultTextView;
    ImageView cross;
    Boolean isPaused;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        resultTextView = findViewById(R.id.result);
        cross = findViewById(R.id.cross);
        isWon = getIntent().getBooleanExtra("IS_WON", false);
        isPaused =getIntent().getBooleanExtra("isPaused", false);
        Typeface menuTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Magenta.ttf");
        resultTextView.setTypeface(menuTypeface);
        if (isWon) {
            resultTextView.setText("YOU WON");
            cross.setVisibility(INVISIBLE);
        } else {
            resultTextView.setText("YOU LOST");
            cross.setVisibility(VISIBLE);
        }
        resultTextView.setGravity(CENTER);
    }

    @Override
    public void onBackPressed() {
        this.onDestroy();
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("isPaused", isPaused);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(intent);
    }

    public void onMainMenuClick(View v) {
        this.onDestroy();
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("isPaused", isPaused);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(intent);
    }
}
