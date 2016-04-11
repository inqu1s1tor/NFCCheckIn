package com.erminesoft.nfcpp.net;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.DataPermission;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.Event;

class BoltsEventManager {

    private DbBridge dbBridge;

    BoltsEventManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    Event addNewEvent(Event event) {
        try {
            event = Backendless.Persistence.save(event);
            if (event != null) {
                DataPermission.FIND.grantForAllRoles(event);
                dbBridge.saveEvent(event);
                return event;
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            Log.d("addNewEvent", "!RuntimeException = " + e.getMessage());
            return null;
        }
    }
}
