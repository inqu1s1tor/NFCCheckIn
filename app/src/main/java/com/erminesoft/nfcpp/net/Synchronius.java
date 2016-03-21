package com.erminesoft.nfcpp.net;

import android.app.IntentService;
import android.content.Intent;

import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Synchronius extends IntentService {

    private final static String SERVICE_NAME = "synchronius";
    private final DbBridge dbBridge;
    private final NetBridge netBridge;
    private Map<Integer, Event> unsentEvents;
    private int processingEventId;

    public Synchronius() {
        super(SERVICE_NAME);
        NfcApplication application = (NfcApplication) getApplicationContext();
        dbBridge = application.getDbBridge();
        netBridge = application.getNetBridge();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    private void extractUnsentEvents() {
        List<Event> eventList = dbBridge.getAllEvents();
        unsentEvents = new HashMap<>(eventList.size());

        for (Event event : eventList) {
            processingEventId = event.getCreationTime();
            unsentEvents.put(processingEventId, event);
        }
    }

    private void sendEvent() {
        Event event = unsentEvents.get(processingEventId);
        //netBridge.addNewEvent();
    }

    private final class NetCallback extends SimpleMainCallBack {

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(String error) {

        }
    }

}
