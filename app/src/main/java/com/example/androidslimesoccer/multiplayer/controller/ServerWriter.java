package com.example.androidslimesoccer.multiplayer.controller;


import java.io.IOException;
import java.net.Socket;

public class ServerWriter extends Thread {
    private byte[] sendData;
    private Socket socket;

    public ServerWriter (byte[] sendData, Socket socket) {
        this.sendData = sendData;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            socket.getOutputStream().write(sendData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
