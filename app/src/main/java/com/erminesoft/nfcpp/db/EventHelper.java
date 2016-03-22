package com.erminesoft.nfcpp.db;

import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.ParseException;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

final class EventHelper {

    List<Event> getAllEvents(Realm realm) {
        return realm.where(Event.class).findAll();
    }

    List<Event> getUnsentEvents(Realm realm) {
        return realm.where(Event.class).equalTo("isSent", false).findAll();
    }

    void saveEvent(Realm realm, List<Event> events) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(events);
        realm.commitTransaction();
        realm.close();
    }

    void saveEvent(Realm realm, Event event) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
        realm.close();
    }

    List<Event> getEventsByDate(Realm realm, String date) {
        int startTime = 0;
        int endTime = (int) (System.currentTimeMillis() / 1000);

        try {
            startTime = DateUtil.getStartOfDay(date);
            endTime = DateUtil.getEndOfDayInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return realm.where(Event.class).between("creationTime", startTime, endTime).findAll();
    }

    Event getLastEventByCardId(Realm realm, String cardId){
        return realm.where(Event.class).equalTo("idCard", cardId).findAllSorted("creationTime", Sort.DESCENDING).first();
    }
}
