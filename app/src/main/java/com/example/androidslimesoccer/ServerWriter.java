package com.example.androidslimesoccer;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ServerWriter extends Thread {
    byte[] sendData;
    DatagramSocket serverSocket;

    public ServerWriter (byte[] sendData, DatagramSocket serverSocket) {
        this.sendData = sendData;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length
                    , InetAddress.getLocalHost(), Utils.serverPort);
            serverSocket.send(sendPacket);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
