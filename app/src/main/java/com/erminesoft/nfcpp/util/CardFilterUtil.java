package com.erminesoft.nfcpp.util;

import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.RealmEvent;

public final class CardFilterUtil {

    private static final int TWELVE_SEC = 20;

    public static boolean isDoubleCheckIn(DbBridge dbBridge, String cardId) {
        RealmEvent realmEvent = dbBridge.getLastEventByCardId(cardId);

        int currentTime = (int) (System.currentTimeMillis() / 1000);
        if (realmEvent == null) {
            return false;
        }

        if (currentTime - realmEvent.getCreationTime() < TWELVE_SEC) {
            return true;
        }

        return false;
    }

}
