package com.erminesoft.nfcpp.net;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.Event;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Aleks on 14.03.2016.
 */
final class EventManager {

    private DbBridge dbBridge;

    public EventManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    void addNewEvent(String idCard, final MainCallBack callback) {

        Event event = new Event();
        event.setIdCard(idCard);
//        event.setIsSent(true);
//        event.setObjectId("");
//        event.setCreationTime(125);
//        event.setCreated(new Date(System.currentTimeMillis()));
        

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
                callback.onError(backendlessFault.toString());
            }
        });

    }


    private void setPermissionGrantForAllRoles(final Event event) {
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


    void getAllEvents(String ownerId, final MainCallBack callback) {
        String whereClause = "ownerId = '" + ownerId + "'";
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);

        Backendless.Data.of(Event.class).find(query, new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                List<Event> eventList = eventBackendlessCollection.getData();
                Log.d("getAllevent", "eventList.size() = " + eventList.size());
                for (Event ev : eventList){
                    Log.d("getAllEvents", "ev.getCreated() = " + ev.getCreated());
                }
                callback.onSuccessGetEvents(eventList);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("getAllEvents", "backendlessFault = " + backendlessFault.toString());
            }
        });
    }


    void getTodayEvents(String ownerId, long curTime, final MainCallBack callback) {
        String curStringDate = new SimpleDateFormat("MM.dd.yyyy").format(curTime);
        String whereClause = "ownerId = '" + ownerId + "'  and  created > '" + curStringDate + "'";
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);

        Backendless.Data.of(Event.class).find(query, new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                List<Event> eventList = eventBackendlessCollection.getData();
                Log.d("getTodayEvents", "eventList.size() = " + eventList.size());
                for (Event ev : eventList){
                    Log.d("getTodayEvents", "ev.getCreated() = " + ev.getCreated());
                }
                callback.onSuccessGetEvents(eventList);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("getTodayEvents", "backendlessFault = " + backendlessFault.toString());
            }
        });
    }

}
