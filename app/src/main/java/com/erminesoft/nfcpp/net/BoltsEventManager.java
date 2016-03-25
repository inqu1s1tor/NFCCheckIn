package com.erminesoft.nfcpp.net;

import com.backendless.Backendless;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.util.SystemUtils;

public class BoltsEventManager {

    private DbBridge dbBridge;

    public BoltsEventManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    RealmEvent addNewEvent(RealmEvent realmEvent) {
        Event event = EventManager.EventConverter.realmEventToClearEvent(realmEvent);
        try {
            event = Backendless.Persistence.save(event);
            if (event != null) {
                Backendless.Data.Permissions.FIND.grantForAllRoles(event);
                RealmEvent updatedEvent = EventManager.EventConverter.clearEventToRealmEvent(event);
                dbBridge.saveEvent(updatedEvent);
                return updatedEvent;
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            return null;
        }
    }
}
