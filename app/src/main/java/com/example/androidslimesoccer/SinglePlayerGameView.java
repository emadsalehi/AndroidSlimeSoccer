package com.example.androidslimesoccer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

//TODO Will Be Completed By "SINA"

public class SinglePlayerGameView extends GameView implements SurfaceHolder.Callback {

    MainThread thread;
    SlimeSprite leftSlimeSprite;
    SlimeSprite rightSlimeSprite;
    Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    BallSprite ballSprite;
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    Bitmap goal = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
    Bitmap leftGoal; Bitmap rightGoal;
    Resources resources = getResources();
    GestureDetector gestureDetector;

    public SinglePlayerGameView(Context context, String leftSlimeName, String rightSlimeName) {
        super(context);
        Utils.assetsXScale = (double)Utils.screenWidth / background.getWidth();
        Utils.assetsYScale = (double)Utils.screenHeight / background.getHeight();
        background = getResizedBitmap(background, Utils.screenWidth, Utils.screenHeight);
        leftGoal = getResizedBitmap(goal, (int)(Utils.assetsXScale * goal.getWidth()),
                (int)(Utils.assetsYScale * goal.getHeight()));
        rightGoal = flipBitmap(leftGoal);

        Bitmap leftSlimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(leftSlimeName, "drawable", context.getPackageName()));
        leftSlimeSprite = new SlimeSprite(SlimeType.valueOf(leftSlimeName.toUpperCase()),
                getResizedBitmap(leftSlimeBitmap, (int)(Utils.assetsXScale * leftSlimeBitmap.getWidth()),
                        (int)(Utils.assetsYScale * leftSlimeBitmap.getHeight())), true);
        leftSlimeSprite.initializeFirstState();

        Bitmap rightSlimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(rightSlimeName, "drawable", context.getPackageName()));
        rightSlimeSprite = new SlimeSprite(SlimeType.valueOf(leftSlimeName.toUpperCase()),
                getResizedBitmap(rightSlimeBitmap, (int)(Utils.assetsXScale * rightSlimeBitmap.getWidth()),
                        (int)(Utils.assetsYScale * rightSlimeBitmap.getHeight())), true);
        rightSlimeSprite.initializeFirstState();

        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                if (e.getX() < Utils.leftRightBorderX) {
                    if (leftSlimeSprite.isLookRight) {
                        leftSlimeSprite.isLookRight = false;
                        leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                    }
                    leftSlimeSprite.x -= Utils.initialXVelocity;
                }
                else if (e.getX() < Utils.rightUpBorderX) {
                    if (!leftSlimeSprite.isLookRight) {
                        leftSlimeSprite.isLookRight = true;
                        leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                    }
                    leftSlimeSprite.x += Utils.initialXVelocity;
                }
            }

        });
        ballSprite = new BallSprite(getResizedBitmap(ballBitmap,
                (int)(Utils.assetsXScale * ballBitmap.getWidth()),
                (int)(Utils.assetsXScale * ballBitmap.getHeight())));
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

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

    public void update (){
        leftSlimeSprite.update();
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
        if (leftSlimeSprite.specialLevel > leftSlimeSprite.slimeType.getSpecialThreshold())
            p.setStrokeWidth(15);
        else
            p.setStrokeWidth(5);
        canvas.drawCircle(Utils.leftSpecialButtonX, Utils.leftSpecialButtonY,
                Utils.specialButtonHalfSide, p);
        Paint p2 = new Paint();
        p2.setColor(leftSlimeSprite.slimeType.getColor());
        canvas.drawCircle(Utils.leftSpecialButtonX, Utils.leftSpecialButtonY,
                (int)((double)leftSlimeSprite.specialLevel / 1000 * Utils.specialButtonHalfSide), p2);
        leftSlimeSprite.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (((event.getX() > (Utils.leftSpecialButtonX - Utils.specialButtonHalfSide)) &&
                (event.getX() < (Utils.leftSpecialButtonX + Utils.specialButtonHalfSide))) &&
                (((event.getY() < (Utils.leftSpecialButtonY + Utils.specialButtonHalfSide))) &&
                        (event.getY() > (Utils.leftSpecialButtonY - Utils.specialButtonHalfSide))))
            leftSlimeSprite.enableSpecial();
        else if (event.getX() < Utils.leftRightBorderX) {
            if (leftSlimeSprite.isLookRight) {
                leftSlimeSprite.isLookRight = false;
                leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
            }
            leftSlimeSprite.x -= Utils.initialXVelocity;
        }
        else if (event.getX() < Utils.rightUpBorderX) {
            if (!leftSlimeSprite.isLookRight) {
                leftSlimeSprite.isLookRight = true;
                leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
            }
            leftSlimeSprite.x += Utils.initialXVelocity;
        }
        else {
            if (leftSlimeSprite.y == Utils.slimeStartY) {
                leftSlimeSprite.yVelocity = -Utils.initialYVelocity;
            }
        }
        return gestureDetector.onTouchEvent(event);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }

    public Bitmap flipBitmap (Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
}
