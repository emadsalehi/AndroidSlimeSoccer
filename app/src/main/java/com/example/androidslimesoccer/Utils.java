package com.example.androidslimesoccer;

import android.content.res.Resources;

public class Utils {
    static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static final int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
    static double assetsXScale = 1.0;
    static double assetsYScale = 1.0;
    static final int slimeStartX = screenWidth * 6 / 40;
    static final int slimeStartY = screenHeight * 76 / 80;
    static final int leftGoalLine = screenWidth * 3 / 40;
    static final int rightGoalLine = screenWidth * 37 / 40;
    static final int initialXVelocity = screenWidth / 100;
    static final int initialYVelocity = screenHeight / 35;
    static final int gravityAcceleration = -initialYVelocity * initialYVelocity / 280;
    static final int floorFriction = gravityAcceleration / 2;
    static final int slimeMaxSpecialTime = 100;
    static final int slowInitialIncrease = 1;
    static final int fastInitialIncrease = 3;
    static final int initialSpecialLevel = 200;
    static final int leftRightBorderX = (int)(0.3f * screenWidth);
    static final int rightUpBorderX = (int)(0.5f * screenWidth);
    static final int leftSpecialButtonX = screenWidth * 7 / 40;
    static final int leftSpecialButtonY = screenHeight / 5;
    static final int rightSpecialButtonX = screenWidth * 33 / 40;
    static final int rightSpecialButtonY = screenHeight / 5;
    static final int specialButtonHalfSide = screenHeight * 3 / 40;
    static final int targetFPS = 30;
    static final int gameUpperBorder = 0;
    static final int netUpperWallHeight = screenHeight * 30 / 40;
    static final int netUpperWallWidth = 0;
    static final double ballSpeedReductionFactor = 0.6;
    static final double ballSpeedThreshold = -10;
    static int ballStartX = screenWidth * 20 / 40;
    static final int ballStartY = screenHeight * 20 / 40;
    static final int goalLimitX = screenWidth * 19 / 40;
    static final int goalLimitY = screenHeight * 10 / 40;
    static final int leftGoalX = screenWidth * 10 / 40;
    static final int rightGoalX = screenWidth * 28 / 40;

    static int ballRatio;
    static int slimeRatio;
}
