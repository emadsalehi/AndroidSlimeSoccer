package com.example.androidslimesoccer;

public enum SlimeType {
    CLASSIC(300, 20, true),
    INDIAN(400, 400, false),
    ALIEN(600, 500, true),
    TRAFIC(400, 200, true);

    private final int specialThreshold;
    private final int enableDecrease;
    private final boolean isImmediate;

    SlimeType(int specialThreshold, int enableDecrease, boolean isImmediate) {
        this.specialThreshold = specialThreshold;
        this.enableDecrease = enableDecrease;
        this.isImmediate = isImmediate;
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
}
