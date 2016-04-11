package com.erminesoft.nfcpp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class SystemUtils {

    public static boolean isNetworkConnected(Context pContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetooth = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        NetworkInfo wimax = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

        if (wifi == null && mobile == null && bluetooth == null
                && wimax == null) {
            return false;
        }

        if (wifi != null && wifi.isConnected()) {
            return true;
        }

        if (mobile != null && mobile.isConnected()) {
            return true;
        }

        if (bluetooth != null && bluetooth.isConnected()) {
            return true;
        }

        if (wimax != null && wimax.isConnected()) {
            return true;
        }

        return false;
    }
}
