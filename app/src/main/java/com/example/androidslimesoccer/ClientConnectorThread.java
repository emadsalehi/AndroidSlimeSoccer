package com.example.androidslimesoccer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientConnectorThread extends Thread {

    Socket socket;
    boolean isConnected = false;
    String dstAddress;

    public ClientConnectorThread(String address) {
        this.dstAddress = address;
    }

    @Override
    public void run() {
        try {
            if (dstAddress != null) {
                socket = new Socket();
                socket.connect(new InetSocketAddress(dstAddress, Utils.serverPort), 100000);
                isConnected = true;
                this.notify();
            }
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
