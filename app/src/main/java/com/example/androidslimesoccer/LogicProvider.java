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
        if (dis < (ballRatio + slimeSprite.slimeImage.getHeight()) && yProjection >= 0) {
            Log.i("collision", "1");
            double relativeXVelocity = ballSprite.xVelocity - slimeSprite.xVelocity;
            double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
            double totalVelocity = Math.sqrt(Math.pow(relativeXVelocity, 2) + Math.pow(relativeYVelocity, 2));

            double theta = Math.atan2(yProjection, -xProjection);
            double alpha = Math.atan2(relativeXVelocity, relativeYVelocity);
            double finalAngle = 2 * theta - alpha - Math.PI / 2;

            ballSprite.xVelocity = (int) (slimeSprite.xVelocity / 4 + totalVelocity * Math.cos(finalAngle));
            ballSprite.yVelocity = (int) (slimeSprite.yVelocity / 4 - totalVelocity * Math.sin(finalAngle));
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
        if ((ballSprite.x <= Utils.leftGoalLine || (ballSprite.x + 2 * Utils.ballRatio) >= Utils.rightGoalLine)
                && (Utils.goalLimitY < ballSprite.y)) {
            slimeSprite.initializeFirstState();
            ballSprite.initializeFirstState();
        }
    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) - (slimeSprite.y + slimeSprite.slimeImage.getHeight())), 2))
                + Utils.ballRatio / 3);
    }
}
