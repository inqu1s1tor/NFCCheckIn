package com.erminesoft.nfcpp.core;

import android.app.Application;


import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.db.DbManager;
import com.erminesoft.nfcpp.net.NetManagerFacade;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;


public final class NfcApplication extends Application {

    private SharedHelper sharedHelper;
    private NetBridge netBridge;
    private DbBridge dbBridge;

    public Tracker getmTracker() {
        return mTracker;
    }

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedHelper = new SharedHelper(this);
        dbBridge = new DbManager(this, sharedHelper);
        netBridge = new NetManagerFacade(this, sharedHelper, dbBridge);
        getDefaultTracker();
    }

    public NetBridge getNetBridge() {
        return netBridge;
    }

    public SharedHelper getSharedHelper() {
        return sharedHelper;
    }

    public DbBridge getDbBridge() {
        return dbBridge;
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }


}
