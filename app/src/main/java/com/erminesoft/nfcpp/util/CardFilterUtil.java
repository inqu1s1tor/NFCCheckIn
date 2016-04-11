package com.erminesoft.nfcpp.util;

import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.Event;

public final class CardFilterUtil {

    private static final int TWELVE_SEC = 20;

    public static boolean isDoubleCheckIn(DbBridge dbBridge, String cardId) {
        Event event = dbBridge.getLastEventByCardId(cardId);
        if (event == null) {
            return false;
        }

        int currentTime = (int) (System.currentTimeMillis() / 1000);

        return currentTime - event.getCreationTime() < TWELVE_SEC;
    }

}
