package com.example.androidslimesoccer;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

//TODO Will Be Completed By "EMAD"

public class PracticeGameView extends SurfaceView implements SurfaceHolder.Callback {

    public PracticeGameView(Context context) {
        super(context);

        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
