package com.example.androidslimesoccer;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientActivity extends Activity {

    ClientGameView clientGameView;
    WifiP2pInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String leftSlimeName = getIntent().getStringExtra("LEFT_SLIME_NAME");
        String rightSlimeName = getIntent().getStringExtra("RIGHT_SLIME_NAME");
        info = (WifiP2pInfo) getIntent().getExtras().get("INFO");
        int goalLimit = getIntent().getIntExtra("GOAL_LIMIT", 5);
        clientGameView = new ClientGameView(this, leftSlimeName, rightSlimeName);
        connectToOwner.execute();
        setContentView(clientGameView);
    }

    AsyncTask<Void,Void,Void> connectToOwner = new AsyncTask<Void, Void, Void>()
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            InetAddress groupOwner = info.groupOwnerAddress;
            Socket socket = new Socket();
            try
            {
                socket.bind(null);
                socket.connect(new InetSocketAddress(groupOwner.getHostAddress(), Utils.serverPort));
                Log.d("Chat","Name sent to Owner");
                clientGameView.startGame(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    };


}
