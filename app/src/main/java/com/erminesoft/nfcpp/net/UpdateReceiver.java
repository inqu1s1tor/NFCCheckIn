package com.erminesoft.nfcpp.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.erminesoft.nfcpp.util.NetworkUtil;

/**
 * Created by Aleks on 25.03.2016.
 */
public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        checkInetAndSync(context);
    }

    private void checkInetAndSync(Context context) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status == "Wifi enabled" || status == "Mobile data enabled") {
            SyncService.start(context);
        }
    }
}
