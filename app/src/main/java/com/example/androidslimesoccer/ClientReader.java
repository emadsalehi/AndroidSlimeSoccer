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

    public ClientReader (DatagramSocket clientSocket, SlimeSprite leftSlimeSprite, SlimeSprite rightSlimeSprite,
                         SpecialSprite leftSpecialSprite, SpecialSprite rightSpecialSprite, BallSprite ballSprite) {
        this.clientSocket = clientSocket;
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
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (isRunning) {
                clientSocket.receive(receivePacket);
                String[] data = String.valueOf(receivePacket.getData()).split(",");
                leftGoal = Integer.valueOf(data[0]);
                rightGoal = Integer.valueOf(data[1]);

                ballSprite.x = Utils.screenWidth / Integer.valueOf(data[2]);
                ballSprite.y = Utils.screenHeight / Integer.valueOf(data[3]);

                leftSlimeSprite.x = Utils.screenWidth / Integer.valueOf(data[4]);
                leftSlimeSprite.y = Utils.screenHeight / Integer.valueOf(data[5]);
                leftSlimeSprite.specialLevel = Integer.valueOf(data[6]);
                boolean isLookRight = data[7].equals("true") ? true : false;
                if (isLookRight && !leftSlimeSprite.isLookRight) {
                    leftSlimeSprite.isLookRight = true;
                    leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                } else if (!isLookRight && leftSlimeSprite.isLookRight) {
                    leftSlimeSprite.isLookRight = false;
                    leftSlimeSprite.slimeImage = flipBitmap(leftSlimeSprite.slimeImage);
                }

                rightSlimeSprite.x = Utils.screenWidth / Integer.valueOf(data[8]);
                rightSlimeSprite.y = Utils.screenHeight / Integer.valueOf(data[9]);
                rightSlimeSprite.specialLevel = Integer.valueOf(data[10]);
                isLookRight = data[11].equals("true") ? true : false;
                if (isLookRight && !rightSlimeSprite.isLookRight) {
                    rightSlimeSprite.isLookRight = true;
                    rightSlimeSprite.slimeImage = flipBitmap(rightSlimeSprite.slimeImage);
                } else if (!isLookRight && rightSlimeSprite.isLookRight) {
                    rightSlimeSprite.isLookRight = false;
                    rightSlimeSprite.slimeImage = flipBitmap(rightSlimeSprite.slimeImage);
                }

                boolean onDraw = data[12].equals("true") ? true : false;
                leftSpecialSprite.isOnDraw = onDraw;
                leftSpecialSprite.x = Utils.screenWidth / Integer.valueOf(data[13]);
                leftSpecialSprite.y = Utils.screenHeight / Integer.valueOf(data[14]);

                onDraw = data[15].equals("true") ? true : false;
                rightSpecialSprite.isOnDraw = onDraw;
                rightSpecialSprite.x = Utils.screenWidth / Integer.valueOf(data[16]);
                rightSpecialSprite.y = Utils.screenHeight / Integer.valueOf(data[17]);
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
