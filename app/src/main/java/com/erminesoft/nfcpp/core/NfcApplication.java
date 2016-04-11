package com.erminesoft.nfcpp.core;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
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
    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);

        sharedHelper = new SharedHelper(this);
        dbBridge = new DbManager(sharedHelper);
        netBridge = new NetManagerFacade(this, sharedHelper, dbBridge);
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

    public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }


}
