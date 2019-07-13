package com.example.androidslimesoccer;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class AIThread extends Thread {
    private BallSprite ballSprite;
    private SlimeSprite slimeSprite1;
    private SlimeSprite slimeSprite2;
    private int targetFPS = Utils.targetFPS;
    private boolean running;
    private boolean paused;
    private long averageFPS;


    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public AIThread(BallSprite ballSprite, SlimeSprite playerSlimeSprite, SlimeSprite aiSlimeSprite) {
        this.ballSprite = ballSprite;
        this.slimeSprite1 = playerSlimeSprite;
        this.slimeSprite2 = aiSlimeSprite;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / targetFPS;

        while (running) {
            if (paused) {
                continue;
            }
            startTime = System.nanoTime();

            if (slimeSprite2.yVelocity == 0 &&
                    slimeSprite2.y + slimeSprite2.slimeImage.getHeight() >= Utils.slimeStartY) {
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

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                this.sleep(waitTime);
            } catch (Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS)        {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println(averageFPS);
            }
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

    public int aiDistance(int slimeSpriteY, int slimeSpriteX, int ballSpriteY, int ballSpriteX) {
        return (int)(Math.sqrt(Math.pow(((ballSpriteX + Utils.ballRatio) - (slimeSpriteX + Utils.slimeRatio)), 2) +
                Math.pow(((ballSpriteY + Utils.ballRatio) - (slimeSpriteY + slimeSprite2.slimeImage.getHeight())), 2)));
    }

    public Bitmap flipBitmap (Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
}
