package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

//TODO Will Be Completed By "SINA"

public class SinglePlayerGameView extends GameView implements SurfaceHolder.Callback {

    MainThread thread;
    SlimeSprite leftSlimeSprite;
    SlimeSprite rightSlimeSprite;
    SpecialSprite leftSpecialSprite;
    SpecialSprite rightSpecialSprite;
    Activity context;
    Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    BallSprite ballSprite;
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    Bitmap goal = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
    Bitmap leftGoal; Bitmap rightGoal;
    Resources resources = getResources();
    SinglePlayerLogicProvider singlePlayerLogicProvider;
    int goalLimit;
    int leftGoalNumber;
    int rightGoalNumber;
    int downX;
    boolean isMute = false;

    public SinglePlayerGameView(Activity context, String leftSlimeName, String rightSlimeName
                ,int goalLimit, boolean isMute) {
        super(context);
        this.context = context;
        leftGoalNumber = 0;
        rightGoalNumber = 0;
        this.isMute = isMute;
        Utils.assetsXScale = (double)Utils.screenWidth / background.getWidth();
        Utils.assetsYScale = (double)Utils.screenHeight / background.getHeight();
        background = getResizedBitmap(background, Utils.screenWidth, Utils.screenHeight);
        leftGoal = getResizedBitmap(goal, (int)(Utils.assetsXScale * goal.getWidth()),
                (int)(Utils.assetsYScale * goal.getHeight()));
        rightGoal = flipBitmap(leftGoal);


        Bitmap leftSlimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(leftSlimeName, "drawable", context.getPackageName()));
        leftSlimeSprite = new SlimeSprite(SlimeType.valueOf(leftSlimeName.toUpperCase()),
                getResizedBitmap(leftSlimeBitmap, (int)(Utils.assetsYScale * leftSlimeBitmap.getWidth()),
                        (int)(Utils.assetsYScale * leftSlimeBitmap.getHeight())), true);


