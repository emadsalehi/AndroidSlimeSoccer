package com.example.androidslimesoccer;

import android.util.Log;

public class PracticeLogicProvider {
    SlimeSprite slimeSprite;
    BallSprite ballSprite;

    public PracticeLogicProvider(SlimeSprite slimeSprite, BallSprite ballSprite) {
        this.slimeSprite = slimeSprite;
        this.ballSprite = ballSprite;
    }

    public void update() {
        slimeSprite.update();
        slimeAndWallCollisionChecker();
        slimeAndBallCollisionChecker();
        ballSprite.update();
        ballAndWallCollisionChecker();
        if (slimeSprite.specialIsActive) {
            doSpecial();
        }
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
        out: if (dis < (ballRatio + (Utils.halfCircleConverter + 1) * slimeRatio) && yProjection >= 0) {
            Log.i("collision", "1");
            if (ballSprite.yVelocity < 0 && yProjection <= ballRatio) {
                break out;
            }
            yProjection += Utils.halfCircleConverter * slimeRatio;
            double relativeXVelocity = ballSprite.xVelocity - slimeSprite.xVelocity;
            double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
            double totalVelocity = Math.sqrt(Math.pow(relativeXVelocity, 2) + Math.pow(relativeYVelocity, 2)) * (0.95);

            double theta = Math.atan2(yProjection, -xProjection);
            double alpha = Math.atan2(relativeXVelocity, relativeYVelocity);
            double finalAngle = 2 * theta - alpha - Math.PI / 2;

            ballSprite.xVelocity = (int) (slimeSprite.xVelocity * 9 / 10 + totalVelocity * Math.cos(finalAngle));
            ballSprite.yVelocity = (int) (slimeSprite.yVelocity * 9 / 10 - totalVelocity * Math.sin(finalAngle));

            if (ballSprite.y >= Utils.slimeStartY - 2 * ballRatio - 1) {
                ballSprite.yVelocity += 5 * Utils.gravityAcceleration;
                ballSprite.xVelocity += Utils.gravityAcceleration;
            }

            ballSprite.y = slimeCenterY - (ballRatio + slimeRatio) * yProjection / dis - ballRatio;
            ballSprite.x = slimeCenterX - (ballRatio + slimeRatio) * xProjection / dis - ballRatio;
        }
        else if (yProjection > - ballRatio && (xProjection <= (ballRatio / 2 + slimeRatio)
                && xProjection >= -(ballRatio / 2 + slimeRatio)) && yProjection < 0
                && ballSprite.y <= Utils.slimeStartY - 2 * ballRatio) {
            if (ballSprite.y == Utils.slimeStartY - ballSprite.getBallImage().getHeight()) {
                slimeSprite.y = Utils.slimeStartY - ballSprite.getBallImage().getHeight()
                        - slimeSprite.slimeImage.getHeight();
                slimeSprite.yVelocity *= 0.8;
                if (xProjection >= 0)
                    ballSprite.xVelocity -= Utils.screenWidth / 200;
                else
                    ballSprite.xVelocity += Utils.screenWidth / 200;
                Log.i("collision", "21");
            } else {
                Log.i("collision", "22");
                double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
                ballSprite.yVelocity = ((int) -relativeYVelocity + slimeSprite.yVelocity / 2);
                ballSprite.xVelocity += slimeSprite.xVelocity * 2;
                ballSprite.y =(slimeSprite.y + slimeSprite.slimeImage.getHeight() + 1);
            }
        }
    }

    public void ballAndWallCollisionChecker() {
        if (ballSprite.y >= (Utils.slimeStartY - ballSprite.getBallImage().getHeight())) {
            ballSprite.y = Utils.slimeStartY - ballSprite.getBallImage().getHeight();
            ballSprite.yVelocity = (int) ((double)(-ballSprite.yVelocity) * Utils.ballSpeedReductionFactor);
        }
        if (ballSprite.x < Utils.leftGoalLine &&
                (Utils.netUpperWallHeight + 2 * Utils.ballRatio >= ballSprite.y)) {
            ballSprite.x = Utils.leftGoalLine;
            ballSprite.xVelocity = (int) ((double)(-ballSprite.xVelocity) * Utils.ballSpeedReductionFactor);
        }
        if (ballSprite.x > (Utils.rightGoalLine - ballSprite.getBallImage().getWidth()) &&
                (Utils.netUpperWallHeight + 2 * Utils.ballRatio >= ballSprite.y)) {
            ballSprite.x = Utils.rightGoalLine - ballSprite.getBallImage().getWidth();
            ballSprite.xVelocity = (int) ((double)(-ballSprite.xVelocity) * Utils.ballSpeedReductionFactor);
        }
        if (ballSprite.y < Utils.gameUpperBorder) {
            ballSprite.y = Utils.gameUpperBorder;
            ballSprite.yVelocity = (int) ((double)(-ballSprite.yVelocity) * Utils.ballSpeedReductionFactor);
        }
        if ((ballSprite.y > Utils.netUpperWallHeight
                && ballSprite.y < Utils.netUpperWallHeight + ballSprite.getBallImage().getWidth())
                && (ballSprite.x < Utils.leftGoalLine + Utils.netUpperWallWidth ||
                ballSprite.x > (Utils.rightGoalLine - Utils.netUpperWallWidth)) ) {
            ballSprite.y = Utils.netUpperWallHeight;
            ballSprite.yVelocity = (int) ((double)(-ballSprite.yVelocity) );
        }

        if ( ballSprite.yVelocity <= 0 && ballSprite.yVelocity >= Utils.ballSpeedThreshold )  {
            ballSprite.yVelocity = 0;
            if (ballSprite.xVelocity > 0) {
                ballSprite.xVelocity +=Utils.floorFriction;
            }
            else if (ballSprite.xVelocity < 0) {
                ballSprite.xVelocity -= Utils.floorFriction;
            }
            if (ballSprite.xVelocity <= Utils.floorFriction && ballSprite.xVelocity >= -Utils.floorFriction){
                ballSprite.xVelocity = 0;
            }
        }
    }

    public void slimeAndWallCollisionChecker() {
        if (slimeSprite.x < Utils.leftGoalLine) {
            slimeSprite.x = Utils.leftGoalLine;
            slimeSprite.xVelocity = 0;
        }
        else if (slimeSprite.x > (Utils.rightGoalLine - slimeSprite.slimeImage.getWidth())) {
            slimeSprite.x = Utils.rightGoalLine - slimeSprite.slimeImage.getWidth();
            slimeSprite.xVelocity = 0;
        }
        if (slimeSprite.y > slimeSprite.firstY) {
            slimeSprite.y = slimeSprite.firstY;
            slimeSprite.yVelocity = 0;
        }
    }

    public void goalChecker() {
        if ((ballSprite.x <= Utils.leftGoalLine || (ballSprite.x + 2 * Utils.ballRatio) >= Utils.rightGoalLine)&&
                (Utils.netUpperWallHeight < ballSprite.y)) {
            slimeSprite.initializeFirstState();
            ballSprite.initializeFirstState();
        }
    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) -
                        (slimeSprite.y + (1 + Utils.halfCircleConverter) * Utils.slimeRatio)), 2))
                + Utils.ballRatio / 2);
    }
}
