package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

//TODO Will Be Completed By "ASHKAN"

public class MenuActivity extends Activity {

    Intent practiceIntent;

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
        practiceIntent = new Intent(this, PracticeActivity.class);
        practiceIntent.putExtra("SLIME_NAME", "classic");
        startActivity(practiceIntent);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}

