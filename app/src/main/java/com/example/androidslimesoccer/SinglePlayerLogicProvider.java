package com.example.androidslimesoccer;


//TODO Will Be Completed By ALL

import android.graphics.Bitmap;
import android.graphics.Matrix;
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
        slimeAndBallCollisionChecker(slimeSprite1);
        slimeAndBallCollisionChecker(slimeSprite2);
        ballSprite.update();
        ballAndWallCollisionChecker();
        slimeAndWallCollisionChecker(slimeSprite1);
        slimeAndWallCollisionChecker(slimeSprite2);
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

    public void slimeAndWallCollisionChecker(SlimeSprite slimeSprite) {
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

    public void aiUpdateAction() {
        if (slimeSprite2.yVelocity == 0) {
            int ballSpriteY = ballSprite.y;
            int ballSpriteX = ballSprite.x;
            int ballXVelocity = ballSprite.xVelocity;
            int ballYVelocity = ballSprite.yVelocity;
            int slime2SpriteX = slimeSprite2.x;
            int slime2SpriteY = slimeSprite2.y;
            int slime2YVelocity = Utils.initialYVelocity;
            for (int t = 0; t < Utils.slimeJumpTime; t++) {
                ballSpriteX += ballXVelocity;
                ballSpriteY += ballYVelocity;
                ballYVelocity = (ballYVelocity == 0) ? 0 : ballYVelocity - Utils.gravityAcceleration;
                slime2SpriteY += slime2YVelocity;
                slime2YVelocity = (slime2YVelocity == 0) ? 0 : slime2YVelocity - Utils.gravityAcceleration;
                if (aiDistance(slime2SpriteY, slime2SpriteX, ballSpriteY, ballSpriteX) <=
                        (Utils.ballRatio + Utils.slimeRatio)) {
                    slimeSprite2.yVelocity -= Utils.initialYVelocity;
                    break;
                }
            }
        }
        int aiSpriteCenterX = slimeSprite2.x + Utils.slimeRatio;
        int playerSpriteCenterX = slimeSprite1.x + Utils.slimeRatio;
        if (aiSpriteCenterX > playerSpriteCenterX && aiSpriteCenterX > ballSprite.x 
                && playerSpriteCenterX < ballSprite.x 
            && ballSprite.x - playerSpriteCenterX > aiSpriteCenterX - ballSprite.x) {
            aiGotoBall();
        }
        else if (aiSpriteCenterX > playerSpriteCenterX && aiSpriteCenterX > ballSprite.x
                && playerSpriteCenterX < ballSprite.x
                && ballSprite.x - playerSpriteCenterX < aiSpriteCenterX - ballSprite.x) {
            aiGotoNet();
        }
        else if (ballSprite.x > aiSpriteCenterX) {
            aiGotoBall();
        }
        else {
            aiGotoNet();
        }
        
    }

    private void aiGotoNet() {
        slimeSprite2.isMoveRight =true;
        slimeSprite2.isMoveLeft = false;
    }

    public void aiGotoBall () {
        int aiSpriteCenterX = slimeSprite2.x + Utils.slimeRatio;
        if (ballSprite.x < slimeSprite2.x) {
            if (slimeSprite2.isLookRight) {
                slimeSprite2.isLookRight = false;
                slimeSprite2.slimeImage = flipBitmap(slimeSprite2.slimeImage);
            }
            slimeSprite2.isMoveRight = false;
            slimeSprite2.isMoveLeft = true;
        }
        else if (ballSprite.x > aiSpriteCenterX) {
            if (!slimeSprite2.isLookRight) {
                slimeSprite2.isLookRight =true;
                slimeSprite2.slimeImage = flipBitmap(slimeSprite2.slimeImage);
            }
            slimeSprite2.isMoveRight = true;
            slimeSprite2.isMoveLeft = false;
        }
        else {
            slimeSprite2.isMoveLeft = false;
            slimeSprite1.isMoveRight = false;
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
    public Bitmap flipBitmap (Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
}
