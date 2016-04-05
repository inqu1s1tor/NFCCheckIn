package com.erminesoft.nfcpp.net;

import com.backendless.Backendless;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.Event;

public class BoltsEventManager {

    private DbBridge dbBridge;

    public BoltsEventManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    Event addNewEvent(Event event) {
        try {
            event = Backendless.Persistence.save(event);
            if (event != null) {
                Backendless.Data.Permissions.FIND.grantForAllRoles(event);
                dbBridge.saveEvent(event);
                return event;
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            return null;
        }
    }
}
