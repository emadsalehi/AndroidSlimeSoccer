package com.example.androidslimesoccer;

//TODO Will Be Completed By "SINA"

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BallSprite {
    private Bitmap ballImage;

    public BallSprite(Bitmap ballImage) {
        this.ballImage = ballImage;
    }

    public void ballDraw (Canvas canvas) {
        //TODO left top numbers
        canvas.drawBitmap(ballImage,100, 100, null);
    }

    
}
