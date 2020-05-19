package com.example.androidslimesoccer.multiplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidslimesoccer.R;
import com.example.androidslimesoccer.multiplayer.controller.WifiBroadcastReceiver;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class BroadcastReceiverActivity extends Activity implements WifiP2pManager.ConnectionInfoListener{
    WifiP2pManager mManager;
    Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    private WifiP2pDeviceList peers;//new ArrayList();
    private ListView mListView;
    private ArrayAdapter<String> WifiP2pArrayAdapter;
    private WifiP2pDevice ConnectedPartner;
    private String name;

    private String TAG = "##BoadcastReceiverAct";

    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            Log.d("INPeerListListener", "Works");
            ArrayList<WifiP2pDevice> peersNameFixed = new ArrayList<WifiP2pDevice>();

            for (WifiP2pDevice peer : peerList.getDeviceList()) {
                String newDeviceName = peer.deviceName.replace("[Phone]","");
                peer.deviceName = newDeviceName;
            }
            peers = new WifiP2pDeviceList(peerList);

            WifiP2pArrayAdapter.clear();
            for (WifiP2pDevice peer : peerList.getDeviceList()) {
                WifiP2pArrayAdapter.add(peer.deviceName); //+ "\n" + peer.deviceAddress
                Log.d("INPeerListListenerNAME:", peer.deviceName);
                // set textBox search_result.setText(peer.deviceName);
            }
            // If an AdapterView is backed by this data, notify it
            // of the change.  For instance, if you have a ListView of available
            // peers, trigger an update.
            //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
            //if (peers.size() == 0) {
            //no devices found
            //return;
            //}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_receiver);

        View bv = findViewById(R.id.broadcastActivity);
        bv.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));

        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiBroadcastReceiver(mManager, mChannel, this, peerListListener);  //Setting up Wifi Receiver

        if (mManager != null && mChannel != null) {
            mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                @Override
                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    if (group != null && mManager != null && mChannel != null) {
                        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "removeGroup onSuccess -");
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.d(TAG, "removeGroup onFailure -" + reason);
                            }
                        });
                    }
                }
            });
        }

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        try {
            Method m = mManager.getClass().getMethod("setDeviceName", Channel.class, String.class,
                    WifiP2pManager.ActionListener.class);
            m.invoke(mManager, mChannel, name, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, "Name change successful.");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d(TAG, "name change failed: " + reason);
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "No such method");
        }

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //will not provide info about who it discovered
            }

            @Override
            public void onFailure(int reasonCode) {

            }
        });

        mListView = findViewById(R.id.ListView);
        TextView emptyText = findViewById(android.R.id.empty);
        mListView.setEmptyView(emptyText);
        WifiP2pArrayAdapter = new ArrayAdapter<>(this, R.layout.fragment_peer, R.id.textView);

        mListView.setAdapter(WifiP2pArrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Log.d(TAG, "item clicked");
                TextView tv = ((LinearLayout) arg1).findViewById(R.id.textView);
                WifiP2pDevice device = null;
                for (WifiP2pDevice wd : peers.getDeviceList())
                {
                    if(wd.deviceName.equals(tv.getText()))
                        device = wd;
                }
                if (device != null)
                {
                    Log.d(TAG, " calling connectToPeer");
                    //Connect to selected peer
                    connectToPeer(device);
                } else {
                    //dialog.setMessage("Failed");
                    //dialog.show();
                }
            }

        });
    }


    public void connectToPeer(final WifiP2pDevice wifiPeer)
    {
        this.ConnectedPartner = wifiPeer;
        final WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiPeer.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener()  {
            public void onSuccess() {

            }
            public void onFailure(int reason) {
                //setClientStatus("Connection to " + targetDevice.deviceName + " failed");
                //TODO: Notify the user the connection failed.
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if (info.groupFormed) {
            Intent intent;
            if (info.isGroupOwner) {
                intent = new Intent(BroadcastReceiverActivity.this, ServerActivity.class);
            } else {
                intent = new Intent(BroadcastReceiverActivity.this, ClientActivity.class);
            }
            intent.putExtra("INFO", info);
            intent.putExtra("LEFT_SLIME_NAME", "classic");
            intent.putExtra("RIGHT_SLIME_NAME", "classic");
            intent.putExtra("GOAL_LIMIT", 5);
            startActivityForResult(intent, 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (mManager != null && mChannel != null) {
                mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                    @Override
                    public void onGroupInfoAvailable(WifiP2pGroup group) {
                        if (group != null && mManager != null && mChannel != null) {
                            mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "removeGroup onSuccess2 -");
                                }

                                @Override
                                public void onFailure(int reason) {
                                    Log.d(TAG, "removeGroup onFailure2 -" + reason);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    public void onRefresh(View view) {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //will not provide info about who it discovered
            }

            @Override
            public void onFailure(int reasonCode) {

            }
        });
    }
}
