package com.erminesoft.nfcpp.db;

import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.ParseException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

final class EventHelper {

    List<RealmEvent> getAllEvents(Realm realm) {
        return realm.where(RealmEvent.class).findAll();
    }

    List<RealmEvent> getUnsentEvents(Realm realm) {
        return realm.where(RealmEvent.class).equalTo("isSent", false).findAll();
//        return realm.where(RealmEvent.class).findAll();
    }

    void saveEvent(Realm realm, List<RealmEvent> realmEvents) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmEvents);
        realm.commitTransaction();
        realm.close();
    }

    void saveEvent(Realm realm, RealmEvent realmEvent) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmEvent);
        realm.commitTransaction();
        realm.close();
    }

    List<RealmEvent> getEventsByDate(Realm realm, String date) {
        int startTime = 0;
        int endTime = (int) (System.currentTimeMillis() / 1000);

        try {
            startTime = DateUtil.getStartOfDay(date);
            endTime = DateUtil.getEndOfDayInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return realm.where(RealmEvent.class).between("creationTime", startTime, endTime).findAll();
    }

    List<RealmEvent> getEventsByMonth(Realm realm, String date) {
        int startTime = 0;
        int endTime = (int) (System.currentTimeMillis() / 1000);

        try {
            startTime = DateUtil.getStartOfMonth(date);
            endTime = DateUtil.getEndOfDayInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realm.where(RealmEvent.class).between("creationTime", startTime, endTime).findAll();
    }

    RealmEvent getLastEventByCardId(Realm realm, String cardId){
        RealmResults<RealmEvent> results = realm.where(RealmEvent.class).equalTo("idCard", cardId).findAllSorted("creationTime", Sort.DESCENDING);

        if(results != null && !results.isEmpty()){
            return results.first();
        }
        return null;
    }
}
