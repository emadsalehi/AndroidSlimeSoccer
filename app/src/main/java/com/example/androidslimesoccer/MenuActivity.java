package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity {

    MediaPlayer mediaPlayer;
    Intent practicePlayerSelect, singlePlayerSelect, multiPlayerIntent, slimologyIntent;
    boolean isPaused;
    TextView singlePlayer, multiPlayer, practice;
    ImageView soundImageView;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        setSinglePlayerText();
        setMultiPlayerText();
        setPracticeText();
        isPaused = getIntent().getBooleanExtra("isPaused", false);
    }

    @Override
    protected void onResume() {
        mediaPlayer = MediaPlayer.create(this, R.raw.main_menu_theme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        soundImageView = findViewById(R.id.sound);
        if (isPaused) {
            mediaPlayer.pause();
            Bitmap muteImageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("mute", "drawable", this.getPackageName()));
            soundImageView.setImageBitmap(muteImageBitmap);
        } else {
            Bitmap unMuteImageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("speaker", "drawable", this.getPackageName()));
            soundImageView.setImageBitmap(unMuteImageBitmap);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    public void onPracticeClick(View v) {
        mediaPlayer.stop();
        practicePlayerSelect = new Intent(this, PracticePlayerSelectActivity.class);
        practicePlayerSelect.putExtra("isPaused", isPaused);
        startActivity(practicePlayerSelect);
    }

    public void onSinglePlayerClick(View v) {
        mediaPlayer.stop();
        singlePlayerSelect = new Intent(this, SinglePlayerSelectActivity.class);
        singlePlayerSelect.putExtra("isPaused", isPaused);
        startActivity(singlePlayerSelect);

    }

    public void onSoundClick(View v) {
        if (mediaPlayer.isPlaying()) {
            isPaused = true;
            Bitmap muteImageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("mute", "drawable", this.getPackageName()));
            ((ImageView) v).setImageBitmap(muteImageBitmap);
            mediaPlayer.pause();
        } else {
            isPaused = false;
            Bitmap unMuteImageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("speaker", "drawable", this.getPackageName()));
            ((ImageView) v).setImageBitmap(unMuteImageBitmap);
            mediaPlayer.start();
        }
    }

    public void onSlimologyClick(View v) {
        mediaPlayer.stop();
        slimologyIntent = new Intent(this, Slimology.class);
        slimologyIntent.putExtra("isPaused", isPaused);
        startActivity(slimologyIntent);
    }

    public void onMultiPlayerClick(View v) {
        mediaPlayer.stop();
//        multiPlayerIntent = new Intent(this, MultiPlayerActivity.class);
//        multiPlayerIntent.putExtra("LEFT_SLIME_NAME", "classic");
//        multiPlayerIntent.putExtra("RIGHT_SLIME_NAME", "classic");
//        multiPlayerIntent.putExtra("GOAL_LIMIT", 5);
//        startActivity(multiPlayerIntent);
//        multiPlayerIntent = new Intent(this, MultiPlayerComingSoon.class);
        multiPlayerIntent = new Intent(this, BroadcastReceiverActivity.class);
        multiPlayerIntent.putExtra("isPaused", isPaused);
        startActivity(multiPlayerIntent);
    }

    public void setSinglePlayerText() {
        singlePlayer = findViewById(R.id.single_player);
        singlePlayer.setTypeface(typeface);
        singlePlayer.setText(R.string.single_player);
        singlePlayer.setTextSize(32);
        singlePlayer.setTextColor(Color.WHITE);
    }

    public void setMultiPlayerText() {
        multiPlayer = findViewById(R.id.multi_player);
        multiPlayer.setTypeface(typeface);
        multiPlayer.setText(R.string.multi_player);
        multiPlayer.setTextSize(32);
        multiPlayer.setTextColor(Color.WHITE);
    }

    public void setPracticeText() {
        practice = findViewById(R.id.practice);
        practice.setTypeface(typeface);
        practice.setText(R.string.practice);
        practice.setTextSize(32);
        practice.setTextColor(Color.WHITE);
    }
}

