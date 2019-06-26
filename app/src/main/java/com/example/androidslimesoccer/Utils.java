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
    static final int slimeMaxSpecialTime = 100;
    static final int slowInitialIncrease = 5;
    static final int fastInitialIncrease = 20;
    static final int initialSpecialLevel = 200;

}
