package com.example.androidslimesoccer;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ServerGameView extends GameView implements SurfaceHolder.Callback {

    MainThread thread;
    Context context;


    public ServerGameView(Context context, String leftSlimeName, String rightSlimeName) {
        super(context);
        this.context = context;

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
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

    @Override
    public void update() {

    }
}
