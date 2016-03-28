package com.erminesoft.nfcpp.core.bridge;

import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.model.User;
import java.util.List;
import java.util.Observer;

import io.realm.Realm;

public interface DbBridge {

    User getMe();

    void setMyUser(User myUser);

    void addNewObserver(Observer observer);

    void removeObserver(Observer observer);

    List<RealmEvent> getAllEvents();

    List<RealmEvent> getUnsentEvents();

    void saveEvent(List<RealmEvent> realmEvents);

    void saveEvent(RealmEvent realmEvent);

    List<RealmEvent> getEventsByDate(String date);

    List<RealmEvent> getEventsByMonth(String date);


    List<User> getAllUsers();

    void saveUser(List<User> users);

    void saveUser(User user);

    RealmEvent getLastEventByCardId(String idCard);

    List<RealmEvent> getEventsByIdPerMonth(String userId, String date);

}
