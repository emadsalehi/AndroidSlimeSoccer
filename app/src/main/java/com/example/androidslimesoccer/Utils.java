package com.example.androidslimesoccer;

import android.content.res.Resources;

import java.net.InetAddress;

public class Utils {
    static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    static double assetsXScale = 1.0;
    static double assetsYScale = 1.0;
    static final int slimeStartX = screenWidth * 6 / 40;
    static final int slimeStartY = screenHeight * 76 / 80;
    static final int leftGoalLine = screenWidth * 3 / 40;
    static final int rightGoalLine = screenWidth * 37 / 40;
    static final int initialXVelocity = screenWidth / 130;
    static final int initialYVelocity = screenHeight / 57;
    static final int gravityAcceleration = -screenHeight / 545;
    static final int floorFriction = gravityAcceleration;
    static final int slimeMaxSpecialTime = 200;
    static final int slowInitialIncrease = 1;
    static final int fastInitialIncrease = 3;
    static final int initialSpecialLevel = 200;
    static final int leftUpBorderX = (int) (0.35f * screenWidth);
    static final int leftSpecialButtonX = screenWidth * 7 / 40;
    static final int leftSpecialButtonY = screenHeight / 5;
    static final int rightSpecialButtonX = screenWidth * 33 / 40;
    static final int rightSpecialButtonY = screenHeight / 5;
    static final int specialButtonHalfSide = screenHeight * 3 / 40;
    static final int targetFPS = 30;
    static final int gameUpperBorder = 0;
    static final int netUpperWallHeight = screenHeight * 28 / 40;
    static final int netUpperWallWidth = screenWidth * 2 / 40;
    static final double ballSpeedReductionFactor = 0.8;
    static final double ballSpeedThreshold = -(double) screenHeight / 200;
    static int ballStartX = screenWidth * 20 / 40;
    static final int ballStartY = screenHeight * 20 / 40;
    static final int goalLimitX = screenWidth * 19 / 40;
    static final int goalLimitY = screenHeight * 9 / 40;
    static final int leftGoalX = screenWidth * 10 / 40;
    static final int rightGoalX = screenWidth * 28 / 40;
    static int ballRatio;
    static int slimeRatio = screenWidth / 20;
    static int slimeNumbers = SlimeType.values().length + 1;
    static double halfCircleConverter = 0.05;
    static int slimeJumpTime =  2 * Utils.initialYVelocity / -Utils.gravityAcceleration;
    static final int netXVelocityIncrease = screenWidth / 200;
    static final int serverPort = 48277;
    static InetAddress IPAddress;
}
