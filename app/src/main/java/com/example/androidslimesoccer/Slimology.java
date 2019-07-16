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
    TextView slimeSuper, slimeName;
    ImageView slime;
    Typeface headerFace, bodyFace;
    int[] slimeAddress = {R.drawable.runner, R.drawable.indian, R.drawable.alien, R.drawable.traffic, R.drawable.classic, R.drawable.random};
    String[] slimes = {"Runner", "Indian", "Alien", "Traffic", "Classic", "Random"};
    String[] slimeSupers = {"\tHave you ever seen \"Usain Bolt\" of slimes? Well, here it is.\n\"Runner\" sprints up and moves as fast as a Ferrari.",
            "\tThe only survivor of Indian massacres in America is here.\n\"Indian\" won't let you jump by summoning clouds and rain upon its opponent.",
            "\tEscaping from \"Area 51\" to \"Android Slime Soccer\".\n\"Alien\" teleports to where the ball is. ",
            "\tAfter retiring from \"Police department\", \"Traffic\" is here to help you.\nIt stops the ball for a few moments.",
            "\tWanna fight without hurting anyone? So, do it with empty hands.\n\"Classic\" has no weapon, more classic than this?",
            "\tFind out what the world has in its sleeve for you by choosing a random slime."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slimology);

        slimeSuper = findViewById(R.id.slime_super);
        slimeName = findViewById(R.id.slimology_name);
        slime = findViewById(R.id.slimology);

        headerFace = Typeface.createFromAsset(getAssets(),
                "fonts/Magenta.ttf");
        bodyFace = Typeface.createFromAsset(getAssets(),
                "fonts/Zekton.ttf");

        slimeName.setTypeface(headerFace);
        slimeSuper.setTypeface(bodyFace);

        slime.setImageResource(slimeAddress[i]);
        slimeName.setText(slimes[i]);
        slimeSuper.setText(slimeSupers[i]);

        slimeSuper.setGravity(Gravity.CENTER);
    }

    public void onRightArrowClick(View v) {
        i = (i + 1) % Utils.slimeNumbers;

        slimeName.setTypeface(headerFace);
        slimeSuper.setTypeface(bodyFace);

        slime.setImageResource(slimeAddress[i]);
        slimeName.setText(slimes[i]);
        slimeSuper.setText(slimeSupers[i]);

        slimeSuper.setGravity(Gravity.CENTER);
    }

    public void onLeftAArrowClick(View v) {
        i = (i + 5) % Utils.slimeNumbers;

        slimeName.setTypeface(headerFace);
        slimeSuper.setTypeface(bodyFace);

        slime.setImageResource(slimeAddress[i]);
        slimeName.setText(slimes[i]);
        slimeSuper.setText(slimeSupers[i]);

        slimeSuper.setGravity(Gravity.CENTER);
    }

    public void onBackClick(View v) {
        super.onBackPressed();
    }
}
