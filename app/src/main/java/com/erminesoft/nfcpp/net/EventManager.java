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
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.annotations.PrimaryKey;

final class EventManager {

    private int PAGE_SIZE = 100;

    private DbBridge dbBridge;

    public EventManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    void addNewEvent(RealmEvent realmEvent, final MainCallBack callback) {
        String myId = dbBridge.getMe().getObjectId();
        Event event = EventConverter.realmEventToClearEvent(myId, realmEvent);
        Backendless.Persistence.save(event, new AsyncCallback<Event>() {
            @Override
            public void handleResponse(Event event) {
                Log.d("save", "handleResponse");
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
                RealmEvent realmEvent = EventConverter.clearEventToRealmEvent(event);
                Log.d("handleResponse", "*getCreationTime" + realmEvent.getCreationTime());
                dbBridge.saveEvent(realmEvent);
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
        Log.d("getAllEvents", "*whereClause=" + whereClause);
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);
        query.setPageSize(PAGE_SIZE);

        Backendless.Data.of(Event.class).find(query, new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                List<Event> eventList = eventBackendlessCollection.getData();
                Log.d("getAllEvents", "*eventList.size()=" + eventList.size());
                List<RealmEvent> realmEvents = new ArrayList<RealmEvent>(eventList.size());
                for (Event ev : eventList) {
                    realmEvents.add(EventConverter.clearEventToRealmEvent(ev));
                }
                mainCallBack.onSuccessGetEvents(realmEvents);
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
        Log.d("getTodayEvents", "*whereClause="+whereClause);
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);
        query.setPageSize(PAGE_SIZE);

        Backendless.Data.of(Event.class).find(query, new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                List<Event> eventList = eventBackendlessCollection.getData();
                List<RealmEvent> realmEvents = new ArrayList<RealmEvent>(eventList.size());
                for (Event ev : eventList) {
                    realmEvents.add(EventConverter.clearEventToRealmEvent(ev));
                }
                callback.onSuccessGetEvents(realmEvents);
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
                List<Event> eventList = eventBackendlessCollection.getData();
                List<RealmEvent> realmEvents = new ArrayList<RealmEvent>(eventList.size());
                for (Event ev : eventList) {
                    realmEvents.add(EventConverter.clearEventToRealmEvent(ev));
                }
                dbBridge.saveEvent(realmEvents);
//                mainCallBack.onSuccessGetEvents(realmEvents);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                mainCallBack.onError(backendlessFault.getMessage());
            }
        });
    }


    static final class EventConverter {

        static Event realmEventToClearEvent(String ownerId, RealmEvent realmRealmEvent) {
            Event clearEvent = new Event();
            clearEvent.setCreationTime((double) realmRealmEvent.getCreationTime());
            clearEvent.setIdCard(realmRealmEvent.getIdCard());
            clearEvent.setIsSent(true);
            clearEvent.setOwnerId(ownerId);
            return clearEvent;
        }

        static RealmEvent clearEventToRealmEvent(Event event) {
            RealmEvent realmEvent = new RealmEvent();
            realmEvent.setObjectId(event.getObjectId());
            realmEvent.setCreationTime(event.getCreationTime().intValue());
            realmEvent.setIdCard(event.getIdCard());
            realmEvent.setCreated(event.getCreated());
            realmEvent.setIsSent(true);
            realmEvent.setOwnerId(event.getOwnerId());
            Log.d("", "getCreationTime=" + realmEvent.getCreationTime() + "");
            return realmEvent;
        }
    }
}
