package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.*;

public class AttributeSelectActivity extends Activity {

    MediaPlayer mediaPlayer;
    Intent singlePlayerIntent;
    Typeface typeface;
    TextView goalLimit, goalNumber, selectField, play, difficulty, easy, hard;
    ImageView selectArrow;
    int field = 0;
    int goal = 5;
    String leftSlimeName, rightSlimeName, aiDifficulty;
    Boolean isPaused;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attribute_select);
        aiDifficulty = String.valueOf(R.string.easy);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        leftSlimeName = getIntent().getStringExtra("LEFT_SLIME_NAME");
        rightSlimeName = getIntent().getStringExtra("RIGHT_SLIME_NAME");
        setSelectField();
        setPlay();
        setGoalAttributes();
        setDifficulty();
        setEasy();
        setHard();
        isPaused = getIntent().getBooleanExtra("isPaused", false);
    }

    public void onBackClick(View v) {
        if (field == 0) {
            onBackPressed();
        } else {
            field = 0;
            selectArrow.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mediaPlayer = MediaPlayer.create(this, R.raw.practice_song);
        mediaPlayer.setLooping(true);
        if (!isPaused) {
            mediaPlayer.start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    public void onArrowUpClick(View v) {
        goal = (goal < 10) ? goal + 1 : goal;
        goalNumber.setTypeface(typeface);
        goalNumber.setText(String.valueOf(goal));
    }

    public void onArrowDownClick(View v) {
        goal = (goal > 1) ? goal - 1 : goal;
        goalNumber.setTypeface(typeface);
        goalNumber.setText(String.valueOf(goal));
    }

    public void onFieldClick(View v) {
        findViewById(R.id.arrow_down).setVisibility(VISIBLE);
        findViewById(R.id.arrow_up).setVisibility(VISIBLE);
        findViewById(R.id.goal_number).setVisibility(VISIBLE);
        findViewById(R.id.goal_limit).setVisibility(VISIBLE);
        findViewById(R.id.difficulty).setVisibility(VISIBLE);
        findViewById(R.id.easy).setVisibility(VISIBLE);
        findViewById(R.id.hard).setVisibility(VISIBLE);
        selectField.setVisibility(INVISIBLE);
        if (field != 0) {
            selectArrow.setVisibility(INVISIBLE);
        }
        if (v.getTag().equals("soccer")) {
            field = R.drawable.bg_soccerfield;
            selectArrow = findViewById(R.id.arrow_upleft);
            selectArrow.setVisibility(VISIBLE);
        } else if (v.getTag().equals("moon")) {
            field = R.drawable.bg_moon;
            selectArrow = findViewById(R.id.arrow_upright);
            selectArrow.setVisibility(VISIBLE);
        } else if (v.getTag().equals("desert")) {
            field = R.drawable.bg_desert;
            selectArrow = findViewById(R.id.arrow_downleft);
            selectArrow.setVisibility(VISIBLE);
        } else if (v.getTag().equals("winter")) {
            field = R.drawable.bg_winter;
            selectArrow = findViewById(R.id.arrow_downright);
            selectArrow.setVisibility(VISIBLE);
        }
    }

    public void onPlayClick(View v) {
        if (field == 0) {
            findViewById(R.id.arrow_down).setVisibility(INVISIBLE);
            findViewById(R.id.arrow_up).setVisibility(INVISIBLE);
            findViewById(R.id.goal_number).setVisibility(INVISIBLE);
            findViewById(R.id.goal_limit).setVisibility(INVISIBLE);
            findViewById(R.id.difficulty).setVisibility(INVISIBLE);
            findViewById(R.id.easy).setVisibility(INVISIBLE);
            findViewById(R.id.hard).setVisibility(INVISIBLE);
            selectField.setVisibility(VISIBLE);
        } else {
            singlePlayerIntent = new Intent(this, SinglePlayerActivity.class);
            singlePlayerIntent.putExtra("LEFT_SLIME_NAME", leftSlimeName);
            singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", rightSlimeName);
            singlePlayerIntent.putExtra("GOAL_LIMIT", goal);
            singlePlayerIntent.putExtra("isPaused", isPaused);
            singlePlayerIntent.putExtra("FIELD", field);
            singlePlayerIntent.putExtra("DIFFICULTY", aiDifficulty);
            startActivity(singlePlayerIntent);
        }
    }

    public void onDifficultyClick(View v) {
        easy = findViewById(R.id.easy);
        hard = findViewById(R.id.hard);
        if ("Easy".equals(v.getTag())) {
            easy.setAlpha((float) 1.0);
            easy.setScaleX((float) 1.5);
            easy.setScaleY((float) 1.5);
            hard.setAlpha((float) 0.5);
            hard.setScaleX((float) 1.0);
            hard.setScaleY((float) 1.0);
            aiDifficulty = String.valueOf(R.string.easy);
        } else {
            easy.setAlpha((float) 0.5);
            easy.setScaleX((float) 1.0);
            easy.setScaleY((float) 1.0);
            hard.setAlpha((float) 1.0);
            hard.setScaleX((float) 1.5);
            hard.setScaleY((float) 1.5);
            aiDifficulty = String.valueOf(R.string.hard);
        }
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    public void setPlay() {
        play = findViewById(R.id.play);
        play.setTypeface(typeface);
        play.setText(R.string.play);
        play.setTextSize(28);
        play.setTextColor(Color.WHITE);
    }

    public void setSelectField() {
        selectField = findViewById(R.id.select_field);
        selectField.setTypeface(typeface);
        selectField.setText(R.string.select_field);
        selectField.setTextSize(24);
        selectField.setTextColor(Color.WHITE);
        selectField.setVisibility(INVISIBLE);
    }

    public void setGoalAttributes() {
        goalLimit = findViewById(R.id.goal_limit);
        goalLimit.setTypeface(typeface);
        goalLimit.setText(R.string.goal_limit);
        goalLimit.setTextSize(16);
        goalLimit.setTextColor(Color.WHITE);
        goalNumber = findViewById(R.id.goal_number);
        goalNumber.setTypeface(typeface);
        goalNumber.setText(String.valueOf(goal));
        goalNumber.setTextSize(30);
        goalNumber.setTextColor(Color.WHITE);
    }

    public void setDifficulty() {
        difficulty = findViewById(R.id.difficulty);
        difficulty.setTypeface(typeface);
        difficulty.setText(R.string.difficulty);
        difficulty.setTextSize(16);
        difficulty.setTextColor(Color.WHITE);
    }

    public void setEasy() {
        easy = findViewById(R.id.easy);
        easy.setTypeface(typeface);
        easy.setText(R.string.easy);
        easy.setTextSize(20);
        easy.setTextColor(Color.WHITE);
        easy.setScaleX((float) 1.5);
        easy.setScaleY((float) 1.5);
    }

    public void setHard() {
        hard = findViewById(R.id.hard);
        hard.setTypeface(typeface);
        hard.setText(R.string.hard);
        hard.setTextSize(20);
        hard.setTextColor(Color.WHITE);
        hard.setAlpha((float) 0.5);
    }
}
