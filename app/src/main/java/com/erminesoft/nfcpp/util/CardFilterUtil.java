package com.erminesoft.nfcpp.util;

import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.Event;

public final class CardFilterUtil {

    private static final int TWELVE_SEC = 20;

    public static boolean isDoubleCheckIn(DbBridge dbBridge, String cardId) {
        Event event = dbBridge.getLastEventByCardId(cardId);

        int currentTime = (int) (System.currentTimeMillis() / 1000);
        if (event == null) {
            return false;
        }

        if (currentTime - event.getCreationTime() < 20) {
            return true;
        }

        return false;
    }

}
