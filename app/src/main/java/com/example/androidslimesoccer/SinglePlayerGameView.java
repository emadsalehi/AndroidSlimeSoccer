package com.example.androidslimesoccer;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

//TODO Will Be Completed By "SINA"

public class SinglePlayerGameView extends SurfaceView implements SurfaceHolder.Callback {


    public SinglePlayerGameView(Context context) {
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
