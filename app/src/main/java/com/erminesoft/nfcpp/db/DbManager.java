package com.erminesoft.nfcpp.db;

import android.content.Context;
import android.util.Log;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.model.User;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public final class DbManager extends Observable implements DbBridge {

    private final SharedHelper sharedHelper;
    private final RealmConfiguration configuration;
    private final UserHelper userHelper;
    private final EventHelper eventHelper;
    private BackendlessUser me;

    public DbManager(Context context, SharedHelper sharedHelper) {
        this.sharedHelper = sharedHelper;
        configuration = new RealmConfiguration.Builder(context).build();
        userHelper = new UserHelper();
        eventHelper = new EventHelper();
    }

    private Realm initRealm() {
        Realm realm = Realm.getInstance(configuration);
        realm.refresh();
        return realm;
    }

    private void notifyObserversProcedure() {
        setChanged();
        notifyObservers();
    }

    @Override
    public User getMe() {
        String param = "name";
        String value = sharedHelper.getUserName();
        return userHelper.getUserByStringParam(initRealm(), param, value);
    }

    @Override
    public void setMyUser(User myUser) {
        Log.d("setMyUser", "*getUserRoles()="+myUser.getUserRoles());
        userHelper.saveUser(initRealm(), myUser);
        notifyObserversProcedure();
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
    public List<RealmEvent> getAllEvents() {
        return eventHelper.getAllEvents(initRealm());
    }

    @Override
    public List<RealmEvent> getUnsentEvents() {
        return eventHelper.getUnsentEvents(initRealm());
    }

    @Override
    public void saveEvent(List<RealmEvent> realmEvents) {
        eventHelper.saveEvent(initRealm(), realmEvents);
        notifyObserversProcedure();
    }

    @Override
    public void saveEvent(RealmEvent realmEvent) {
        eventHelper.saveEvent(initRealm(), realmEvent);
        notifyObserversProcedure();
    }

    @Override
    public List<RealmEvent> getEventsByDate(String date) {
        return eventHelper.getEventsByDate(initRealm(), date);
    }

    @Override
    public List<RealmEvent> getEventsByMonth(String date) {
        return eventHelper.getEventsByMonth(initRealm(), date);
    }


    @Override
    public List<User> getAllUsers() {
        return userHelper.getAllUsers(initRealm());
    }

    @Override
    public void saveUser(List<User> users) {
        userHelper.saveUser(initRealm(), users);
        notifyObserversProcedure();
    }

    @Override
    public void saveUser(User user) {
        userHelper.saveUser(initRealm(), user);
        notifyObserversProcedure();
    }

    @Override
    public RealmEvent getLastEventByCardId(String idCard) {
        return eventHelper.getLastEventByCardId(initRealm(), idCard);
    }

    @Override
    public List<RealmEvent> getEventsByIdPerMonth(String userId, String date) {
        return eventHelper.getMonthEventsByUserId(initRealm(), date, userId);
    }

}
