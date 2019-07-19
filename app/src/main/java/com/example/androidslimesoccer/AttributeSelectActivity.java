package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.*;

public class AttributeSelectActivity extends Activity {

    Intent singlePlayerIntent;
    Typeface typeface;
    TextView goalLimit, goalNumber, selectField;
    ImageView selectArrow;
    int field = 0;
    int goal = 5;
    String leftSlimeName;
    String rightSlimeName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attribute_select);
        leftSlimeName = getIntent().getStringExtra("LEFT_SLIME_NAME");
        rightSlimeName = getIntent().getStringExtra("RIGHT_SLIME_NAME");
        selectField = findViewById(R.id.select_field);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        selectField.setTypeface(typeface);
        selectField.setText("Please choose a field");
        selectField.setVisibility(INVISIBLE);
        goalLimit = findViewById(R.id.goal_limit);
        goalNumber = findViewById(R.id.goal_number);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        goalLimit.setTypeface(typeface);
        goalLimit.setText("Goal Limit");
        goalNumber.setTypeface(typeface);
        goalNumber.setText(Integer.toString(goal));
    }

    public void onBackClick(View v) {
        if (field == 0) {
            super.onBackPressed();
        } else {
            field = 0;
            selectArrow.setVisibility(INVISIBLE);
        }
    }

    public void onArrowUpClick(View v) {
        goal = (goal < 10) ? goal + 1 : goal;
        goalNumber.setTypeface(typeface);
        goalNumber.setText(Integer.toString(goal));
    }

    public void onArrowDownClick(View v) {
        goal = (goal > 1) ? goal - 1 : goal;
        goalNumber.setTypeface(typeface);
        goalNumber.setText(Integer.toString(goal));
    }

    public void onFieldClick(View v) {
        findViewById(R.id.arrow_down).setVisibility(VISIBLE);
        findViewById(R.id.arrow_up).setVisibility(VISIBLE);
        findViewById(R.id.goal_number).setVisibility(VISIBLE);
        findViewById(R.id.goal_limit).setVisibility(VISIBLE);
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
            selectField.setVisibility(VISIBLE);
        } else {
            singlePlayerIntent = new Intent(this, SinglePlayerActivity.class);
            singlePlayerIntent.putExtra("LEFT_SLIME_NAME", leftSlimeName);
            singlePlayerIntent.putExtra("RIGHT_SLIME_NAME", rightSlimeName);
            singlePlayerIntent.putExtra("GOAL_LIMIT", goal);
            singlePlayerIntent.putExtra("FIELD", field);
            startActivity(singlePlayerIntent);
        }
    }

    public Integer getField() {
        return field;
    }
}
