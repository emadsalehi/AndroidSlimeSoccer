package com.example.androidslimesoccer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

//TODO Will Be Completed By "SINA"

public class SinglePlayerGameView extends GameView implements SurfaceHolder.Callback {

    MainThread thread;
    SlimeSprite leftSlimeSprite;
    SlimeSprite rightSlimeSprite;
    Context context;
    Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    BallSprite ballSprite;
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    Bitmap goal = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
    Bitmap leftGoal; Bitmap rightGoal;
    Resources resources = getResources();
    GestureDetector gestureDetector;
    SinglePlayerLogicProvider singlePlayerLogicProvider;
    int goalLimit;
    int leftGoalNumber;
    int rightGoalNumber;

    public SinglePlayerGameView(Context context, String leftSlimeName, String rightSlimeName
                ,int goalLimit) {
        super(context);
        this.context = context;
        leftGoalNumber = 0;
        rightGoalNumber = 0;
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
        rightSlimeSprite = new SlimeSprite(SlimeType.valueOf(rightSlimeName.toUpperCase()),
                getResizedBitmap(rightSlimeBitmap, (int)(Utils.assetsXScale * rightSlimeBitmap.getWidth()),
                        (int)(Utils.assetsYScale * rightSlimeBitmap.getHeight())), false);
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
        this.goalLimit = goalLimit;
        Utils.ballRatio = (int)(Utils.assetsXScale * ballBitmap.getWidth() / 2);
        Utils.ballStartX -= Utils.ballRatio;
        Utils.slimeRatio = leftSlimeSprite.slimeImage.getWidth() / 2;
        ballSprite = new BallSprite(getResizedBitmap(ballBitmap,
                (int)(Utils.assetsXScale * ballBitmap.getWidth()),
                (int)(Utils.assetsXScale * ballBitmap.getHeight())));
        ballSprite.initializeFirstState();
        singlePlayerLogicProvider = new SinglePlayerLogicProvider(leftSlimeSprite, rightSlimeSprite, ballSprite);
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
                (int)((double)leftSlimeSprite.specialLevel / 1000 * Utils.specialButtonHalfSide), p2);

        canvas.drawCircle(Utils.rightSpecialButtonX, Utils.rightSpecialButtonY,
                Utils.specialButtonHalfSide, p);
        canvas.drawCircle(Utils.rightSpecialButtonX, Utils.rightSpecialButtonY,
                (int)((double)rightSlimeSprite.specialLevel / 1000 * Utils.specialButtonHalfSide), p2);

        leftSlimeSprite.draw(canvas);
        rightSlimeSprite.draw(canvas);
        ballSprite.draw(canvas);
        Typeface numberTypeface = Typeface.createFromAsset(this.context.getAssets(),
                        "fonts/Courier-BoldRegular.ttf");
        Log.i("goalLimit", Integer.toString(goalLimit));
        Paint numberPaint = new Paint();
        numberPaint.setTypeface(numberTypeface);
        numberPaint.setTextSize(Utils.screenHeight / 9);
        canvas.drawText(Integer.toString(goalLimit), Utils.goalLimitX, Utils.goalLimitY, numberPaint);

        canvas.drawText(Integer.toString(leftGoalNumber), Utils.leftGoalX, Utils.goalLimitY, numberPaint);
        canvas.drawText(Integer.toString(rightGoalNumber), Utils.rightGoalX, Utils.goalLimitY, numberPaint);
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
                singlePlayerLogicProvider.doSpecial(leftSlimeSprite,rightSlimeSprite);
                leftSlimeSprite.specialButtonIsHold = true;
            }
            else if (event.getX(index) < Utils.leftRightBorderX) {
                if (leftSlimeSprite.isLookRight) {
                    leftSlimeSprite.isLookRight = false;
                    leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                }
                leftSlimeSprite.isMoveLeft = true;
            } else if (event.getX(index) < Utils.rightUpBorderX) {
                if (!leftSlimeSprite.isLookRight) {
                    leftSlimeSprite.isLookRight = true;
                    leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                }
                leftSlimeSprite.isMoveRight = true;
            } else {
                if (leftSlimeSprite.y == Utils.slimeStartY) {
                    leftSlimeSprite.yVelocity = -Utils.initialYVelocity;
                }
            }
            return true;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            if (((event.getX(index) > (Utils.leftSpecialButtonX - Utils.specialButtonHalfSide)) &&
                    (event.getX(index) < (Utils.leftSpecialButtonX + Utils.specialButtonHalfSide))) &&
                    (((event.getY(index) < (Utils.leftSpecialButtonY + Utils.specialButtonHalfSide))) &&
                            (event.getY(index) > (Utils.leftSpecialButtonY - Utils.specialButtonHalfSide)))) {
                leftSlimeSprite.specialButtonIsHold = false;
            }
            else if (event.getX(index) < Utils.rightUpBorderX) {
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
}
