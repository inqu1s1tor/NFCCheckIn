package com.erminesoft.nfcpp.net;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.RealmEvent;

import java.util.ArrayDeque;
import java.util.Queue;

public final class SyncService extends IntentService {

    private final static String SERVICE_NAME = "synchronius";
    private DbBridge dbBridge;
    private NetBridge netBridge;
    private Queue<RealmEvent> unsentRealmEvent;
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
        unsentRealmEvent = new ArrayDeque<>(dbBridge.getUnsentEvents());
    }

    private void sendEvent() {
        RealmEvent realmEvent = unsentRealmEvent.poll();
        if (realmEvent == null) {
            isWork = false;
            return;
        }

        Log.d("sendEvent", "realmEvent.getIsSent()=" + realmEvent.getIsSent());
        Log.d("sendEvent", "realmEvent.getCreationTime()=" + realmEvent.getCreationTime());
        Log.d("sendEvent", "realmEvent.getIdCard()=" + realmEvent.getIdCard());

        netBridge.addNewEvent(realmEvent, new NetCallback());

//        RealmEvent savedRealmEvent = netBridge.addNewEventBolt(realmEvent);
//        Log.d("sendEvent", "savedRealmEvent.getCreationTime()=" + savedRealmEvent.getCreationTime());
//        Log.d("sendEvent", "savedRealmEvent.getIdCard()=" + savedRealmEvent.getIdCard());

//        if (savedRealmEvent != null) {
//            savedRealmEvent.setIsSent(true);
//            dbBridge.saveEvent(savedRealmEvent);
//           // sendEvent();
//        }
    }

    private final class NetCallback extends SimpleMainCallBack {

//        @Override
//        public void onSuccessGetEvent(RealmEvent realmEvent) {
//            Log.d("!!", "onSuccessGetEvent");
//        }


        @Override
        public void onSuccess() {
            Log.d("!!", "onSuccess()");
            sendEvent();
        }
    }
}
