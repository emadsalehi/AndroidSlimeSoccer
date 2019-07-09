package com.example.androidslimesoccer;

//TODO Will Be Completed By "SINA"

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BallSprite {
    private Bitmap ballImage;
    int x, y;
    int xVelocity = 0; int yVelocity = 0;
    int firstX = Utils.ballStartX, firstY = Utils.ballStartY;

    public BallSprite(Bitmap ballImage) {
        this.ballImage = ballImage;
    }

    public void initializeFirstState () {
        x = firstX;
        y = firstY;
        xVelocity = 0;
        yVelocity = 0;
    }

    public void draw (Canvas canvas) {
        //TODO left top numbers
        canvas.drawBitmap(ballImage,x, y, null);
    }

    public void update () {
        x += xVelocity;
        y += yVelocity;
        yVelocity -= Utils.gravityAcceleration;
    }

    public Bitmap getBallImage() {
        return ballImage;
    }
}
