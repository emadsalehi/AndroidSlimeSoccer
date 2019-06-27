package com.example.androidslimesoccer;


import android.graphics.Color;

public enum SlimeType {
    CLASSIC(300, 20, true, Color.RED),
    INDIAN(400, 400, true, Color.rgb(130, 77, 7)),
    ALIEN(600, 500, true, Color.GREEN),
    TRAFIC(400, 200, true, Color.GRAY),
    RUNNER(100, 20, false, Color.rgb(71, 161, 11));


    private final int specialThreshold;
    private final int enableDecrease;
    private final boolean isImmediate;
    private final int color;

    SlimeType(int specialThreshold, int enableDecrease, boolean isImmediate, int color) {
        this.specialThreshold = specialThreshold;
        this.enableDecrease = enableDecrease;
        this.isImmediate = isImmediate;
        this.color = color;
    }

    public int getSpecialThreshold() {
        return specialThreshold;
    }

    public int getEnableDecrease() {
        return enableDecrease;
    }

    public boolean isImmediate() {
        return isImmediate;
    }

    public int getColor() {
        return color;
    }
}
