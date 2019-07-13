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
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerGameView extends GameView implements SurfaceHolder.Callback {

    MainThread thread;
    ServerReceiver serverReceiver;
    DatagramSocket serverSocket;
    Context context;
    SpecialSprite leftSpecialSprite;
    SpecialSprite rightSpecialSprite;
    SlimeSprite leftSlimeSprite, rightSlimeSprite;
    BallSprite ballSprite;
    MultiPlayerLogicProvider multiPlayerLogicProvider;
    Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    Bitmap goal = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
    Bitmap leftGoal; Bitmap rightGoal;
    Resources resources = getResources();
    int goalLimit = 5;
    int leftGoalNumber = 0, rightGoalNumebr = 0;
    int downX;



    public ServerGameView(Context context, String leftSlimeName, String rightSlimeName) {
        super(context);
        this.context = context;
        Utils.assetsXScale = (double)Utils.screenWidth / background.getWidth();
        Utils.assetsYScale = (double)Utils.screenHeight / background.getHeight();
        leftGoal = getResizedBitmap(goal, (int)(Utils.assetsXScale * goal.getWidth()),
                (int)(Utils.assetsYScale * goal.getHeight()));
        rightGoal = flipBitmap(leftGoal);

        Bitmap leftSlimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(leftSlimeName, "drawable", context.getPackageName()));
        leftSlimeSprite = new SlimeSprite(SlimeType.valueOf(leftSlimeName.toUpperCase()),
                getResizedBitmap(leftSlimeBitmap, (int)(Utils.assetsYScale * leftSlimeBitmap.getWidth()),
                        (int)(Utils.assetsYScale * leftSlimeBitmap.getHeight())), true);
        leftSlimeSprite.initializeFirstState();

        Bitmap rightSlimeBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier(rightSlimeName, "drawable", context.getPackageName()));
        rightSlimeSprite = new SlimeSprite(SlimeType.valueOf(rightSlimeName.toUpperCase()),
                getResizedBitmap(rightSlimeBitmap, (int)(Utils.assetsYScale * rightSlimeBitmap.getWidth()),
                        (int)(Utils.assetsYScale * rightSlimeBitmap.getHeight())), false);
        rightSlimeSprite.initializeFirstState();

        Utils.ballRatio = (int)(Utils.assetsYScale * ballBitmap.getWidth() / 2);
        Utils.ballStartX = Utils.screenWidth / 2 - Utils.ballRatio;
        Utils.slimeRatio = (int) (Utils.screenWidth / 20 * (Utils.assetsYScale / Utils.assetsXScale));
        ballSprite = new BallSprite(getResizedBitmap(ballBitmap,
                (int)(Utils.assetsYScale * ballBitmap.getWidth()),
                (int)(Utils.assetsYScale * ballBitmap.getHeight())));
        ballSprite.initializeFirstState();
        leftSpecialSprite = new SpecialSprite(SlimeType.valueOf(leftSlimeName.toUpperCase()), resources);
        rightSpecialSprite = new SpecialSprite(SlimeType.valueOf(rightSlimeName.toUpperCase()), resources);

        try {
            serverSocket = new DatagramSocket(Utils.serverPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        multiPlayerLogicProvider = new MultiPlayerLogicProvider(leftSlimeSprite, rightSlimeSprite
                , ballSprite, leftSpecialSprite, rightSpecialSprite, serverSocket);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        serverReceiver = new ServerReceiver(rightSlimeSprite, serverSocket);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        serverReceiver.start();
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
                serverReceiver.setRunning(false);
                serverReceiver.join();
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
                (int)((double)rightSlimeSprite.specialLevel / 1000 * Utils.specialButtonHalfSide - 7), p2);

        leftSlimeSprite.draw(canvas);
        rightSlimeSprite.draw(canvas);
        ballSprite.draw(canvas);
        leftSpecialSprite.draw(canvas);
        rightSpecialSprite.draw(canvas);
        Typeface numberTypeface = Typeface.createFromAsset(this.context.getAssets(),
                "fonts/Courier-BoldRegular.ttf");
        Paint numberPaint = new Paint();
        numberPaint.setTypeface(numberTypeface);
        numberPaint.setTextSize(Utils.screenHeight / 9);
        canvas.drawText(Integer.toString(goalLimit), Utils.goalLimitX, Utils.goalLimitY, numberPaint);

        canvas.drawText(Integer.toString(multiPlayerLogicProvider.slime1Goals),
                Utils.leftGoalX, Utils.goalLimitY, numberPaint);
        canvas.drawText(Integer.toString(multiPlayerLogicProvider.slime2Goals),
                Utils.rightGoalX, Utils.goalLimitY, numberPaint);
    }


    @Override
    public void update() {
        multiPlayerLogicProvider.update();
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
}
