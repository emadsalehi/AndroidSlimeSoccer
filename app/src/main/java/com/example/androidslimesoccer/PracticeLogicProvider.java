package com.example.androidslimesoccer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class PracticeLogicProvider {
    SlimeSprite slimeSprite;
    BallSprite ballSprite;
    SpecialSprite specialSprite;
    Context context;
    float soundVolume;

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    public PracticeLogicProvider(SlimeSprite slimeSprite, BallSprite ballSprite, SpecialSprite specialSprite, Context context) {
        this.slimeSprite = slimeSprite;
        this.ballSprite = ballSprite;
        this.specialSprite = specialSprite;
        this.context = context;
        SoundManager.InitSound(context);
    }

    public void update() {
        slimeSprite.update();
        slimeAndWallCollisionChecker();
        slimeAndBallCollisionChecker();
        ballSprite.update();
        ballAndWallCollisionChecker();
        if (slimeSprite.specialIsActive) {
            doSpecial();
        } else {
            specialSprite.isOnDraw = false;
        }
        goalChecker();
    }

    public void doSpecial() {
        if (specialSprite.slimeType == SlimeType.INDIAN) {
            specialSprite.isOnDraw = false;
        } else if (specialSprite.slimeType == SlimeType.TRAFFIC) {
            specialSprite.x = ballSprite.x - Utils.ballRatio;
            specialSprite.y = ballSprite.y - Utils.ballRatio;
            if (slimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 3) {
                ballSprite.xVelocity = 0;
                ballSprite.yVelocity = 0;
            }
            if (slimeSprite.specialCountDown == 0 || slimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 5) {
                specialSprite.isOnDraw = true;
            } else {
                specialSprite.isOnDraw = false;
            }
        } else if (specialSprite.slimeType == SlimeType.RUNNER) {
            out: if (slimeSprite.isMoveLeft) {
                slimeSprite.xVelocity = -3 * Utils.initialXVelocity;
                slimeSprite.x -= Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker()) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
                slimeSprite.x -= Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker()) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
                slimeSprite.x -= Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker()) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
            }
            else if (slimeSprite.isMoveRight) {
                slimeSprite.xVelocity = 3 * Utils.initialXVelocity;
                slimeSprite.x += Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker()) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
                slimeSprite.x += Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker()) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
                slimeSprite.x += Utils.initialXVelocity;
                if (slimeAndBallCollisionChecker()) {
                    ballSprite.update();
                    ballAndWallCollisionChecker();
                    break out;
                }
            } else {
                slimeSprite.xVelocity = 0;
            }
            slimeAndWallCollisionChecker();

        } else if (specialSprite.slimeType == SlimeType.ALIEN) {
            if (slimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 2) {
                specialSprite.isOnDraw = true;
                specialSprite.x = (int) (ballSprite.x - slimeSprite.slimeImage.getWidth() + 0.4 * Utils.ballRatio);
                specialSprite.y = (int) (ballSprite.y + 1.6 * Utils.ballRatio);
            } else if (slimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 4) {
                specialSprite.isOnDraw = true;
                Bitmap temp = specialSprite.specialImage1;
                specialSprite.specialImage1 = specialSprite.specialImage2;
                specialSprite.specialImage2 = temp;
                specialSprite.x = (int) (ballSprite.x - slimeSprite.slimeImage.getWidth() + 0.4 * Utils.ballRatio);
                specialSprite.y = (int) (ballSprite.y + 1.6 * Utils.ballRatio);
            } else if (slimeSprite.specialCountDown >= Utils.slimeMaxSpecialTime - 5) {
                specialSprite.isOnDraw = false;
                Bitmap temp = specialSprite.specialImage1;
                specialSprite.specialImage1 = specialSprite.specialImage2;
                specialSprite.specialImage2 = temp;
                slimeSprite.x = (int) (ballSprite.x - slimeSprite.slimeImage.getWidth() +  1.4 * Utils.ballRatio);
                slimeSprite.y = (ballSprite.y + Utils.ballRatio);
                if (slimeSprite.y + slimeSprite.slimeImage.getHeight() > Utils.slimeStartY)
                    slimeSprite.y = Utils.slimeStartY - slimeSprite.slimeImage.getHeight();
                slimeSprite.yVelocity = -Utils.initialYVelocity;
                slimeSprite.xVelocity = 0;
            } else {
                specialSprite.isOnDraw = false;
            }
        }
    }

    public boolean slimeAndBallCollisionChecker() {
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
        if ((ballSprite.y > Utils.netUpperWallHeight
                && ballSprite.y < Utils.netUpperWallHeight + ballSprite.getBallImage().getWidth())
                && (ballSprite.x < Utils.leftGoalLine + Utils.netUpperWallWidth ||
                ballSprite.x > (Utils.rightGoalLine - Utils.netUpperWallWidth)) ) {
            ballSprite.y = Utils.netUpperWallHeight;
            ballSprite.yVelocity = (int) ((double)(-ballSprite.yVelocity) );
            if (ballSprite.x < Utils.leftGoalLine + Utils.netUpperWallWidth) {
                ballSprite.xVelocity += Utils.netXVelocityIncrease;
            } else {
                ballSprite.xVelocity -= Utils.netXVelocityIncrease;
            }
            SoundManager.playSound(0, soundVolume);
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
                        (slimeSprite.y + slimeSprite.slimeImage.getHeight() +
                                Utils.halfCircleConverter * Utils.slimeRatio)), 2)) + Utils.ballRatio / 2);
    }
}
