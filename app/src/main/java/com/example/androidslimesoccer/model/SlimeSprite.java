package com.example.androidslimesoccer.model;

//TODO Will Be Completed By "EMAD"

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.androidslimesoccer.utils.SlimeType;
import com.example.androidslimesoccer.utils.Utils;

public class SlimeSprite {

    private int screenWidth = Utils.screenWidth;
    private int screenHeight = Utils.screenHeight;
    public SlimeType slimeType;
    public Bitmap slimeImage;
    public boolean specialIsActive = false, specialButtonIsHold = false;
    public boolean isLookRight;
    public boolean isMoveRight = false, isMoveLeft = false;
    public int specialCountDown = 0;
    public int x, y;
    public int xVelocity = 0;
    public int yVelocity = 0;
    public int firstY = Utils.slimeStartY;
    public int specialLevel = Utils.initialSpecialLevel;
    private boolean isFirstPlayer;
    private int specialMaxTime = Utils.slimeMaxSpecialTime;
    private int firstX = Utils.slimeStartX;

    public SlimeSprite(SlimeType slimeType, Bitmap slimeImage, boolean isFirstPlayer) {
        this.slimeType = slimeType;
        this.slimeImage = slimeImage;
        this.isFirstPlayer = isFirstPlayer;
        firstY -= slimeImage.getHeight();
        isLookRight = isFirstPlayer;
        if (!isLookRight) {
            this.slimeImage = flipBitmap(slimeImage);
        }
    }

    public void initializeFirstState() {
        if (isFirstPlayer) {
            x = firstX;
            y = firstY;
            if (!isLookRight) {
                isLookRight = true;
                slimeImage = flipBitmap(slimeImage);
            }
        } else {
            x = screenWidth - firstX - slimeImage.getWidth();
            y = firstY;
            if (isLookRight) {
                isLookRight = false;
                slimeImage = flipBitmap(slimeImage);
            }
        }
    }

    public void enableSpecial() {
        if (specialLevel > slimeType.getSpecialThreshold()) {
            specialIsActive = true;
            if (slimeType.isImmediate()) {
                specialLevel -= slimeType.getEnableDecrease();
                specialCountDown = specialMaxTime;
            } else {
                specialCountDown = 1;
            }
        }
    }

    public void draw(Canvas canvas) {
        if (isFirstPlayer) {
            canvas.drawBitmap(slimeImage, x, y, null);
        } else {
            float[] colorTransform = {
                    0.8f, 0, 0, 0, 0,
                    0, 0.8f, 0, 0, 0,
                    0, 0, 0.8f, 0, 0,
                    0, 0, 0, 1f, 0};

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0f); //Remove Colour
            colorMatrix.set(colorTransform); //Apply the Red

            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
            Paint paint = new Paint();
            paint.setColorFilter(colorFilter);
            canvas.drawBitmap(slimeImage, x, y, paint);

        }
    }

    public void update() {
        if (isMoveLeft) {
            x -= Utils.initialXVelocity;
            xVelocity = -Utils.initialXVelocity;
        } else if (isMoveRight) {
            x += Utils.initialXVelocity;
            xVelocity = Utils.initialXVelocity;
        } else {
            xVelocity = 0;
        }
        y += yVelocity;
        yVelocity -= Utils.gravityAcceleration;
        if (specialIsActive) {
            specialCountDown--;
            if (!slimeType.isImmediate() && specialButtonIsHold) {
                specialLevel -= slimeType.getEnableDecrease();
                if (specialLevel >= slimeType.getSpecialThreshold())
                    specialCountDown++;
            }
            if (specialCountDown == 0)
                specialIsActive = false;
        }
        if (isFirstPlayer) {
            if (x > screenWidth / 2)
                specialLevel += Utils.fastInitialIncrease;
            else
                specialLevel += Utils.slowInitialIncrease;
        } else {
            if (x < screenWidth / 2)
                specialLevel += Utils.fastInitialIncrease;
            else
                specialLevel += Utils.slowInitialIncrease;
        }

        if (specialLevel > 1000)
            specialLevel = 1000;
    }

    public Bitmap flipBitmap(Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

}
