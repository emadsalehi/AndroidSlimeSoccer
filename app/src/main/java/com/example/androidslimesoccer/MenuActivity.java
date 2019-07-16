package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mediaPlayer = MediaPlayer.create(this, R.raw.main_menu_theme);
        mediaPlayer.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void onPracticeClick(View v) {
        practicePlayerSelect = new Intent(this, PracticePlayerSelectActivity.class);
        startActivity(practicePlayerSelect);
    }

    public void onSinglePlayerClick(View v) {
        singlePlayerSelect = new Intent(this, SinglePlayerSelectActivity.class);
//        singlePlayerIntent.putExtra("LEFT_SLIME_NAME", "indian");
//        singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", "classic");
//        singlePlayerIntent.putExtra("GOAL_LIMIT", 5);
        startActivity(singlePlayerSelect);
    }

    public void onSoundClick(View v) {
        if (mediaPlayer.isPlaying()) {
            Bitmap muteImageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("mute", "drawable", this.getPackageName()));
            ((ImageView) v).setImageBitmap(muteImageBitmap);
            mediaPlayer.pause();
        } else {
            Bitmap unMuteImageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("speaker", "drawable", this.getPackageName()));
            ((ImageView) v).setImageBitmap(unMuteImageBitmap);
            mediaPlayer.start();
        }
    }

    public void onSlimologyClick(View v) {
        slimologyIntent = new Intent(this, Slimology.class);
        startActivity(slimologyIntent);
    }

    public void onMultiPlayerClick(View v) {
//        multiPlayerIntent = new Intent(this, MultiPlayerActivity.class);
//        multiPlayerIntent.putExtra("LEFT_SLIME_NAME", "classic");
//        multiPlayerIntent.putExtra("RIGHT_SLIME_NAME", "classic");
//        multiPlayerIntent.putExtra("GOAL_LIMIT", 5);
//        startActivity(multiPlayerIntent);
//        multiPlayerIntent = new Intent(this, HostJoinSelectActivity.class);
        multiPlayerIntent = new Intent(this, MultiPlayerComingSoon.class);
        startActivity(multiPlayerIntent);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}

