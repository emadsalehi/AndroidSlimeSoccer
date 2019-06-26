package com.example.androidslimesoccer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

//TODO Will Be Completed By "EMAD"

public class PracticeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String slimeName = getIntent().getStringExtra("SLIME_NAME");
        setContentView(new PracticeGameView(this, slimeName));
    }
}