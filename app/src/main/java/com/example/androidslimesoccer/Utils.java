package com.example.androidslimesoccer;

import android.content.res.Resources;

public class Utils {
    static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
    static int gravityAcceleration = -10;
    static int floorFriction = -20;
    static double assetsScale = 0.5;

    static int slimeStartX = screenWidth / 10;
    static int slimeStartY = screenHeight / 10;
    static int slimeMaxSpecialTime = 100;

}
