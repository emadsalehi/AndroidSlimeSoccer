package com.example.androidslimesoccer.multiplayer.controller;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.androidslimesoccer.model.SlimeSprite;
import com.example.androidslimesoccer.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReader extends Thread {

    SlimeSprite slimeSprite;
    Socket socket;
    boolean isRunning = true;

    public ServerReader(SlimeSprite slimeSprite, Socket socket) {
        this.slimeSprite = slimeSprite;
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Horraaaaaaaaaaaaaaaaaaaaaaaaaa");
            while (isRunning) {
                String receive = bufferedReader.readLine();
                System.out.println(receive);
                String[] data = receive.split(",");
                System.out.println(receive);
                if (data[0].equals("c")) {
                    if (data[1].equals("j")) {
                        if (slimeSprite.y == Utils.slimeStartY - slimeSprite.slimeImage.getHeight()
                                && slimeSprite.yVelocity == 0) {
                            slimeSprite.yVelocity = -Utils.initialYVelocity;
                        }
                    } else if (data[1].equals("s")) {
                        slimeSprite.enableSpecial();
                    } else {
                        String metaData = data[1];
                        boolean value = data[2].equals("t");
                        if (metaData.equals("r")) {
                            slimeSprite.isMoveRight = value;
                            if (!slimeSprite.isLookRight && value) {
                                slimeSprite.slimeImage = flipBitmap(slimeSprite.slimeImage);
                                slimeSprite.isLookRight = true;
                            }
                        } else if (metaData.equals("l")) {
                            slimeSprite.isMoveLeft = value;
                            if (slimeSprite.isLookRight && value) {
                                slimeSprite.slimeImage = flipBitmap(slimeSprite.slimeImage);
                                slimeSprite.isLookRight = false;
                            }
                        } else
                            slimeSprite.specialButtonIsHold = value;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap flipBitmap (Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
