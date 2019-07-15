package com.example.androidslimesoccer;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientReader extends Thread {
    boolean isRunning = true;
    DatagramSocket clientSocket;
    SlimeSprite leftSlimeSprite, rightSlimeSprite;
    SpecialSprite leftSpecialSprite, rightSpecialSprite;
    BallSprite ballSprite;
    int leftGoal = 0, rightGoal = 0;

    public ClientReader (SlimeSprite leftSlimeSprite, SlimeSprite rightSlimeSprite,
                         SpecialSprite leftSpecialSprite, SpecialSprite rightSpecialSprite, BallSprite ballSprite) {
        this.leftSlimeSprite = leftSlimeSprite;
        this.rightSlimeSprite = rightSlimeSprite;
        this.leftSpecialSprite = leftSpecialSprite;
        this.rightSpecialSprite = rightSpecialSprite;
        this.ballSprite = ballSprite;
    }

    @Override
    public void run() {
        try {
            byte[] receiveData = new byte[128];
            clientSocket = new DatagramSocket();
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (isRunning) {
                clientSocket.receive(receivePacket);
                String[] data = new String(receivePacket.getData()).split(",");
                if(data[0].equals("s")) {
                    leftGoal = Integer.valueOf(data[1]);
                    rightGoal = Integer.valueOf(data[2]);

                    ballSprite.x = Utils.screenWidth / Integer.valueOf(data[3]);
                    ballSprite.y = Utils.screenHeight / Integer.valueOf(data[4]);

                    leftSlimeSprite.x = Utils.screenWidth / Integer.valueOf(data[5]);
                    leftSlimeSprite.y = Utils.screenHeight / Integer.valueOf(data[6]);
                    leftSlimeSprite.specialLevel = Integer.valueOf(data[7]);
                    boolean isLookRight = data[8].equals("true") ? true : false;
                    if (isLookRight && !leftSlimeSprite.isLookRight) {
                        leftSlimeSprite.isLookRight = true;
                        leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                    } else if (!isLookRight && leftSlimeSprite.isLookRight) {
                        leftSlimeSprite.isLookRight = false;
                        leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                    }

                    rightSlimeSprite.x = Utils.screenWidth / Integer.valueOf(data[9]);
                    rightSlimeSprite.y = Utils.screenHeight / Integer.valueOf(data[10]);
                    rightSlimeSprite.specialLevel = Integer.valueOf(data[11]);
                    isLookRight = data[12].equals("true") ? true : false;
                    if (isLookRight && !rightSlimeSprite.isLookRight) {
                        rightSlimeSprite.isLookRight = true;
                        rightSlimeSprite.slimeImage = flipBitmap(rightSlimeSprite.slimeImage);
                    } else if (!isLookRight && rightSlimeSprite.isLookRight) {
                        rightSlimeSprite.isLookRight = false;
                        rightSlimeSprite.slimeImage = flipBitmap(rightSlimeSprite.slimeImage);
                    }

                    boolean onDraw = data[13].equals("true") ? true : false;
                    leftSpecialSprite.isOnDraw = onDraw;
                    int indexCounter = 14;
                    if(onDraw) {
                        leftSpecialSprite.x = Utils.screenWidth / Integer.valueOf(data[indexCounter]);
                        indexCounter++;
                        leftSpecialSprite.y = Utils.screenHeight / Integer.valueOf(data[indexCounter]);
                        indexCounter++;
                    }
                    onDraw = data[indexCounter].equals("true") ? true : false;
                    indexCounter++;
                    rightSpecialSprite.isOnDraw = onDraw;
                    if(onDraw) {
                        rightSpecialSprite.x = Utils.screenWidth / Integer.valueOf(data[17]);
                        indexCounter++;
                        rightSpecialSprite.y = Utils.screenHeight / Integer.valueOf(data[18]);
                    }
                }
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public int getLeftGoal() {
        return leftGoal;
    }

    public int getRightGoal() {
        return rightGoal;
    }

    public Bitmap flipBitmap (Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
}
