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
        xVelocity = 0;
        yVelocity = 0;
        x = firstX;
        y = firstY;
    }

    public void Draw (Canvas canvas) {
        //TODO left top numbers
        canvas.drawBitmap(ballImage,100, 100, null);
    }

    public void update () {
        x += xVelocity;
        y += yVelocity;
        yVelocity -= Utils.gravityAcceleration;
        if (y >= Utils.slimeStartY) {
            y = Utils.slimeStartY;
            yVelocity = (int) ((double)(-yVelocity) * Utils.ballspeedReductionFactor);
        }
        if (x <= Utils.leftGoalLine) {
            x = Utils.leftGoalLine;
            xVelocity = (int) ((double)(-xVelocity) * Utils.ballspeedReductionFactor);
        }
        if (x >= (Utils.rightGoalLine - ballImage.getWidth())) {
            x = Utils.rightGoalLine - ballImage.getWidth();
            xVelocity = (int) ((double)(-xVelocity) * Utils.ballspeedReductionFactor);
        }
        if (y < Utils.gameUpperborder) {
            y = Utils.gameUpperborder;
            yVelocity = (int) ((double)(-yVelocity) * Utils.ballspeedReductionFactor);
        }
        if ((y > Utils.netUpperWallHeight && y < Utils.netUpperWallHeight + ballImage.getWidth())
                && (x < Utils.leftGoalLine + Utils.netUpperWallWidth || x > (Utils.rightGoalLine - Utils.netUpperWallWidth)) ) {
            y = Utils.netUpperWallHeight;
            yVelocity = (int) ((double)(-yVelocity) * Utils.ballspeedReductionFactor);
        }

    }
    
}
