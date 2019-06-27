package com.example.androidslimesoccer;

//TODO Will Be Completed By "EMAD"

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SlimeSprite {

    private int screenWidth = Utils.screenWidth;
    private int screenHeight = Utils.screenHeight;
    SlimeType slimeType;
    Bitmap slimeImage;
    boolean specialIsActive = false;
    boolean isFirstPlayer;
    boolean isLookRight;
    int specialMaxTime = Utils.slimeMaxSpecialTime;
    int specialCountDown = 0;
    int x, y;
    int xVelocity = 0; int yVelocity = 0;
    int firstX = Utils.slimeStartX, firstY = Utils.slimeStartY;
    int specialLevel = Utils.initialSpecialLevel;

    public SlimeSprite(SlimeType slimeType, Bitmap slimeImage, boolean isFirstPlayer) {
        this.slimeType = slimeType;
        this.slimeImage = slimeImage;
        this.isFirstPlayer = isFirstPlayer;
    }

    public void initializeFirstState() {
        if(isFirstPlayer) {
            x = firstX;
            y = firstY;
            isLookRight = true;
        } else {
            x = screenWidth - firstX - slimeImage.getWidth();
            y = firstY;
            isLookRight = false;
        }
    }

    public void enableSpecial() {
        if (specialLevel > slimeType.getSpecialThreshold()) {
            specialIsActive = true;
            specialCountDown = specialMaxTime;
            specialLevel -= slimeType.getEnableDecrease();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(slimeImage,x, y, null);
    }

    public void update() {
        x += xVelocity;
        y += yVelocity;
        yVelocity -= Utils.gravityAcceleration;
        if (x < Utils.leftGoalLine)
            x = Utils.leftGoalLine;
        if (x > (Utils.rightGoalLine - slimeImage.getWidth()))
            x = Utils.rightGoalLine - slimeImage.getWidth();
        if (y > Utils.slimeStartY) {
            y = Utils.slimeStartY;
            yVelocity = 0;
        }
        if (specialIsActive) {
            specialCountDown--;
            if (specialCountDown == 0)
                specialIsActive = false;
        }
        if (x > screenWidth / 2)
            specialLevel += Utils.fastInitialIncrease;
        else
            specialLevel += Utils.slowInitialIncrease;

        if (specialLevel > 1000)
            specialLevel = 1000;
    }
}
