package com.example.androidslimesoccer.view;

import android.content.Context;
import android.view.SurfaceView;

abstract public class GameView extends SurfaceView{
    public GameView(Context context) {
        super(context);
    }
    abstract public void update();

}
