package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HostJoinSelectActivity extends Activity {

    Intent client, server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_join_select);
        client = new Intent(this, ClientActivity.class);
        server = new Intent(this, ServerActivity.class);
    }

    public void hostClick(View v) {
        server.putExtra("LEFT_SLIME_NAME", "classic");
        server.putExtra("RIGHT_SLIME_NAME", "classic");
        startActivity(server);
    }

    public void joinClick(View v) {
        client.putExtra("LEFT_SLIME_NAME", "classic");
        client.putExtra("RIGHT_SLIME_NAME", "classic");
        startActivity(client);
    }
}
