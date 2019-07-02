package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//TODO Will Be Completed By "ASHKAN"

public class MenuActivity extends Activity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this, SinglePlayerActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intent.putExtra("LEFT_SLIME_NAME", "classic");
        intent.putExtra("RIGHT_SLIME_NAME", "classic");
        startActivity(intent);
    }
}

