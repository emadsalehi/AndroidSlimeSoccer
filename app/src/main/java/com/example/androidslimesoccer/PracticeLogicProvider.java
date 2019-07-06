package com.example.androidslimesoccer;

public class PracticeLogicProvider {
    SlimeSprite slimeSprite;
    BallSprite ballSprite;

    public PracticeLogicProvider(SlimeSprite slimeSprite, BallSprite ballSprite) {
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

    public void slimeAndBallCollisionChecker() {

    }

    public void goalChecker() {

    }
}
