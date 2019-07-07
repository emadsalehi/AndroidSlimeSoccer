package com.example.androidslimesoccer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

//TODO Will Be Completed By "SINA"

public class SinglePlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String leftSlimeName = getIntent().getStringExtra("LEFT_SLIME_NAME");
        String rightSlimeName = getIntent().getStringExtra("RIGHT_SLIME_NAME");
        int goalLimit = getIntent().getIntExtra("GOAL_LIMIT", 0);
        Log.i("singleplayer", "activity");
        setContentView(new SinglePlayerGameView(this, leftSlimeName, rightSlimeName, goalLimit));
        Log.i("singleplayer", "activity");
    }
}
