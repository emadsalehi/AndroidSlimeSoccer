package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

//TODO Will Be Completed By "ASHKAN"

public class MenuActivity extends Activity {
    MediaPlayer mediaPlayer;
    Intent practicePlayerSelect;
    Intent singlePlayerSelect;
    Intent singlePlayerIntent;
    Intent multiPlayerIntent;
    Intent slimologyIntent;
    boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        mediaPlayer = MediaPlayer.create(this, R.raw.main_menu_theme);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isPaused = getIntent().getBooleanExtra("isPaused", false);
    }

    @Override
    protected void onResume() {
        mediaPlayer = MediaPlayer.create(this, R.raw.main_menu_theme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        ImageView soundImageView = findViewById(R.id.sound);
        if (isPaused) {
            mediaPlayer.pause();
            Bitmap muteImageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("mute", "drawable", this.getPackageName()));
            soundImageView.setImageBitmap(muteImageBitmap);
        }
        else {
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
//        singlePlayerIntent.putExtra("LEFT_SLIME_NAME", "indian");
//        singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", "classic");
//        singlePlayerIntent.putExtra("GOAL_LIMIT", 5);
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
//        multiPlayerIntent = new Intent(this, BroadcastReceiverActivity.class);
        multiPlayerIntent = new Intent(this, MultiPlayerComingSoon.class);
        multiPlayerIntent.putExtra("isPaused", isPaused);
        startActivity(multiPlayerIntent);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}

