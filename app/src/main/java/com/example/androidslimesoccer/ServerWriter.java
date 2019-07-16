package com.example.androidslimesoccer;


import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ServerWriter extends Thread {
    byte[] sendData;
    Socket socket;

    public ServerWriter (byte[] sendData, Socket socket) {
        this.sendData = sendData;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            socket.getOutputStream().write(sendData);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
