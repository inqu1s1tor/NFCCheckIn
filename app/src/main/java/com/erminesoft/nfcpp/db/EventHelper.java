package com.erminesoft.nfcpp.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

final class EventHelper {

    List<Event> getAllEvents() {
        return new Select().from(Event.class).execute();
    }

    List<Event> getUnsentEvents() {
        List<Event> events;
        try {
            events = new Select().from(Event.class).where("is_sent", false).execute();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            events = new ArrayList<>();
        }
        return events;
    }

    void saveEvent(List<Event> events) {
        ActiveAndroid.beginTransaction();
        try {
            for (Event event : events) {
                event.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    void saveEvent(Event event) {
        event.save();
    }

    List<Event> getEventsByDate(String date) {
        int startTime = 0;
        int endTime = (int) (System.currentTimeMillis() / 1000);

        try {
            startTime = DateUtil.getStartOfDay(date);
            endTime = DateUtil.getEndOfDayInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Select().from(Event.class)
                .where("creation_time > ?", startTime)
                .and("creation_time < ? ", endTime)
                .orderBy("creation_time ASC")
                .execute();
    }

    List<Event> getEventsByMonth(String date) {
        int startTime = 0;
        int endTime = (int) (System.currentTimeMillis() / 1000);

        try {
            startTime = DateUtil.getStartOfMonth(date);
            endTime = DateUtil.getEndOfMonthInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Select().from(Event.class)
                .where("creation_time > ?", startTime)
                .and("creation_time < ? ", endTime)
                .orderBy("creation_time ASC")
                .execute();
    }

    List<Event> getMonthEventsByUserId(String date, String userId) {
        int startTime = 0;
        int endTime = (int) (System.currentTimeMillis() / 1000);

        try {
            startTime = DateUtil.getStartOfMonth(date);
            endTime = DateUtil.getEndOfMonthInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Select().from(Event.class)
                .where("creation_time > ?", startTime)
                .and("creation_time < ?", endTime)
                .and("owner_id = ?", userId)
                .orderBy("creation_time ASC")
                .execute();
    }

    List<Event> getEventsByDateAndUserId(String date, String userId) {
        int startTime = 0;
        int endTime = (int) (System.currentTimeMillis() / 1000);

        try {
            startTime = DateUtil.getStartOfDay(date);
            endTime = DateUtil.getEndOfDayInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Select().from(Event.class)
                .where("creation_time > ?", startTime)
                .and("creation_time < ?", endTime)
                .and("owner_id = ?", userId)
                .orderBy("creation_time ASC")
                .execute();
    }

    Event getLastEventByCardId(String cardId) throws IllegalArgumentException {
        Event event = null;
        try {
            event = new Select().from(Event.class).where("card_id", cardId).orderBy("creation_time DESC").executeSingle();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return event;
    }

    void clearAllEvents() {
        new Delete().from(Event.class).execute();

    }
}
