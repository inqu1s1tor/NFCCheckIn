package com.erminesoft.nfcpp.core;

import android.app.Application;

import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.db.DbManager;
import com.erminesoft.nfcpp.net.NetManagerFacade;


public final class NfcApplication extends Application {

    private SharedHelper sharedHelper;
    private NetBridge netBridge;
    private DbBridge dbBridge;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedHelper = new SharedHelper(this);
        dbBridge = new DbManager();
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
}
