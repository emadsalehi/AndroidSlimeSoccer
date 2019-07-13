package com.example.androidslimesoccer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

//TODO Will Be Completed By "SINA"

public class SinglePlayerActivity extends Activity {
    SinglePlayerGameView singlePlayerGameView;
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
        singlePlayerGameView = new SinglePlayerGameView(this, leftSlimeName, rightSlimeName, goalLimit);
        setContentView(singlePlayerGameView);
        Log.i("singleplayer", "activity");
    }

    @Override
    public void onBackPressed(){
        singlePlayerGameView.thread.setPaused(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage("Do you want to leave the game? ");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                singlePlayerGameView.thread.setRunning(false);
                SinglePlayerActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("NOPE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                singlePlayerGameView.thread.setPaused(false);
            }
        });
        builder.show();
    }
}
