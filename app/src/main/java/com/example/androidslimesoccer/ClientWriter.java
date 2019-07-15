package com.example.androidslimesoccer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientWriter extends Thread {
    byte[] sendData;
    DatagramSocket socket;

    public ClientWriter(byte[] sendData) {
        this.sendData = sendData;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length
                    , InetAddress.getLocalHost(), Utils.serverPort);
            socket.send(sendPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
