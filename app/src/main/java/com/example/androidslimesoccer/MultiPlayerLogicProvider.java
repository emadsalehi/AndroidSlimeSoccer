package com.example.androidslimesoccer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.net.DatagramSocket;

public class MultiPlayerLogicProvider {
    SlimeSprite slimeSprite1, slimeSprite2;
    SpecialSprite specialSprite1, specialSprite2;
    BallSprite ballSprite;
    DatagramSocket serverSocket;
    byte[] sendData = new byte[32];
    int slime1Goals = 0, slime2Goals = 0;

    public MultiPlayerLogicProvider(SlimeSprite slimeSprite1, SlimeSprite slimeSprite2, BallSprite ballSprite
            , SpecialSprite specialSprite1, SpecialSprite specialSprite2, DatagramSocket serverSocket) {
        this.slimeSprite1 = slimeSprite1;
        this.slimeSprite2 = slimeSprite2;
        this.ballSprite = ballSprite;
        this.specialSprite1 = specialSprite1;
        this.specialSprite2 = specialSprite2;
        this.serverSocket = serverSocket;
    }

    public void update() {
        slimeSprite1.update();
        slimeSprite2.update();
        slimeAndWallCollisionChecker(slimeSprite1);
        slimeAndWallCollisionChecker(slimeSprite2);
        slimeAndBallCollisionChecker(slimeSprite1);
        slimeAndBallCollisionChecker(slimeSprite2);
        ballSprite.update();
        ballAndWallCollisionChecker();
        if (slimeSprite1.specialIsActive)
            doSpecial(slimeSprite1, specialSprite1, slimeSprite2);
        else
            specialSprite1.isOnDraw = false;
        if (slimeSprite2.specialIsActive)
            doSpecial(slimeSprite2, specialSprite2, slimeSprite1);
        else
            specialSprite2.isOnDraw = false;
        goalChecker();
        writer();
    }

    public void goalChecker() {
        if (ballSprite.x <= Utils.leftGoalLine &&
                (Utils.netUpperWallHeight < ballSprite.y )) {
            slime2Goals++;
            slimeSprite1.initializeFirstState();
            slimeSprite2.initializeFirstState();
            ballSprite.initializeFirstState();
        } else if ((ballSprite.x + 2 * Utils.ballRatio) >= Utils.rightGoalLine &&
                (Utils.netUpperWallHeight < ballSprite.y )) {
            slime1Goals++;
            slimeSprite1.initializeFirstState();
            slimeSprite2.initializeFirstState();
            ballSprite.initializeFirstState();
        }
    }