        Bitmap rightSlimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(rightSlimeName, "drawable", context.getPackageName()));
        rightSlimeSprite = new SlimeSprite(SlimeType.valueOf(rightSlimeName.toUpperCase()),
                getResizedBitmap(rightSlimeBitmap, (int)(Utils.assetsYScale * rightSlimeBitmap.getWidth()),
                        (int)(Utils.assetsYScale * rightSlimeBitmap.getHeight())), false);

        this.goalLimit = goalLimit;
        Utils.ballRatio = (int)(Utils.assetsYScale * ballBitmap.getWidth() / 2);
        Utils.ballStartX = Utils.screenWidth / 2 - Utils.ballRatio;
        Utils.slimeRatio = (int) (Utils.screenWidth / 20 * (Utils.assetsYScale / Utils.assetsXScale));
        ballSprite = new BallSprite(getResizedBitmap(ballBitmap,
                (int)(Utils.assetsYScale * ballBitmap.getWidth()),
                (int)(Utils.assetsYScale * ballBitmap.getHeight())));
        ballSprite.initializeFirstState();
        leftSlimeSprite.initializeFirstState();
        rightSlimeSprite.initializeFirstState();

        leftSpecialSprite = new SpecialSprite(SlimeType.valueOf(leftSlimeName.toUpperCase()), resources);
        rightSpecialSprite = new SpecialSprite(SlimeType.valueOf(rightSlimeName.toUpperCase()), resources);
        singlePlayerLogicProvider = new SinglePlayerLogicProvider(leftSlimeSprite, rightSlimeSprite
                , ballSprite, leftSpecialSprite, rightSpecialSprite, context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("created","called");
        thread = new MainThread(getHolder(), this);
        if(isMute) {
            singlePlayerLogicProvider.setSoundVolume(0);
        }
        else {
            singlePlayerLogicProvider.setSoundVolume(1);
        }
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

    public void update () {
        singlePlayerLogicProvider.update();
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
                (int)((double)leftSlimeSprite.specialLevel / 1000 * Utils.specialButtonHalfSide - 7), p2);

        Paint p1 = new Paint();
        p1.setStyle(Paint.Style.STROKE); p.setColor(Color.BLACK);
        if (rightSlimeSprite.specialLevel > rightSlimeSprite.slimeType.getSpecialThreshold())
            p1.setStrokeWidth(15);
        else
            p1.setStrokeWidth(5);
        canvas.drawCircle(Utils.rightSpecialButtonX, Utils.rightSpecialButtonY,
                Utils.specialButtonHalfSide, p1);
        Paint p12 = new Paint();
        p12.setColor(rightSlimeSprite.slimeType.getColor());
        canvas.drawCircle(Utils.rightSpecialButtonX, Utils.rightSpecialButtonY,
                (int)((double)rightSlimeSprite.specialLevel / 1000 * Utils.specialButtonHalfSide - 7), p12);

        leftSlimeSprite.draw(canvas);
        rightSlimeSprite.draw(canvas);
        ballSprite.draw(canvas);
        leftSpecialSprite.draw(canvas);
        rightSpecialSprite.draw(canvas);
        Typeface numberTypeface = Typeface.createFromAsset(this.context.getAssets(),
                "fonts/Zekton.ttf");
        Paint numberPaint = new Paint();
        numberPaint.setTypeface(numberTypeface);
        numberPaint.setTextSize(Utils.screenHeight / 9);
        canvas.drawText(Integer.toString(goalLimit), Utils.goalLimitX, Utils.goalLimitY, numberPaint);

        canvas.drawText(Integer.toString(singlePlayerLogicProvider.slime1Goals),
                Utils.leftGoalX, Utils.goalLimitY, numberPaint);
        canvas.drawText(Integer.toString(singlePlayerLogicProvider.slime2Goals),
                Utils.rightGoalX, Utils.goalLimitY, numberPaint);

        if (singlePlayerLogicProvider.slime1Goals == goalLimit) {
            Intent intent = new Intent(context, ResultActivity.class);
            intent.putExtra("IS_WON", true);
            intent.putExtra("isPaused", isMute);
            thread.setRunning(false);
            context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            context.startActivity(intent);
        }else if (singlePlayerLogicProvider.slime2Goals == goalLimit) {

            Intent intent = new Intent(context, ResultActivity.class);
            intent.putExtra("isPaused", isMute);
            intent.putExtra("IS_WON", false);
            thread.setRunning(false);
            context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            context.startActivity(intent);
        }
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
                leftSlimeSprite.enableSpecial();
                leftSlimeSprite.specialButtonIsHold = true;
            } else if (event.getX(index) < Utils.leftUpBorderX) {
                if (leftSlimeSprite.y == Utils.slimeStartY - leftSlimeSprite.slimeImage.getHeight()) {
                    leftSlimeSprite.yVelocity = -Utils.initialYVelocity;
                }
            } else {
                downX = (int) event.getX(index);
            }
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (event.getX(index) > Utils.leftUpBorderX) {
                int x = (int) event.getX(index);
                if (x < downX) {
                    if (leftSlimeSprite.isLookRight) {
                        leftSlimeSprite.isLookRight = false;
                        leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                    }
                    leftSlimeSprite.isMoveRight = false;
                    leftSlimeSprite.isMoveLeft = true;
                } else {
                    if (!leftSlimeSprite.isLookRight) {
                        leftSlimeSprite.isLookRight = true;
                        leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                    }
                    leftSlimeSprite.isMoveLeft = false;
                    leftSlimeSprite.isMoveRight = true;
                }
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            if (((event.getX(index) > (Utils.leftSpecialButtonX - Utils.specialButtonHalfSide)) &&
                    (event.getX(index) < (Utils.leftSpecialButtonX + Utils.specialButtonHalfSide))) &&
                    (((event.getY(index) < (Utils.leftSpecialButtonY + Utils.specialButtonHalfSide))) &&
                            (event.getY(index) > (Utils.leftSpecialButtonY - Utils.specialButtonHalfSide)))) {
                leftSlimeSprite.specialButtonIsHold = false;
            } else if (event.getX(index) > Utils.leftUpBorderX) {
                if (leftSlimeSprite.isMoveLeft)
                    leftSlimeSprite.isMoveLeft = false;
                else if (leftSlimeSprite.isMoveRight)
                    leftSlimeSprite.isMoveRight = false;
            }
            return true;
        }
        return false;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }

    public Bitmap flipBitmap (Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }


    public boolean muteSound (){
        if(isMute) {
            isMute = false;
            singlePlayerLogicProvider.setSoundVolume(1);
            return false;
        } else {
            isMute = true;
            singlePlayerLogicProvider.setSoundVolume(0);
            return true;
        }
    }
}
