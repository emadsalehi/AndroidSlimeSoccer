package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

//TODO Will Be Completed By "ASHKAN"

public class MenuActivity extends Activity {

    Intent practicePlayerSelect;
    //    Intent practiceIntent;
    Intent singlePlayerIntent;
//    Intent multiPlayerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onPracticeClick(View v) {
        practicePlayerSelect = new Intent(this, PracticePlayerSelectActivity.class);
        startActivity(practicePlayerSelect);
    }

    public void onSinglePlayerClick(View v) {
        singlePlayerIntent = new Intent(this, SinglePlayerActivity.class);
        singlePlayerIntent.putExtra("LEFT_SLIME_NAME", "classic");
        singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", "classic");
        singlePlayerIntent.putExtra("GOAL_LIMIT", 5);
        startActivity(singlePlayerIntent);
    }

//    public void onMultiPlayerClick(View v) {
//        multiPlayerIntent = new Intent(this, MultiPlayerActivity.class);
//        multiPlayerIntent.putExtra("LEFT_SLIME_NAME", "classic");
//        multiPlayerIntent.putExtra("RIGHT_SLIME_NAME", "classic");
//        multiPlayerIntent.putExtra("GOAL_LIMIT", 5);
//        startActivity(multiPlayerIntent);
//    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}

