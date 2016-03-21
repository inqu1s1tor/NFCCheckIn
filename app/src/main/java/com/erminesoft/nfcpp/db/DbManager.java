package com.erminesoft.nfcpp.db;

import android.content.Context;
import android.util.Log;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.User;
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.ParseException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public final class DbManager extends Observable implements DbBridge {

    private final RealmConfiguration configuration;
    private final UserHelper userHelper;
    private final EventHelper eventHelper;
    private BackendlessUser me;

    public DbManager(Context context) {
        configuration = new RealmConfiguration.Builder(context).build();
        userHelper = new UserHelper();
        eventHelper = new EventHelper();
    }

    private Realm initRealm() {
        Realm realm = Realm.getInstance(configuration);
        realm.refresh();
        return realm;
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
    public List<Event> getAllEvents() {
        return eventHelper.getAllEvents(initRealm());
    }

    @Override
    public List<Event> getUnsentEvents() {
        return eventHelper.getUnsentEvents(initRealm());
    }

    @Override
    public void saveEvent(List<Event> events) {
        eventHelper.saveEvent(initRealm(), events);
        setChanged();
        notifyObservers();
    }

    @Override
    public void saveEvent(Event event) {
        eventHelper.saveEvent(initRealm(), event);
        setChanged();
        notifyObservers();
    }

    @Override
    public List<Event> getEventsByDate(String date) {
        return eventHelper.getEventsByDate(initRealm(), date);
    }

    @Override
    public List<User> getAllUsers() {
        return userHelper.getAllUsers(initRealm());
    }

    @Override
    public void saveUser(List<User> users) {
        userHelper.saveUser(initRealm(), users);
    }

    @Override
    public void saveUser(User user) {
        userHelper.saveUser(initRealm(), user);
    }

}
