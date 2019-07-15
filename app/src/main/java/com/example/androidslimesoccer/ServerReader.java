package com.example.androidslimesoccer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerReader extends Thread {

    SlimeSprite slimeSprite;
    DatagramSocket serverSocket;
    boolean isRunning = true;

    public ServerReader(SlimeSprite slimeSprite, DatagramSocket serverSocket) {
        this.slimeSprite = slimeSprite;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            byte [] receiveData = new byte[8];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (isRunning) {
                serverSocket.receive(receivePacket);
                String[] data = new String(receivePacket.getData()).split(",");
                Utils.IPAddress = receivePacket.getAddress();
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
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
