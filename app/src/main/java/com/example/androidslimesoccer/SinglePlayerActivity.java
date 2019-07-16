package com.example.androidslimesoccer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

//TODO Will Be Completed By "SINA"

public class SinglePlayerActivity extends Activity {
    SinglePlayerGameView singlePlayerGameView;
    ImageView pauseImage;
    Context context = this;
    boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String leftSlimeName = getIntent().getStringExtra("LEFT_SLIME_NAME");
        String rightSlimeName = getIntent().getStringExtra("RIGHT_SLIME_NAME");
        int goalLimit = getIntent().getIntExtra("GOAL_LIMIT", 0);
        final FrameLayout game = new FrameLayout(this);

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
        Log.i("singleplayer", "activity");
        singlePlayerGameView = new SinglePlayerGameView(this, leftSlimeName, rightSlimeName, goalLimit);
        gameWidgets.addView(pauseImage);
        game.addView(singlePlayerGameView);
        game.addView(gameWidgets);
        setContentView(game);
        Log.i("singleplayer", "activity");
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
        singlePlayerGameView.thread.setRunning(false);
        try {
            singlePlayerGameView.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onPausePressed(View v) {
        if (!singlePlayerGameView.thread.isPaused()) {
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

            final ImageView muteImage = new ImageView(this);
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
                    singlePlayerGameView.thread.setPaused(false);
                    pauseImage.setAlpha((float) 0.5);
                    alertDialog.dismiss();
                    return false;
                }
            });
            muteImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    singlePlayerGameView.muteSound();
                    return false;
                }
            });
            exitImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    singlePlayerGameView.thread.setRunning(false);
                    try {
                        singlePlayerGameView.thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    onBackPressed();
                    return false;
                }
            });
            Window window = alertDialog.getWindow();
            window.setLayout(pauseMenuWidth, pauseMenuHeight);
            v.setAlpha(1);
            singlePlayerGameView.thread.setPaused(true);
            return false;
        } else {
            singlePlayerGameView.thread.setPaused(false);
            v.setAlpha((float) 0.5);
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        singlePlayerGameView.thread.setPaused(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage("Do you want to leave the game? ");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                singlePlayerGameView.thread.setRunning(false);
                try {
                    singlePlayerGameView.thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//        super.onBackPressed();
                Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(intent);
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
