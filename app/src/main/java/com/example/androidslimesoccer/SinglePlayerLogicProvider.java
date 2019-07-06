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
//        Log.i("ratios", Integer.toString(slimeRatio + ballRatio));
//        Log.i("dis", Integer.toString(dis));
        if (dis < ( ballRatio + slimeRatio ) && yProjection >= 0) {
            Log.i("collision", "1");
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
        else if (yProjection < - ballRatio && (xProjection < (ballRatio + slimeRatio)
                && xProjection > -(ballRatio + slimeRatio))) {
            Log.i("collision", "2");
            double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
            ballSprite.yVelocity = -(int)relativeYVelocity  + slimeSprite.yVelocity;
            ballSprite.y = slimeSprite.y + ballRatio;
            if (ballSprite.y >= Utils.slimeStartY + ballSprite.getBallImage().getHeight()) {
                Log.i("collision", "3");
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

    public void aiUpdateAction() {

    }

    public void doSpecial(SlimeSprite performerSlimeSprite, SlimeSprite performedSlimeSprite) {

    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) - (slimeSprite.y + slimeSprite.slimeImage.getHeight())), 2)));
    }
}
