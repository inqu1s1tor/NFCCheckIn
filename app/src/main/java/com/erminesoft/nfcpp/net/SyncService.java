package com.erminesoft.nfcpp.net;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Event;

import java.util.ArrayDeque;
import java.util.Queue;

public final class SyncService extends IntentService {

    private final static String SERVICE_NAME = "synchronius";
    private DbBridge dbBridge;
    private NetBridge netBridge;
    private Queue<Event> unsentEvent;
    private boolean isWork;

    public static void start(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        context.startService(intent);
    }

    public SyncService() {
        super(SERVICE_NAME);
        isWork = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (isWork) {
            return;
        }

        isWork = true;
        initModules();
        extractUnsentEvents();
        sendEvent();
    }

    private void initModules() {
        NfcApplication application = (NfcApplication) getApplicationContext();
        dbBridge = application.getDbBridge();
        netBridge = application.getNetBridge();
    }

    private void extractUnsentEvents() {
        unsentEvent = new ArrayDeque<>(dbBridge.getUnsentEvents());
    }

    private void sendEvent() {
        Event event = unsentEvent.poll();
        if (event == null) {
            isWork = false;
            return;
        }

        Log.d("sendEvent", "event.getIsSent()=" + event.getIsSent());
        Log.d("sendEvent", "event.getCreationTime()=" + event.getCreationTime());
        Log.d("sendEvent", "event.getIdCard()=" + event.getIdCard());

        Event savedEvent = netBridge.addNewEventBolt(event);
        Log.d("sendEvent", "savedEvent.getCreationTime()=" + savedEvent.getCreationTime());
        Log.d("sendEvent", "savedEvent.getIdCard()=" + savedEvent.getIdCard());

        if (savedEvent != null) {
            savedEvent.setIsSent(true);
            dbBridge.saveEvent(savedEvent);
           // sendEvent();
        }
    }
}
