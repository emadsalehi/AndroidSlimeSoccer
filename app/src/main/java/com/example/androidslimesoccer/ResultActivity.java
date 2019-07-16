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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultActivity extends Activity {
    boolean isWon;
    Resources resources;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = this;
        resources = getResources();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        isWon = getIntent().getBooleanExtra("IS_WON", false);
        RelativeLayout resultLinearLayout = new RelativeLayout(this);
        Bitmap background = BitmapFactory.decodeResource(resources,
        resources.getIdentifier("result_background", "drawable", getPackageName()));
        resultLinearLayout.setBackground(new BitmapDrawable(background));
        TextView resultTextView = new TextView(this);
        resultTextView.setTextSize(Utils.screenWidth / 30);
        Typeface resultTypeface = Typeface.createFromAsset(this.getAssets(),
                "fonts/Magenta.ttf");
        resultTextView.setTypeface(resultTypeface);
        resultTextView.setX(Utils.screenWidth * 5/ 40);
        resultTextView.setY(Utils.screenHeight/ 30);

        TextView mainMenuTextView = new TextView(this);
        mainMenuTextView.setText("MAIN MENU");
        mainMenuTextView.setTextSize(Utils.screenWidth / 100);
        Typeface menuTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Magenta.ttf");
        mainMenuTextView.setTypeface(menuTypeface);
        mainMenuTextView.setX(Utils.screenWidth * 29/ 40);
        mainMenuTextView.setY(Utils.screenHeight * 25 / 30);
        mainMenuTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(intent);
                return false;
            }
        });


        if (isWon) {
            resultTextView.setText("YOU WON");
        }
        else {
            resultTextView.setText("YOU LOST");
        }
        resultLinearLayout.addView(resultTextView);
        resultLinearLayout.addView(mainMenuTextView);
        setContentView(resultLinearLayout);
    }

    @Override
    public void onBackPressed(){
        this.onDestroy();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(intent);
    }

}
