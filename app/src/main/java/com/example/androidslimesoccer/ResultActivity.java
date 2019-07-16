package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.Gravity.CENTER;
import static android.view.View.VISIBLE;

public class ResultActivity extends Activity {

    boolean isWon;
    TextView resultTextView;
    ImageView cross;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        resultTextView = findViewById(R.id.result);
        cross = findViewById(R.id.cross);
        isWon = getIntent().getBooleanExtra("IS_WON", false);
        Typeface menuTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Magenta.ttf");
        resultTextView.setTypeface(menuTypeface);
        if (isWon)
            resultTextView.setText("You Won");
        else {
            resultTextView.setText("You Lost");
            cross.setVisibility(VISIBLE);
        }
        resultTextView.setGravity(CENTER);
    }

    @Override
    public void onBackPressed() {
        this.onDestroy();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(intent);
    }

    public void onMainMenuClick(View v) {
        this.onDestroy();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(intent);
    }
}
