package com.example.androidslimesoccer;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Slimology extends Activity {

    int i = 0;
    TextView slimeSuper;
    TextView slimeName;
    ImageView slime;
    String[] slimes = {"Runner", "Indian", "Alien", "Traffic", "Classic", "Random"};
    String[] slimeSupers = {"\tHave you ever seen an \"Usain Bolt\" slime? Well, here it is.\n\"Runner\" sprints up and moves as fast as a Ferrari.",
            "\tThe only survivor of Indian massacres in America is here.\n\"Indian\" won't let you jump by summoning clouds and rain upon his opponent.",
            "\tIt has come from the planet \"Of Apes\".\n\"Alien\" teleports to where the ball is. ",
            "\t",
            "\tWant to fight without hurting anyone? Fighting with empty hands is the solution.\n\"Classic\" has no weapon, more classic than this?",
            "\tFind out what the world has in its sleeve for you by choosing a random slime."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slimology);
        slimeSuper = findViewById(R.id.slime_super);
        slimeName = findViewById(R.id.slimology_name);
        slime = findViewById(R.id.slimology);
        slime.setImageResource(R.drawable.runner);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        slimeName.setTypeface(face);
        slimeSuper.setTypeface(face);
        slimeName.setText(slimes[i]);
        slimeSuper.setText(slimeSupers[i]);
        slimeSuper.setGravity(Gravity.CENTER);
    }

    public void onRightArrowClick(View v) {
        slimeSuper = findViewById(R.id.slime_super);
        slimeName = findViewById(R.id.slimology_name);
        slime = findViewById(R.id.slimology);
        i = (i + 1) % 6;
        switch (i) {
            case 0:
                slime.setImageResource(R.drawable.runner);
                break;
            case 1:
                slime.setImageResource(R.drawable.indian);
                break;
            case 2:
                slime.setImageResource(R.drawable.alien);
                break;
            case 3:
                slime.setImageResource(R.drawable.traffic);
                break;
            case 4:
                slime.setImageResource(R.drawable.classic);
                break;
            case 5:
                slime.setImageResource(R.drawable.random);
                break;
        }
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        slimeName.setTypeface(face);
        slimeSuper.setTypeface(face);
        slimeName.setText(slimes[i]);
        slimeSuper.setText(slimeSupers[i]);
        slimeSuper.setGravity(Gravity.CENTER);
    }

    public void onLeftAArrowClick(View v) {
        slimeSuper = findViewById(R.id.slime_super);
        slimeName = findViewById(R.id.slimology_name);
        slime = findViewById(R.id.slimology);
        i = (i + 5) % 6;
        switch (i) {
            case 0:
                slime.setImageResource(R.drawable.runner);
                break;
            case 1:
                slime.setImageResource(R.drawable.indian);
                break;
            case 2:
                slime.setImageResource(R.drawable.alien);
                break;
            case 3:
                slime.setImageResource(R.drawable.traffic);
                break;
            case 4:
                slime.setImageResource(R.drawable.classic);
                break;
            case 5:
                slime.setImageResource(R.drawable.random);
                break;
        }
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        slimeName.setTypeface(face);
        slimeSuper.setTypeface(face);
        slimeName.setText(slimes[i]);
        slimeSuper.setText(slimeSupers[i]);
        slimeSuper.setGravity(Gravity.CENTER);
    }
}
