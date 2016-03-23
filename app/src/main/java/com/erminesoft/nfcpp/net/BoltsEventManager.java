package com.erminesoft.nfcpp.net;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.Event;

public class BoltsEventManager {

    Event addNewEvent(Event eventToSave) {

//        Event event = new Event();
//        event.setIdCard(idCard);
//        event.setIsSent(true);
//        event.setObjectId("");
//        event.setCreationTime(125);
//        event.setCreated(new Date(System.currentTimeMillis()));


        return Backendless.Persistence.save(eventToSave);


//        , new AsyncCallback<Event>() {
//            @Override
//            public void handleResponse(Event event) {
//                Log.d("addNewEvent", "event.getObjectId() = " + event.getObjectId());
//                setPermissionGrantForAllRoles(event);
//                callback.onSuccess();
//            }
//
//            @Override
//            public void handleFault(BackendlessFault backendlessFault) {
//                Log.d("addNewEvent", "backendlessFault = " + backendlessFault.toString());
//                callback.onError(backendlessFault.toString());
//            }
//        });

    }
}
