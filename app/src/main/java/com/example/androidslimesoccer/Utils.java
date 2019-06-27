package com.example.androidslimesoccer;

import android.content.res.Resources;

public class Utils {
    static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static final int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
    static final int gravityAcceleration = -10;
    static final int floorFriction = -20;
    static final double assetsScale = 0.5;
    static final int slimeStartX = screenWidth / 10;
    static final int slimeStartY = screenHeight / 10;
    static final int leftGoalLine = screenWidth / 20;
    static final int rightGoalLine = screenWidth * 19 / 20;
    static final int initialXVelocity = screenWidth / 100;
    static final int initialYVelocity = screenWidth / 50;
    static final int slimeMaxSpecialTime = 100;
    static final int slowInitialIncrease = 5;
    static final int fastInitialIncrease = 20;
    static final int initialSpecialLevel = 200;
    static final int leftRightBorderX = (int)(0.3f * screenWidth);
    static final int rightUpBorderX = (int)(0.5f * screenWidth);
    static final int leftSpecialButtonX = screenWidth / 10;
    static final int leftSpecialButtonY = screenWidth / 5;
    static final int rightSpecialButtonX = screenWidth * 9 / 10;
    static final int rightSpecialButtonY = screenWidth / 5;
    static final int specialButtonHalfSide = screenHeight / 10;
}
