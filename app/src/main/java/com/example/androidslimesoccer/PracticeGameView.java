package com.example.androidslimesoccer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

//TODO Will Be Completed By "EMAD"

public class PracticeGameView extends GameView implements SurfaceHolder.Callback {

    MainThread thread;
    SlimeSprite slimeSprite;
    SpecialSprite specialSprite;
    Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    BallSprite ballSprite;
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.practice_background);
    Bitmap goal = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
    Bitmap leftGoal;
    Bitmap rightGoal;
    Resources resources = getResources();
    PracticeLogicProvider logicProvider;
    int downX;


    public PracticeGameView(Context context, String slimeName) {
        super(context);
        Utils.assetsXScale = (double) Utils.screenWidth / background.getWidth();
        Utils.assetsYScale = (double) Utils.screenHeight / background.getHeight();
        background = getResizedBitmap(background, Utils.screenWidth, Utils.screenHeight);
        leftGoal = getResizedBitmap(goal, (int) (Utils.assetsXScale * goal.getWidth()),
                (int) (Utils.assetsYScale * goal.getHeight()));
        rightGoal = flipBitmap(leftGoal);
        Bitmap slimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(slimeName, "drawable", context.getPackageName()));
        slimeSprite = new SlimeSprite(SlimeType.valueOf(slimeName.toUpperCase()),
                getResizedBitmap(slimeBitmap, (int) (Utils.assetsYScale * slimeBitmap.getWidth()),
                        (int) (Utils.assetsYScale * slimeBitmap.getHeight())), true);
        Utils.ballRatio = (int) (Utils.assetsYScale * ballBitmap.getWidth() / 2);
        Utils.ballStartX = Utils.screenWidth / 2 - Utils.ballRatio;
        Utils.slimeRatio = (int) (Utils.screenWidth / 20 * (Utils.assetsYScale / Utils.assetsXScale));
        ballSprite = new BallSprite(getResizedBitmap(ballBitmap,
                (int) (Utils.assetsYScale * ballBitmap.getWidth()),
                (int) (Utils.assetsYScale * ballBitmap.getHeight())));
        slimeSprite.initializeFirstState();
        ballSprite.initializeFirstState();
        specialSprite = new SpecialSprite(SlimeType.valueOf(slimeName.toUpperCase()), resources);
        logicProvider = new PracticeLogicProvider(slimeSprite, ballSprite, specialSprite, context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("created","called");
        thread = new MainThread(getHolder(), this);
        if (!thread.isRunning()) {
            thread.setRunning(true);
            if (!thread.isPaused()) {
                thread.start();
            } else {
                Log.i("thread", Boolean.toString(thread.isPaused()));
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("changed","called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("destroyed","called");
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(background, 0,
                0, null);
        canvas.drawBitmap(leftGoal, Utils.screenWidth / 40,
                Utils.screenHeight * 14 / 20, null);
        canvas.drawBitmap(rightGoal, Utils.screenWidth * 39 / 40 - rightGoal.getWidth(),
                Utils.screenHeight * 14 / 20, null);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        if (slimeSprite.specialLevel > slimeSprite.slimeType.getSpecialThreshold())
            p.setStrokeWidth(15);
        else
            p.setStrokeWidth(5);
        canvas.drawCircle(Utils.leftSpecialButtonX, Utils.leftSpecialButtonY,
                Utils.specialButtonHalfSide, p);
        Paint p2 = new Paint();
        p2.setColor(slimeSprite.slimeType.getColor());
        canvas.drawCircle(Utils.leftSpecialButtonX, Utils.leftSpecialButtonY,
                (int) ((double) slimeSprite.specialLevel / 1000 * Utils.specialButtonHalfSide - 7), p2);
        slimeSprite.draw(canvas);
        ballSprite.draw(canvas);
        specialSprite.draw(canvas);
    }

    public void update() {
        logicProvider.update();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }

    public Bitmap flipBitmap(Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            if (((event.getX(index) > (Utils.leftSpecialButtonX - Utils.specialButtonHalfSide)) &&
                    (event.getX(index) < (Utils.leftSpecialButtonX + Utils.specialButtonHalfSide))) &&
                    (((event.getY(index) < (Utils.leftSpecialButtonY + Utils.specialButtonHalfSide))) &&
                            (event.getY(index) > (Utils.leftSpecialButtonY - Utils.specialButtonHalfSide)))) {
                slimeSprite.enableSpecial();
                slimeSprite.specialButtonIsHold = true;
            } else if (event.getX(index) < Utils.leftUpBorderX) {
                if (slimeSprite.y == Utils.slimeStartY - slimeSprite.slimeImage.getHeight()) {
                    slimeSprite.yVelocity = -Utils.initialYVelocity;
                }
            } else {
                downX = (int) event.getX(index);
            }
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (event.getX(index) > Utils.leftUpBorderX) {
                int x = (int) event.getX(index);
                if (x < downX) {
                    if (slimeSprite.isLookRight) {
                        slimeSprite.isLookRight = false;
                        slimeSprite.slimeImage = flipBitmap(slimeSprite.slimeImage);
                    }
                    slimeSprite.isMoveRight = false;
                    slimeSprite.isMoveLeft = true;
                } else {
                    if (!slimeSprite.isLookRight) {
                        slimeSprite.isLookRight = true;
                        slimeSprite.slimeImage = flipBitmap(slimeSprite.slimeImage);
                    }
                    slimeSprite.isMoveLeft = false;
                    slimeSprite.isMoveRight = true;
                }
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            if (((event.getX(index) > (Utils.leftSpecialButtonX - Utils.specialButtonHalfSide)) &&
                    (event.getX(index) < (Utils.leftSpecialButtonX + Utils.specialButtonHalfSide))) &&
                    (((event.getY(index) < (Utils.leftSpecialButtonY + Utils.specialButtonHalfSide))) &&
                            (event.getY(index) > (Utils.leftSpecialButtonY - Utils.specialButtonHalfSide)))) {
                slimeSprite.specialButtonIsHold = false;
            } else if (event.getX(index) > Utils.leftUpBorderX) {
                if (slimeSprite.isMoveLeft)
                    slimeSprite.isMoveLeft = false;
                else if (slimeSprite.isMoveRight)
                    slimeSprite.isMoveRight = false;
            }
            return true;
        }
        return false;
    }
}
