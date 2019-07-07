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
        if (dis < (ballRatio + (Utils.halfCircleConverter + 1) * slimeRatio) && yProjection >= 0) {
            Log.i("collision", "1");
            yProjection += Utils.halfCircleConverter * slimeRatio;
            double relativeXVelocity = ballSprite.xVelocity - slimeSprite.xVelocity;
            double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
            double totalVelocity = Math.sqrt(Math.pow(relativeXVelocity, 2) + Math.pow(relativeYVelocity, 2)) * (0.95);

            double theta = Math.atan2(yProjection, -xProjection);
            double alpha = Math.atan2(relativeXVelocity, relativeYVelocity);
            double finalAngle = 2 * theta - alpha - Math.PI / 2;

            ballSprite.xVelocity = (int) (slimeSprite.xVelocity * 3 / 5 + totalVelocity * Math.cos(finalAngle));
            ballSprite.yVelocity = (int) (slimeSprite.yVelocity * 3 / 5 - totalVelocity * Math.sin(finalAngle));

            ballSprite.x = slimeCenterX - (ballRatio + slimeRatio) * xProjection / dis - ballRatio;
            ballSprite.y = slimeCenterY - (ballRatio + slimeRatio) * yProjection / dis - ballRatio;
        }
        else if (yProjection > - ballRatio && (xProjection < (ballRatio / 2 + slimeRatio)
                && xProjection > -(ballRatio / 2 + slimeRatio)) && yProjection < 0) {
            if (ballSprite.y == Utils.slimeStartY - ballSprite.getBallImage().getHeight()) {
                slimeSprite.y = Utils.slimeStartY - ballSprite.getBallImage().getHeight()
                        - slimeSprite.slimeImage.getHeight();
                Log.i("collision", "21");
                if (xProjection >= 0)
                    ballSprite.xVelocity -= Utils.screenWidth / 90;
                else
                    ballSprite.xVelocity += Utils.screenWidth / 90;

            } else {
                Log.i("collision", "22");
                double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
                ballSprite.yVelocity -= ((int) relativeYVelocity  - slimeSprite.yVelocity / 2);
                ballSprite.y = slimeSprite.y + ballRatio + ballSprite.yVelocity / 5;
            }
        }
    }

    public void aiUpdateAction() {
        if (slimeSprite2.yVelocity == 0) {
            int ballSpriteY = ballSprite.y;
            int ballSpriteX = ballSprite.x;
            int ballXVelocity = ballSprite.xVelocity;
            int ballYVelocity = ballSprite.yVelocity;
            int slime2SpriteX = slimeSprite2.x;
            int slime2SpriteY = slimeSprite2.y;
            int slime2YVelocity = Utils.initialYVelocity;
            Log.i("ballSpriteX", Integer.toString(Utils.slimeJumpTime));
            for (int t = 0; t < Utils.slimeJumpTime ; t++) {
                ballSpriteX += ballXVelocity;
                ballSpriteY += ballYVelocity;
                ballYVelocity = (ballYVelocity == 0)  ? 0 : ballYVelocity - Utils.gravityAcceleration;
                slime2SpriteY += slime2YVelocity;
                slime2YVelocity = (slime2YVelocity == 0) ? 0 : slime2YVelocity - Utils.gravityAcceleration;
//                Log.i("ballSpriteX", Integer.toString(ballSpriteX));
//                Log.i("ballSpriteY", Integer.toString(ballSpriteY));
//                Log.i("slimeSpriteX", Integer.toString(slime2SpriteX));
//                Log.i("slimeSpriteY", Integer.toString(slime2SpriteY));
                if (aiDistance(slime2SpriteY, slime2SpriteX, ballSpriteY, ballSpriteX) <=
                        (Utils.ballRatio + Utils.slimeRatio)) {
                    slimeSprite2.yVelocity -= Utils.initialYVelocity;
                    break;
                }
            }
        }
    }

    public void doSpecial(SlimeSprite performerSlimeSprite, SlimeSprite performedSlimeSprite) {

    }

    public int aiDistance(int slimeSpriteY, int slimeSpriteX, int ballSpriteY, int ballSpriteX) {
        return (int)(Math.sqrt(Math.pow(((ballSpriteX + Utils.ballRatio) - (slimeSpriteX + Utils.slimeRatio)), 2) +
                Math.pow(((ballSpriteY + Utils.ballRatio) - (slimeSpriteY + slimeSprite2.slimeImage.getHeight())), 2)));
    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) -
                        (slimeSprite.y + (1 + Utils.halfCircleConverter) * Utils.slimeRatio)), 2))
                + Utils.ballRatio / 2);
    }
}
