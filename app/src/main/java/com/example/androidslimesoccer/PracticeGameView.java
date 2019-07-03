package com.example.androidslimesoccer;

import android.content.Context;
import android.content.res.Resources;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

//TODO Will Be Completed By "EMAD"

public class PracticeGameView extends GameView implements SurfaceHolder.Callback {

    MainThread thread;
    SlimeSprite slimeSprite;
    Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    BallSprite ballSprite;
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.practice_background);
    Bitmap goal = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
    Bitmap leftGoal; Bitmap rightGoal;
    Resources resources = getResources();

    public PracticeGameView(Context context, String slimeName) {
        super(context);
        Utils.assetsXScale = (double)Utils.screenWidth / background.getWidth();
        Utils.assetsYScale = (double)Utils.screenHeight / background.getHeight();
        Log.i(slimeName, "HEY");
        background = getResizedBitmap(background, Utils.screenWidth, Utils.screenHeight);
        leftGoal = getResizedBitmap(goal, (int)(Utils.assetsXScale * goal.getWidth()),
                (int)(Utils.assetsYScale * goal.getHeight()));
        rightGoal = flipBitmap(leftGoal);
        Bitmap slimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(slimeName, "drawable", context.getPackageName()));
        slimeSprite = new SlimeSprite(SlimeType.valueOf(slimeName.toUpperCase()),
                getResizedBitmap(slimeBitmap, (int)(Utils.assetsXScale * slimeBitmap.getWidth()),
                        (int)(Utils.assetsYScale * slimeBitmap.getHeight())), true);
        ballSprite = new BallSprite(getResizedBitmap(ballBitmap,
                (int)(Utils.assetsXScale * ballBitmap.getWidth()),
                (int)(Utils.assetsXScale * ballBitmap.getHeight())));
        slimeSprite.initializeFirstState();
        Utils.ballStartX += ((int)(Utils.assetsXScale * ballBitmap.getWidth() / 2));
        ballSprite.initializeFirstState();

        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
        p.setStyle(Paint.Style.STROKE); p.setColor(Color.BLACK);
        if (slimeSprite.specialLevel > slimeSprite.slimeType.getSpecialThreshold())
            p.setStrokeWidth(15);
        else
            p.setStrokeWidth(5);
        canvas.drawCircle(Utils.leftSpecialButtonX, Utils.leftSpecialButtonY,
                Utils.specialButtonHalfSide, p);
        Paint p2 = new Paint();
        p2.setColor(slimeSprite.slimeType.getColor());
        canvas.drawCircle(Utils.leftSpecialButtonX, Utils.leftSpecialButtonY,
                (int)((double)slimeSprite.specialLevel / 1000 * Utils.specialButtonHalfSide), p2);
        slimeSprite.draw(canvas);
        ballSprite.draw(canvas);
    }

    public void update() {
        slimeSprite.update();
        ballSprite.update();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }

    public Bitmap flipBitmap (Bitmap bm) {
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
                            (event.getY(index) > (Utils.leftSpecialButtonY - Utils.specialButtonHalfSide))))
                slimeSprite.enableSpecial();
            else if (event.getX(index) < Utils.leftRightBorderX) {
                if (slimeSprite.isLookRight) {
                    slimeSprite.isLookRight = false;
                    slimeSprite.slimeImage = flipBitmap(slimeSprite.slimeImage);
                }
                slimeSprite.isMoveLeft = true;
            } else if (event.getX(index) < Utils.rightUpBorderX) {
                if (!slimeSprite.isLookRight) {
                    slimeSprite.isLookRight = true;
                    slimeSprite.slimeImage = flipBitmap(slimeSprite.slimeImage);
                }
                slimeSprite.isMoveRight = true;
            } else {
                if (slimeSprite.y == Utils.slimeStartY) {
                    slimeSprite.yVelocity = -Utils.initialYVelocity;
                }
            }
            return true;
        } else if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) &&
                event.getX(index) < Utils.rightUpBorderX) {
            if (slimeSprite.isMoveLeft)
                slimeSprite.isMoveLeft = false;
            else if (slimeSprite.isMoveRight)
                slimeSprite.isMoveRight = false;
            return true;
        }
//        else if (action == MotionEvent.ACTION_MOVE) {
//            if (event.getX() < Utils.leftRightBorderX && slimeSprite.isLookRight) {
//                slimeSprite.isLookRight = false;
//                slimeSprite.slimeImage = flipBitmap(slimeSprite.slimeImage);
//                isMoveToRight = false;
//                isMoveToLeft = true;
//            } else if (event.getX() <= Utils.rightUpBorderX && !slimeSprite.isLookRight) {
//                slimeSprite.isLookRight = true;
//                slimeSprite.slimeImage = flipBitmap(slimeSprite.slimeImage);
//                isMoveToLeft = false;
//                isMoveToRight = true;
//            }
//            return true;
//        }
        return false;
    }
}
