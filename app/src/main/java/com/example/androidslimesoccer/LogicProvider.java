package com.example.androidslimesoccer;

public class LogicProvider {
    SlimeSprite slimeSprite;
    BallSprite ballSprite;

    public LogicProvider(SlimeSprite slimeSprite, BallSprite ballSprite) {
        this.slimeSprite = slimeSprite;
        this.ballSprite = ballSprite;
    }

    public void enableSpecial() {
        slimeSprite.enableSpecial();
    }

    public void update() {
        slimeSprite.update();
        ballSprite.update();
        slimeAndBallCollisionChecker();
        if (slimeSprite.specialIsActive)
            doSpecial();
        goalChecker();
    }

    public void doSpecial() {

    }

    public void slimeAndBallCollisionChecker() {

    }

    public void goalChecker() {

    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) - (slimeSprite.y - slimeSprite.slimeImage.getHeight())), 2)));
    }
}
