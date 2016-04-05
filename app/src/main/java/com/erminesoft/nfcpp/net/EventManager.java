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
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.SimpleDateFormat;

final class EventManager {

    private int PAGE_SIZE = 100;

    private DbBridge dbBridge;

    public EventManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    void addNewEvent(Event realmEvent, final MainCallBack callback) {
        String myId = dbBridge.getMe().getObjectId();
        Backendless.Persistence.save(realmEvent, new AsyncCallback<Event>() {
            @Override
            public void handleResponse(Event event) {
                setPermissionGrantForAllRoles(event, callback);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                callback.onError(backendlessFault.toString());
            }
        });
    }


    private void setPermissionGrantForAllRoles(final Event event, final MainCallBack mainCallBack) {

        Backendless.Data.Permissions.FIND.grantForAllRoles(event, new AsyncCallback<Event>() {
            @Override
            public void handleResponse(Event event1) {
                dbBridge.saveEvent(event1);
                mainCallBack.onSuccess();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                mainCallBack.onError(backendlessFault.getMessage());
            }
        });
    }


    void getAllEventsByUserId(String ownerId, final MainCallBack mainCallBack) {
        String whereClause = "ownerId = '" + ownerId + "'";
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);
        query.setPageSize(PAGE_SIZE);

        Backendless.Data.of(Event.class).find(query, new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                mainCallBack.onSuccessGetEvents(eventBackendlessCollection.getData());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                mainCallBack.onError(backendlessFault.getMessage());
            }
        });
    }


    void getTodayEventsByUserId(String ownerId, long curTime, final MainCallBack callback) {
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_M_D_Y).format(curTime);
        String whereClause = "ownerId = '" + ownerId + "'  and  created > '" + curStringDate + "'";
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);
        query.setPageSize(PAGE_SIZE);

        Backendless.Data.of(Event.class).find(query, new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                callback.onSuccessGetEvents(eventBackendlessCollection.getData());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                callback.onError(backendlessFault.getMessage());
                Log.d("getTodayEvents", "backendlessFault = " + backendlessFault.toString());
            }
        });
    }


    void getAllEvents(final MainCallBack mainCallBack) {
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setPageSize(PAGE_SIZE);

        Backendless.Data.of(Event.class).find(query, new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                dbBridge.saveEvent(eventBackendlessCollection.getData());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                mainCallBack.onError(backendlessFault.getMessage());
            }
        });
    }
}
