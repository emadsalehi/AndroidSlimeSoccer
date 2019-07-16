package com.example.androidslimesoccer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectorThread extends Thread {
    Socket socket = null;
    boolean isConnected = false;

    public ServerConnectorThread(){
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(Utils.serverPort);
            socket = serverSocket.accept();
            isConnected = true;
            this.notify();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
