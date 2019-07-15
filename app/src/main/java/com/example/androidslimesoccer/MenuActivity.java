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
    //    Intent practiceIntent;
    Intent singlePlayerIntent;
    Intent multiPlayerIntent;

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
    protected void onPause(){
        mediaPlayer.pause();
        super.onPause();
    }

    public void onPracticeClick(View v) {
        practicePlayerSelect = new Intent(this, PracticePlayerSelectActivity.class);
        startActivity(practicePlayerSelect);
    }

    public void onSinglePlayerClick(View v) {
        singlePlayerIntent = new Intent(this, SinglePlayerActivity.class);
        singlePlayerIntent.putExtra("LEFT_SLIME_NAME", "indian");
        singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", "classic");
        singlePlayerIntent.putExtra("GOAL_LIMIT", 5);
        startActivity(singlePlayerIntent);
    }

    public void onSoundClick(View v) {
        if (mediaPlayer.isPlaying()){
            Bitmap muteImageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("mute", "drawable", this.getPackageName()));
            ((ImageView)v).setImageBitmap(muteImageBitmap);
            mediaPlayer.pause();
        }
        else {
            Bitmap unMuteImageBitmap =  BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("speaker", "drawable", this.getPackageName()));
            ((ImageView)v).setImageBitmap(unMuteImageBitmap);
            mediaPlayer.start();
        }
    }

//    public void onMultiPlayerClick(View v) {
    public void onMultiPlayerClick(View v) {
//        multiPlayerIntent = new Intent(this, MultiPlayerActivity.class);
//        multiPlayerIntent.putExtra("LEFT_SLIME_NAME", "classic");
//        multiPlayerIntent.putExtra("RIGHT_SLIME_NAME", "classic");
//        multiPlayerIntent.putExtra("GOAL_LIMIT", 5);
//        startActivity(multiPlayerIntent);
        multiPlayerIntent = new Intent(this, HostJoinSelectActivity.class);
        startActivity(multiPlayerIntent);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}

