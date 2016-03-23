package com.erminesoft.nfcpp.net;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

final class EventManager {

    private DbBridge dbBridge;

    public EventManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    void addNewEvent(RealmEvent realmEvent, final MainCallBack callback) {

        Event event = EventConverter.realmEventToClearEvent(realmEvent);
        Backendless.Persistence.save(event, new AsyncCallback<Event>() {
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
                RealmEvent realmEvent = EventConverter.clearEventToRealmEvent(event);
                dbBridge.saveEvent(realmEvent);
                mainCallBack.onSuccess();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                mainCallBack.onError(backendlessFault.getMessage());
            }
        });
    }


    void getAllEvents(String ownerId, final MainCallBack mainCallBack) {
        String whereClause = "ownerId = '" + ownerId + "'";
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);

        Backendless.Data.of(Event.class).find(query, new AsyncCallback<BackendlessCollection<Event>>() {
            @Override
            public void handleResponse(BackendlessCollection<Event> eventBackendlessCollection) {
                List<Event> eventList = eventBackendlessCollection.getData();
                List<RealmEvent> realmEvents = new ArrayList<RealmEvent>(eventList.size());
                for (Event ev : eventList) {
                    realmEvents.add(EventConverter.clearEventToRealmEvent(ev));
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                mainCallBack.onError(backendlessFault.getMessage());
            }
        });
    }


    void getTodayEvents(String ownerId, long curTime, final MainCallBack callback) {
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_M_D_Y).format(curTime);
        String whereClause = "ownerId = '" + ownerId + "'  and  created > '" + curStringDate + "'";
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);

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

    RealmEvent addNewEvent(RealmEvent realmEventToSave) {
        Event event = EventConverter.realmEventToClearEvent(realmEventToSave);
        event = Backendless.Persistence.save(event);
        return EventConverter.clearEventToRealmEvent(event);
    }

    static final class Event {
        private String idCard;
        private boolean isSent;
        private int creationTime;
        private String objectId;
        private Date created;

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getIdCard() {
            return idCard;
        }

        public boolean getIsSent() {
            return isSent;
        }

        public void setIsSent(boolean sent) {
            this.isSent = sent;
        }

        public int getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(int creationTime) {
            this.creationTime = creationTime;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }
    }

    static final class EventConverter {

        static Event realmEventToClearEvent(RealmEvent realmRealmEvent) {
            Event clearEvent = new Event();
            clearEvent.setCreationTime(realmRealmEvent.getCreationTime());
            clearEvent.setIdCard(realmRealmEvent.getIdCard());
            return clearEvent;
        }

        static RealmEvent clearEventToRealmEvent(Event event) {
            RealmEvent realmEvent = new RealmEvent();
            realmEvent.setObjectId(event.getObjectId());
            realmEvent.setCreationTime(event.getCreationTime());
            realmEvent.setIdCard(event.getIdCard());
            realmEvent.setCreated(event.getCreated());
            realmEvent.setIsSent(true);
            return realmEvent;
        }
    }
}
