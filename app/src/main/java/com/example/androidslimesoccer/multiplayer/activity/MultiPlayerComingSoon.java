package com.example.androidslimesoccer.multiplayer.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androidslimesoccer.R;

public class MultiPlayerComingSoon extends Activity {
    MediaPlayer mediaPlayer;
    Boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_player_coming_soon);
        TextView comingSoon = findViewById(R.id.coming_soon);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        isPaused = getIntent().getBooleanExtra("isPaused", false);
        comingSoon.setTypeface(face);
        comingSoon.setText("Coming Soon...");
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

    public void onBackClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}
