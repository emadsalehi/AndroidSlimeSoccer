package com.example.androidslimesoccer;

import android.content.res.Resources;

public class Utils {
    static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static final int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
    static final int gravityAcceleration = -2;
    static final int floorFriction = -1;
    static double assetsXScale = 1.0;
    static double assetsYScale = 1.0;
    static final int slimeStartX = screenWidth * 6 / 40;
    static final int slimeStartY = screenHeight * 35 / 40;
    static final int leftGoalLine = screenWidth * 3 / 40;
    static final int rightGoalLine = screenWidth * 37 / 40;
    static final int initialXVelocity = screenWidth / 100;
    static final int initialYVelocity = screenWidth / 74;
    static final int slimeMaxSpecialTime = 100;
    static final int slowInitialIncrease = 1;
    static final int fastInitialIncrease = 3;
    static final int initialSpecialLevel = 200;
    static final int leftRightBorderX = (int)(0.3f * screenWidth);
    static final int rightUpBorderX = (int)(0.5f * screenWidth);
    static final int leftSpecialButtonX = screenWidth * 7 / 40;
    static final int leftSpecialButtonY = screenHeight / 5;
    static final int rightSpecialButtonX = screenWidth * 33 / 40;
    static final int rightSpecialButtonY = screenWidth / 5;
    static final int specialButtonHalfSide = screenHeight * 3 / 40;
    static final int targetFPS = 30;
}
