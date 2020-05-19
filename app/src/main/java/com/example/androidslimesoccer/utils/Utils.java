package com.example.androidslimesoccer.utils;

import android.content.res.Resources;

import java.net.InetAddress;

public class Utils {
    public static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static final int slimeStartX = screenWidth * 6 / 40;
    public static final int slimeStartY = screenHeight * 76 / 80;
    public static final int leftGoalLine = screenWidth * 3 / 40;
    public static final int rightGoalLine = screenWidth * 37 / 40;
    public static final int initialXVelocity = screenWidth / 130;
    public static final int initialYVelocity = screenHeight / 57;
    public static final int slimeMaxSpecialTime = 200;
    public static final int slowInitialIncrease = 1;
    public static final int fastInitialIncrease = 3;
    public static final int initialSpecialLevel = 200;
    public static final int leftUpBorderX = (int) (0.35f * screenWidth);
    public static final int leftSpecialButtonX = screenWidth * 7 / 40;
    public static final int leftSpecialButtonY = screenHeight / 5;
    public static final int rightSpecialButtonX = screenWidth * 33 / 40;
    public static final int rightSpecialButtonY = screenHeight / 5;
    public static final int specialButtonHalfSide = screenHeight * 3 / 40;
    public static final int targetFPS = 30;
    public static final int gameUpperBorder = 0;
    public static final int netUpperWallHeight = screenHeight * 28 / 40;
    public static final int netUpperWallWidth = screenWidth * 2 / 40;
    public static final double ballSpeedReductionFactor = 0.8;
    public static final double ballSpeedThreshold = -(double) screenHeight / 200;
    public static final int ballStartY = screenHeight * 20 / 40;
    public static final int goalLimitX = screenWidth * 19 / 40;
    public static final int goalLimitY = screenHeight * 9 / 40;
    public static final int leftGoalX = screenWidth * 10 / 40;
    public static final int rightGoalX = screenWidth * 28 / 40;
    public static final int netXVelocityIncrease = screenWidth / 200;
    public static final int serverPort = 8989;
    public static double assetsXScale = 1.0;
    public static double assetsYScale = 1.0;
    public static int gravityAcceleration = -screenHeight / 545;
    public static int floorFriction = gravityAcceleration;
    public static int ballStartX = screenWidth * 20 / 40;
    public static int ballRatio;
    public static int slimeRatio = screenWidth / 20;
    public static int slimeNumbers = SlimeType.values().length + 1;
    public static double halfCircleConverter = 0.05;
    public static int slimeJumpTime = 2 * Utils.initialYVelocity / -Utils.gravityAcceleration;
    public static InetAddress IPAddress;
}
