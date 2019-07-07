package com.example.androidslimesoccer;


//TODO Will Be Completed By ALL

import android.util.Log;

public class SinglePlayerLogicProvider {
    SlimeSprite slimeSprite1, slimeSprite2;
    BallSprite ballSprite;
    int slime1Goals = 0, slime2Goals = 0;

    public SinglePlayerLogicProvider(SlimeSprite slimeSprite1, SlimeSprite slimeSprite2, BallSprite ballSprite) {
        this.slimeSprite1 = slimeSprite1;
        this.slimeSprite2 = slimeSprite2;
        this.ballSprite = ballSprite;
    }


    public void update() {
        aiUpdateAction();
        slimeSprite1.update();
        slimeSprite2.update();
        ballSprite.update();
        slimeAndBallCollisionChecker(slimeSprite1);
        slimeAndBallCollisionChecker(slimeSprite2);
        if (slimeSprite1.specialIsActive)
            doSpecial(slimeSprite1, slimeSprite2);
        if (slimeSprite2.specialIsActive)
            doSpecial(slimeSprite2, slimeSprite1);
        goalChecker();
    }


    public void goalChecker() {
        if (ballSprite.x <= Utils.leftGoalLine &&
                (Utils.netUpperWallHeight < ballSprite.y + 2 * Utils.ballRatio)) {
            slime2Goals++;
            slimeSprite1.initializeFirstState();
            slimeSprite2.initializeFirstState();
            ballSprite.initializeFirstState();
        } else if ((ballSprite.x + 2 * Utils.ballRatio) >= Utils.rightGoalLine &&
                (Utils.netUpperWallHeight < ballSprite.y + 2 * Utils.ballRatio)) {
            slime1Goals++;
            slimeSprite1.initializeFirstState();
            slimeSprite2.initializeFirstState();
            ballSprite.initializeFirstState();
        }
    }

    public void slimeAndBallCollisionChecker(SlimeSprite slimeSprite) {
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

            ballSprite.xVelocity = (int) (totalVelocity * Math.cos(finalAngle));
            ballSprite.yVelocity = (int) (- totalVelocity * Math.sin(finalAngle));
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

    public void aiUpdateAction() {

    }

    public void doSpecial(SlimeSprite performerSlimeSprite, SlimeSprite performedSlimeSprite) {

    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) - (slimeSprite.y + slimeSprite.slimeImage.getHeight())), 2)));
    }
}
