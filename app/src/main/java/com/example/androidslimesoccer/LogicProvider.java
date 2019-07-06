package com.example.androidslimesoccer;

import android.util.Log;

public class LogicProvider {
    SlimeSprite slimeSprite;
    BallSprite ballSprite;

    public LogicProvider(SlimeSprite slimeSprite, BallSprite ballSprite) {
        this.slimeSprite = slimeSprite;
        this.ballSprite = ballSprite;
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

        int slimeCenterY = slimeSprite.y + slimeSprite.slimeImage.getHeight();
        int slimeCenterX = slimeSprite.x + slimeRatio;

        int ballCenterX = ballSprite.x + ballRatio;
        int ballCenterY = ballSprite.y + ballRatio;

        int yProjection = (slimeCenterY - ballCenterY);
        int xProjection = (slimeCenterX - ballCenterX);
        if (dis < ( ballRatio + slimeRatio ) && yProjection >= 0) {
            Log.i("collision", "1");
            if (ballSprite.y == Utils.slimeStartY - ballSprite.getBallImage().getHeight())
                ballSprite.yVelocity -= Utils.screenHeight / 80;
            double relativeXVelocity = ballSprite.xVelocity - slimeSprite.xVelocity;
            double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;

            double radialSpeed = (relativeXVelocity * xProjection + relativeYVelocity * yProjection) /
                    dis;
            double newSpeedX = relativeXVelocity - 2 * radialSpeed * xProjection / dis;
            double newSpeedY = relativeYVelocity - 2 * radialSpeed * yProjection / dis;
            Log.i("newSpeedY",Double.toString(newSpeedX));
            ballSprite.xVelocity = (int)newSpeedX + slimeSprite.xVelocity;
            ballSprite.yVelocity = (int)newSpeedY + slimeSprite.yVelocity;
            Log.i("yProjection",Double.toString(yProjection));
            ballSprite.x = slimeCenterX - (ballRatio + slimeRatio) * xProjection / dis - ballRatio;
            ballSprite.y = slimeCenterY - (ballRatio + slimeRatio) * yProjection / dis - ballRatio;
        }
        else if (yProjection >= - ballRatio && (xProjection < (ballRatio + slimeRatio)
                && xProjection > -(ballRatio + slimeRatio)) && yProjection <= 0) {
            Log.i("collision", "2");
            if (ballSprite.y == Utils.slimeStartY - ballSprite.getBallImage().getHeight()) {
                slimeSprite.y = Utils.slimeStartY - ballSprite.getBallImage().getHeight()
                        - slimeSprite.slimeImage.getHeight();
                if (xProjection >= 0)
                    ballSprite.x -= Utils.screenWidth / 80;
                else
                    ballSprite.x += Utils.screenWidth / 80;

            } else {
                double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
                ballSprite.yVelocity = -(int) relativeYVelocity + slimeSprite.yVelocity;
                ballSprite.y = slimeSprite.y + ballRatio;
            }
        }
    }

    public void goalChecker() {
        if (ballSprite.x <= Utils.leftGoalLine || (ballSprite.x + 2 * Utils.ballRatio) >= Utils.rightGoalLine) {
            slimeSprite.initializeFirstState();
            ballSprite.initializeFirstState();
        }
    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) - (slimeSprite.y + slimeSprite.slimeImage.getHeight())), 2)))
                + Utils.ballRatio / 4;
    }
}
