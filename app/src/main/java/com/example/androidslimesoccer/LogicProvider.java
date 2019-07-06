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
        int ballRatio = Utils.ballRatio;
        int slimeRatio = Utils.slimeRatio;
        int dis = distance(slimeSprite, ballSprite);

        int slimeCenterY = slimeSprite.y + slimeRatio;
        int slimeCenterX = slimeSprite.x + slimeRatio;

        int ballCenterX = ballSprite.x + ballRatio;
        int ballCenterY = ballSprite.y + ballRatio;

        int yProjection = (slimeCenterY - ballCenterY);
        int xProjection = (slimeCenterX - ballCenterX);

        if (dis < ( ballRatio + slimeRatio ) && yProjection >= 0) {
            double relativeXVelocity = ballSprite.xVelocity - slimeSprite.xVelocity;
            double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;

            double radialSpeed = (relativeXVelocity * xProjection + relativeYVelocity * yProjection) /
                    dis;
            double newSpeedX = relativeXVelocity - 2 * radialSpeed * xProjection / dis;
            double newSpeedY = relativeYVelocity - 2 * radialSpeed * yProjection / dis;
            ballSprite.xVelocity = (int)newSpeedX + slimeSprite.xVelocity;
            ballSprite.yVelocity = (int)newSpeedY + slimeSprite.yVelocity;
            ballSprite.x = slimeSprite.x - (ballRatio + slimeRatio) * xProjection / dis;
            ballSprite.y = slimeSprite.y - (ballRatio + slimeRatio) * yProjection / dis;
        }
        else if (yProjection < - ballRatio && (xProjection < (ballRatio + slimeRatio)
                && xProjection > -(ballRatio + slimeRatio))) {
            double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
            ballSprite.yVelocity = -(int)relativeYVelocity  + slimeSprite.yVelocity;
            ballSprite.y = slimeSprite.y + ballRatio;
            if (ballSprite.y >= Utils.slimeStartY + ballSprite.getBallImage().getHeight()) {
                ballSprite.y = Utils.slimeStartY + ballSprite.getBallImage().getHeight();
                ballSprite.yVelocity = 0;
                if (xProjection <= 0 ) {
                    ballSprite.x += Utils.screenWidth / 50;
                }else {
                    ballSprite.x -= Utils.screenWidth / 50;
                }
            }
        }
    }

    public void goalChecker() {

    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) - (slimeSprite.y + slimeSprite.slimeImage.getHeight())), 2)));
    }
}
