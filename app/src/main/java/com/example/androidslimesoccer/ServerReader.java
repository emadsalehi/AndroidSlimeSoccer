package com.example.androidslimesoccer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerReader extends Thread {

    SlimeSprite slimeSprite;
    Socket socket;
    InputStream inputStream;
    boolean isRunning = true;

    public ServerReader(SlimeSprite slimeSprite, Socket socket) {
        this.slimeSprite = slimeSprite;
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRunning) {
            String[] data = scanner.nextLine().split(",");
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
                    boolean value = data[2].equals("t") ? true : false;
                    if (metaData.equals("r"))
                        slimeSprite.isMoveRight = value;
                    else if (metaData.equals("l"))
                        slimeSprite.isMoveLeft = value;
                    else
                        slimeSprite.specialButtonIsHold = value;
                }
            }
        }
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
