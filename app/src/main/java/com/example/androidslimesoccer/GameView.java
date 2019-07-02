package com.example.androidslimesoccer;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

abstract public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public GameView(Context context) {
        super(context);
    }
    abstract public void update();

}
