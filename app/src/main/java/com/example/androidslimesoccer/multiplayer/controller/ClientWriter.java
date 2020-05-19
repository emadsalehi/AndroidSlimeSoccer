package com.example.androidslimesoccer.multiplayer.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientWriter extends Thread {
    byte[] sendData;
    Socket socket;

    public ClientWriter(byte[] sendData, Socket socket) {
        this.sendData = sendData;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            socket.getOutputStream().write(sendData);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
