package com.example.androidslimesoccer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import java.net.Socket;

public class ClientActivity extends Activity {

    ClientGameView clientGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String leftSlimeName = getIntent().getStringExtra("LEFT_SLIME_NAME");
        String rightSlimeName = getIntent().getStringExtra("RIGHT_SLIME_NAME");
        String ip = getIntent().getStringExtra("IP");
        System.out.println(ip);
        ClientConnectorThread clientConnectorThread = new ClientConnectorThread(ip);
        clientConnectorThread.start();
        try {
            synchronized (clientConnectorThread) {
                if (!clientConnectorThread.isConnected()) {
                    clientConnectorThread.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Socket socket = clientConnectorThread.getSocket();
        int goalLimit = getIntent().getIntExtra("GOAL_LIMIT", 5);
        clientGameView = new ClientGameView(this, leftSlimeName, rightSlimeName, socket);
        setContentView(clientGameView);
    }
}
