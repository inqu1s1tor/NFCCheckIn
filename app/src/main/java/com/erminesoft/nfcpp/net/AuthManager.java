package com.erminesoft.nfcpp.net;

import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.bridge.DbBridge;

/**
 * Created by Aleks on 10.03.2016.
 */
final class AuthManager {

    private SharedHelper sharedHelper;
    private DbBridge dbBridge;

    AuthManager (SharedHelper sharedHelper, DbBridge dbBridge) {
        this.sharedHelper = sharedHelper;
        this.dbBridge = dbBridge;
    }
}