    public void doSpecial(SlimeSprite performerSlimeSprite, SpecialSprite specialSprite
            , SlimeSprite performedSlimeSprite) {
        if (specialSprite.slimeType == SlimeType.INDIAN) {
            specialSprite.isOnDraw = true;
            specialSprite.y = performedSlimeSprite.y - specialSprite.specialImage1.getHeight() * 2;
            specialSprite.x = performedSlimeSprite.x;
            if (performedSlimeSprite.y >= Utils.slimeStartY - performedSlimeSprite.slimeImage.getHeight() - Utils.initialYVelocity) {
                performedSlimeSprite.y = Utils.slimeStartY - performedSlimeSprite.slimeImage.getHeight();
                performedSlimeSprite.yVelocity = 0;
            }
        } else if (specialSprite.slimeType == SlimeType.TRAFFIC) {
            specialSprite.x = ballSprite.x - Utils.ballRatio;
            specialSprite.y = ballSprite.y - Utils.ballRatio;
            if (performerSlimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 3) {
                ballSprite.xVelocity = 0;
                ballSprite.yVelocity = 0;
            }
            if (performerSlimeSprite.specialCountDown == 0
                    || performerSlimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 5) {
                specialSprite.isOnDraw = true;
            } else {
                specialSprite.isOnDraw = false;
            }
        } else if (specialSprite.slimeType == SlimeType.RUNNER) {
            out: if (performerSlimeSprite.isMoveLeft) {
                performerSlimeSprite.xVelocity = -3 * Utils.initialXVelocity;
                performerSlimeSprite.x -= Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker(performerSlimeSprite)) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
                performerSlimeSprite.x -= Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker(performerSlimeSprite)) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
                performerSlimeSprite.x -= Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker(performerSlimeSprite)) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
            }
            else if (performerSlimeSprite.isMoveRight) {
                performerSlimeSprite.xVelocity = 3 * Utils.initialXVelocity;
                performerSlimeSprite.x += Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker(performerSlimeSprite)) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
                performerSlimeSprite.x += Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker(performerSlimeSprite)) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
                performerSlimeSprite.x += Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker(performerSlimeSprite)) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
            } else {
                performerSlimeSprite.xVelocity = 0;
            }
            slimeAndWallCollisionChecker(performerSlimeSprite);

        } else if (specialSprite.slimeType == SlimeType.ALIEN) {
            if (performerSlimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 2) {
                specialSprite.isOnDraw = true;
                specialSprite.x = (int) (ballSprite.x - performerSlimeSprite.slimeImage.getWidth()
                        + 0.4 * Utils.ballRatio);
                specialSprite.y = (int) (ballSprite.y + 1.6 * Utils.ballRatio);
            } else if (performerSlimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 4) {
                specialSprite.isOnDraw = true;
                Bitmap temp = specialSprite.specialImage1;
                specialSprite.specialImage1 = specialSprite.specialImage2;
                specialSprite.specialImage2 = temp;
                specialSprite.x = (int) (ballSprite.x - performerSlimeSprite.slimeImage.getWidth()
                        + 0.4 * Utils.ballRatio);
                specialSprite.y = (int) (ballSprite.y + 1.6 * Utils.ballRatio);
            } else if (performerSlimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 5) {
                specialSprite.isOnDraw = false;
                Bitmap temp = specialSprite.specialImage1;
                specialSprite.specialImage1 = specialSprite.specialImage2;
                specialSprite.specialImage2 = temp;
                performerSlimeSprite.x = (int) (ballSprite.x - performerSlimeSprite.slimeImage.getWidth()
                        +  1.4 * Utils.ballRatio);
                performerSlimeSprite.y = (ballSprite.y + Utils.ballRatio);
                if (performerSlimeSprite.y + performerSlimeSprite.slimeImage.getHeight() > Utils.slimeStartY)
                    performerSlimeSprite.y = Utils.slimeStartY - performerSlimeSprite.slimeImage.getHeight();
                performerSlimeSprite.yVelocity = -Utils.initialYVelocity;
                performerSlimeSprite.xVelocity = 0;
            } else {
                specialSprite.isOnDraw = false;
            }
        }
    }

    public boolean slimeAndBallCollisionChecker(SlimeSprite slimeSprite) {
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
            yProjection += Utils.halfCircleConverter * slimeRatio;
            double relativeXVelocity = ballSprite.xVelocity - slimeSprite.xVelocity;
            double relativeYVelocity = ballSprite.yVelocity - slimeSprite.yVelocity;
            double totalVelocity = Math.sqrt(Math.pow(relativeXVelocity, 2) + Math.pow(relativeYVelocity, 2)) * (0.95);

            if (relativeYVelocity < 0 && relativeYVelocity < -yProjection) {
                ballSprite.y = (slimeSprite.y + slimeSprite.slimeImage.getHeight());
                if (ballSprite.y >= Utils.slimeStartY - 2 * ballRatio) {
                    ballSprite.y = Utils.slimeStartY - ballSprite.getBallImage().getHeight();
                    ballSprite.yVelocity = 0;
                    slimeSprite.y = Utils.slimeStartY - ballSprite.getBallImage().getHeight()
                            - slimeSprite.slimeImage.getHeight();
                }
                break out;
            }
            if (relativeYVelocity < -1 && yProjection <= 1.5 * ballRatio) {
                break out;
            }


            double theta = Math.atan2(yProjection, -xProjection);
            double alpha = Math.atan2(relativeXVelocity, relativeYVelocity);
            double finalAngle = 2 * theta - alpha - Math.PI / 2;

            ballSprite.xVelocity = (int) (slimeSprite.xVelocity + totalVelocity * Math.cos(finalAngle));
            ballSprite.yVelocity = (int) (slimeSprite.yVelocity - totalVelocity * Math.sin(finalAngle));

            if (ballSprite.y >= Utils.slimeStartY - 2 * ballRatio - 1) {
                ballSprite.yVelocity += 5 * Utils.gravityAcceleration;
                ballSprite.xVelocity += Utils.gravityAcceleration;
            }

            ballSprite.y = slimeCenterY - (ballRatio + slimeRatio) * yProjection / dis - ballRatio;
            ballSprite.x = slimeCenterX - (ballRatio + slimeRatio) * xProjection / dis - ballRatio;
            return true;
        }
        else if (yProjection > - ballRatio && (xProjection <= (ballRatio / 2 + slimeRatio)
                && xProjection >= -(ballRatio / 2 + slimeRatio)) && yProjection < 0
                && ballSprite.y <= Utils.slimeStartY - 2 * ballRatio) {
            if (ballSprite.y == Utils.slimeStartY - ballSprite.getBallImage().getHeight()) {
                slimeSprite.y = Utils.slimeStartY - ballSprite.getBallImage().getHeight()
                        - slimeSprite.slimeImage.getHeight();
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
            return true;
        }
        return false;
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
        if ((ballSprite.y > Utils.netUpperWallHeight - ballSprite.getBallImage().getWidth()
                && ballSprite.y < Utils.netUpperWallHeight + 2 * ballSprite.getBallImage().getWidth())
                && (ballSprite.x < Utils.leftGoalLine + Utils.netUpperWallWidth ||
                ballSprite.x > (Utils.rightGoalLine - Utils.netUpperWallWidth)) ) {
            ballSprite.y = Utils.netUpperWallHeight;
            ballSprite.yVelocity = (-ballSprite.yVelocity) ;
            if (ballSprite.x < Utils.leftGoalLine + Utils.netUpperWallWidth ) {
                ballSprite.xVelocity += Utils.netXVelocityIncrease;
            }
            else {
                ballSprite.yVelocity -= Utils.netXVelocityIncrease;
            }
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

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) -
                        (slimeSprite.y + (slimeSprite.y + slimeSprite.slimeImage.getHeight() +
                                Utils.halfCircleConverter * Utils.slimeRatio))), 2))
                + Utils.ballRatio / 2);
    }

    public void writer() {
        StringBuilder sb = new StringBuilder();
        sb.append(slime1Goals);
        sb.append(",");
        sb.append(slime1Goals);
        sb.append(",");
        //To Be Continued...
    }

    public Bitmap flipBitmap (Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
}
