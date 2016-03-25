package com.erminesoft.nfcpp.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.erminesoft.nfcpp.util.NetworkUtil;

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (NetworkUtil.isConnected(context)) {
            SyncService.start(context);
        }
    }
}
