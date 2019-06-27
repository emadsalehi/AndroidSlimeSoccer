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
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

//TODO Will Be Completed By "EMAD"

public class PracticeGameView extends SurfaceView implements SurfaceHolder.Callback {

    MainThread thread;
    SlimeSprite slimeSprite;
    Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    BallSprite ballSprite;
    Bitmap background = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background),
            Utils.screenWidth, Utils.screenHeight);
    Bitmap goal = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    Bitmap leftGoal; Bitmap rightGoal;
    Resources resources = getResources();
    int rightSideY; int leftSideX;

    public PracticeGameView(Context context, String slimeName) {
        super(context);
        leftGoal = getResizedBitmap(goal, (int)Utils.assetsScale * goal.getWidth(),
                (int)Utils.assetsScale * goal.getHeight());
        rightGoal = flipBitmap(leftGoal);
        Bitmap slimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(slimeName, "drawable", context.getPackageName()));
        slimeSprite = new SlimeSprite(SlimeType.valueOf(slimeName.toUpperCase()),
                getResizedBitmap(slimeBitmap, (int)Utils.assetsScale * slimeBitmap.getWidth(),
                        (int)Utils.assetsScale * slimeBitmap.getHeight()), true);
        ballSprite = new BallSprite(getResizedBitmap(ballBitmap,
                (int)Utils.assetsScale * ballBitmap.getWidth(),
                (int)Utils.assetsScale * ballBitmap.getHeight()));
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunnig(true);
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
        canvas.drawBitmap(background, Utils.screenWidth / 3,
                Utils.screenHeight / 3, null);
        canvas.drawBitmap(leftGoal, Utils.screenWidth * 1 / 20,
                Utils.screenHeight * 19 / 20, null);
        canvas.drawBitmap(rightGoal, Utils.screenWidth * 19 / 20 - rightGoal.getWidth(),
                Utils.screenHeight * 19 / 20, null);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE); p.setColor(Color.BLACK);
        if (slimeSprite.specialLevel > slimeSprite.slimeType.getSpecialThreshold())
            p.setStrokeWidth(20);
        else
            p.setStrokeWidth(5);
        canvas.drawCircle(Utils.leftSpecialButtonX, Utils.leftSpecialButtonY,
                Utils.specialButtonHalfSide, p);
        Paint p2 = new Paint();
        p2.setColor(slimeSprite.slimeType.getColor());
        canvas.drawArc(new RectF(), 90 - (slimeSprite.specialLevel / 2000 * 360),
                90 + (slimeSprite.specialLevel / 2000 * 360), true, p2);

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap flipBitmap (Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (((event.getX() > (Utils.leftSpecialButtonX - Utils.specialButtonHalfSide)) &&
                (event.getX() < (Utils.leftSpecialButtonX + Utils.specialButtonHalfSide))) &&
                (((event.getY() < (Utils.leftSpecialButtonY + Utils.specialButtonHalfSide))) &&
                        (event.getY() > (Utils.leftSpecialButtonY - Utils.specialButtonHalfSide))))

            slimeSprite.enableSpecial();
        else if (event.getX() < Utils.leftRightBorderX)
            slimeSprite.x -= Utils.initialXVelocity;
        else if (event.getX() < Utils.rightUpBorderX)
            slimeSprite.x += Utils.initialXVelocity;
        else {
            if (slimeSprite.yVelocity == 0)
                slimeSprite.yVelocity = Utils.initialYVelocity;
        }
        return super.onTouchEvent(event);
    }
}
