package com.example.androidslimesoccer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import java.net.Socket;


public class ServerActivity extends Activity {

    ServerGameView serverGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String leftSlimeName = getIntent().getStringExtra("LEFT_SLIME_NAME");
        String rightSlimeName = getIntent().getStringExtra("RIGHT_SLIME_NAME");
        int goalLimit = getIntent().getIntExtra("GOAL_LIMIT", 5);
        System.out.println(getIntent().getStringExtra("IP"));
        ServerConnectorThread serverConnectorThread = new ServerConnectorThread();
        serverConnectorThread.start();
        synchronized (serverConnectorThread) {
            try {
                if (!serverConnectorThread.isConnected()) {
                    serverConnectorThread.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Socket socket = serverConnectorThread.getSocket();
        serverGameView = new ServerGameView(this, leftSlimeName, rightSlimeName, socket);
        setContentView(serverGameView);
    }
}
