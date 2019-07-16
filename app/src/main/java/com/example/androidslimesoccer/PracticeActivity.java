package com.example.androidslimesoccer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

//TODO Will Be Completed By "EMAD"

public class PracticeActivity extends Activity {
    PracticeGameView gameView;
    Context context = this;
    ImageView pauseImage;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final FrameLayout game = new FrameLayout(this);
        String slimeName = getIntent().getStringExtra("SLIME_NAME");
        gameView = new PracticeGameView(this, slimeName);

        LinearLayout gameWidgets = new LinearLayout(this);
        pauseImage = new ImageView(this);
        Bitmap pauseBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier("pause", "drawable", this.getPackageName()));
        pauseImage.setImageBitmap(Bitmap.createScaledBitmap(pauseBitmap, Utils.screenWidth / 15,
                Utils.screenWidth / 15, true));
        pauseImage.setAlpha((float) 0.5);
        pauseImage.setX(Utils.screenWidth / 100);
        pauseImage.setY(Utils.screenHeight / 100);
        pauseImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onPausePressed(v);
            }
        });


        gameWidgets.addView(pauseImage);
        game.addView(gameView);
        game.addView(gameWidgets);
        setContentView(game);
        Log.i("slimeRatio", Integer.toString(Utils.slimeRatio));
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onPausePressed(View v) {
        if (!gameView.thread.isPaused()) {
            int pauseMenuWidth = (int) (Utils.screenWidth / 1.5);
            int pauseMenuHeight = pauseMenuWidth * 10 / 16;
            int pauseMenuIconSize = (int) (Utils.screenWidth / 6);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.pause_dialog, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            ImageView resumeImage = new ImageView(this);
            Bitmap resumeBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("pause_menu_resume", "drawable", this.getPackageName()));
            resumeImage.setImageBitmap(Bitmap.createScaledBitmap(resumeBitmap, pauseMenuIconSize,
                    pauseMenuIconSize, true));
            resumeImage.setX(Utils.screenWidth / 40);
            resumeImage.setY(Utils.screenHeight / 7);

            ImageView muteImage = new ImageView(this);
            Bitmap muteBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("pause_menu_sound_on", "drawable", this.getPackageName()));
            muteImage.setImageBitmap(Bitmap.createScaledBitmap(muteBitmap, pauseMenuIconSize,
                    pauseMenuIconSize, true));
            muteImage.setX(Utils.screenWidth / 100 + (float) (pauseMenuIconSize * 0.3));
            muteImage.setY(Utils.screenHeight / 7);

            ImageView exitImage = new ImageView(this);
            Bitmap exitBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("pause_menu_exit", "drawable", this.getPackageName()));
            exitImage.setImageBitmap(Bitmap.createScaledBitmap(exitBitmap, pauseMenuIconSize,
                    pauseMenuIconSize, true));
            exitImage.setX(Utils.screenWidth / 100 + (float) (1.35 * (pauseMenuIconSize * 0.4)));
            exitImage.setY(Utils.screenHeight / 7);
            ((LinearLayout) dialogView).addView(resumeImage);
            ((LinearLayout) dialogView).addView(muteImage);
            ((LinearLayout) dialogView).addView(exitImage);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            resumeImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gameView.thread.setPaused(false);
                    pauseImage.setAlpha((float) 0.5);
                    alertDialog.dismiss();
                    return false;
                }
            });
            exitImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gameView.thread.setRunning(false);
                    onBackPressed();
                    return false;
                }
            });
            Window window = alertDialog.getWindow();
            window.setLayout(pauseMenuWidth, pauseMenuHeight);
            v.setAlpha(1);
            gameView.thread.setPaused(true);
            return false;
        } else {
            gameView.thread.setPaused(false);
            v.setAlpha((float) 0.5);
            return false;
        }
    }

    @Override
    public void onDestroy() {
        Log.i("destroy ac","called");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.i("pause","called");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("stop","called");
        gameView.thread.setRunning(false);
        try {
            gameView.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.i("resume","called");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        gameView.thread.setRunning(false);
        try {
            gameView.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.onDestroy();
//        super.onBackPressed();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(intent);
    }
}