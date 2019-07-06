package com.example.androidslimesoccer;

public class PracticeGameLogicProvider {
    SlimeSprite slimeSprite;
    BallSprite ballSprite;

    public PracticeGameLogicProvider(SlimeSprite slimeSprite, BallSprite ballSprite) {
        this.slimeSprite = slimeSprite;
        this.ballSprite = ballSprite;
    }

    public void enableSpecial() {
        slimeSprite.enableSpecial();
    }

    public void update() {
        slimeSprite.update();
        ballSprite.update();
        if (slimeSprite.specialIsActive)
            doSpecial();
    }

    public void doSpecial() {

    }
}
