package com.example.androidslimesoccer;

//TODO Will Be Completed By "SINA"

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

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
    }

    public void draw (Canvas canvas) {
        //TODO left top numbers
        canvas.drawBitmap(ballImage,x, y, null);
    }

    public void update () {
        x += xVelocity;
        y += yVelocity;
        yVelocity -= Utils.gravityAcceleration;
        if (y >= (Utils.slimeStartY - ballImage.getHeight())) {
            y = Utils.slimeStartY - ballImage.getHeight();
            yVelocity = (int) ((double)(-yVelocity) * Utils.ballSpeedReductionFactor);
        }
        if (x <= Utils.leftGoalLine) {
            x = Utils.leftGoalLine;
            xVelocity = (int) ((double)(-xVelocity) * Utils.ballSpeedReductionFactor);
        }
        if (x >= (Utils.rightGoalLine - ballImage.getWidth())) {
            x = Utils.rightGoalLine - ballImage.getWidth();
            xVelocity = (int) ((double)(-xVelocity) * Utils.ballSpeedReductionFactor);
        }
        if (y < Utils.gameUpperBorder) {
            y = Utils.gameUpperBorder;
            yVelocity = (int) ((double)(-yVelocity) * Utils.ballSpeedReductionFactor);
        }
        if ((y > Utils.netUpperWallHeight && y < Utils.netUpperWallHeight + ballImage.getWidth())
                && (x < Utils.leftGoalLine + Utils.netUpperWallWidth || x > (Utils.rightGoalLine - Utils.netUpperWallWidth)) ) {
            y = Utils.netUpperWallHeight;
            yVelocity = (int) ((double)(-yVelocity) * Utils.ballSpeedReductionFactor);
        }

        if ( yVelocity <= 0 && yVelocity >= Utils.ballSpeedThreshold )  {
            yVelocity = 0;
        }
//        Log.i("speed", Integer.toString(yVelocity));
    }

    public Bitmap getBallImage() {
        return ballImage;
    }
}
