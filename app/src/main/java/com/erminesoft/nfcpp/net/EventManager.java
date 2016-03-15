package com.erminesoft.nfcpp.net;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.Event;

import java.util.List;

/**
 * Created by Aleks on 14.03.2016.
 */
final class EventManager {

    private DbBridge dbBridge;

    public EventManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    void addNewEvent(String idCard, final MainCallBack callback){

        Event event = new Event();
        event.setIdCard(idCard);

        Backendless.Persistence.save(event, new AsyncCallback<Event>() {
            @Override
            public void handleResponse(Event event) {
                Log.d("addNewEvent", "event.getObjectId() = " + event.getObjectId());
                setPermissionGrantForAllRoles(event);
                callback.onSuccess();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("addNewEvent", "backendlessFault = " + backendlessFault.toString());
            }
        });

    }


    private void setPermissionGrantForAllRoles(final Event event){
        Backendless.Data.Permissions.FIND.grantForAllRoles(event, new AsyncCallback<Event>() {
            @Override
            public void handleResponse(Event event1) {
                Log.d("grantForAllRoles", "handleResponse");
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("grantForAllRoles", "backendlessFault = " + backendlessFault.toString());
            }
        });
    }
    void getAllEvents(final MainCallBack callback) {
        Backendless.Data.of( Event.class ).find(new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                List<Event> eventList = eventBackendlessCollection.getData();
                Log.d("getAllevent", "eventList.size() = " + eventList.size());
                callback.onSuccessGetEvents(eventList);

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("getAllEvents", "backendlessFault = " + backendlessFault.toString());
            }
        });
    }
}
