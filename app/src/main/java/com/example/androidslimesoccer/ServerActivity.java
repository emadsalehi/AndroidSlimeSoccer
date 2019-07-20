package com.example.androidslimesoccer;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerActivity extends Activity {

    ServerGameView serverGameView;
    WifiP2pInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String leftSlimeName = getIntent().getStringExtra("LEFT_SLIME_NAME");
        String rightSlimeName = getIntent().getStringExtra("RIGHT_SLIME_NAME");
        int goalLimit = getIntent().getIntExtra("GOAL_LIMIT", 5);
        info = (WifiP2pInfo) getIntent().getExtras().get("INFO");
        serverGameView = new ServerGameView(this, leftSlimeName, rightSlimeName);
        getClientInfo.execute();
        setContentView(serverGameView);
    }

    AsyncTask<Void,Void,Void> getClientInfo = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Log.d("Chat","waiting for client");
                ServerSocket serverSocket = new ServerSocket(Utils.serverPort);
                Socket clientSocket = serverSocket.accept();
                Log.d("Chat","Client found");
                serverGameView.startGame(clientSocket);
            }catch(Exception e){
                Log.d("Chat","Exception in getClientInfo" + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

    };
}
