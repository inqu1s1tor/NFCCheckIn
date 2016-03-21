package com.erminesoft.nfcpp.db;

import android.content.Context;
import android.util.Log;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DbManager extends Observable implements DbBridge {

    private final Realm realm;
    private BackendlessUser me;

    public DbManager(Context context) {
        RealmConfiguration configuration = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(configuration);
    }

    @Override
    public BackendlessUser getMyUser() {
        return me;
    }

    @Override
    public void setMyUser(BackendlessUser myUser) {
        me = myUser;
        setChanged();
        notifyObservers();
    }

    @Override
    public void addNewObserver(Observer observer) {
        super.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        super.deleteObserver(observer);
    }

    @Override
    public List<Event> getEvents() {
        return realm.where(Event.class).findAll();
    }

    @Override
    public void saveEvent(List<Event> events) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(events);
        realm.commitTransaction();
        setChanged();
        notifyObservers();
    }

    @Override
    public void saveEvent(Event event) {
        Log.d("DB", "saveEvent");
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();

        setChanged();
        notifyObservers();
    }

    @Override
    public List<Event> getEventsByDate(String date) {
        Date startTime = Calendar.getInstance().getTime();
        Date endTime = startTime;

        try {
            startTime = DateUtil.getStartOfDay(date);
            endTime = DateUtil.getEndOfDayInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return realm.where(Event.class).between("creationTime", startTime, endTime).findAll();
    }

}
