package com.erminesoft.nfcpp.core.bridge;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.User;

import java.util.List;
import java.util.Observer;

public interface DbBridge {

    @Deprecated
    BackendlessUser getMyUser();

    @Deprecated
    void setMyUser(BackendlessUser myUser);

    User getMe();

    void setMyUser(User myUser);

    void addNewObserver(Observer observer);

    void removeObserver(Observer observer);

    List<Event> getAllEvents();

    List<Event> getUnsentEvents();

    void saveEvent(List<Event> events);

    void saveEvent(Event event);

    List<Event> getEventsByDate(String date);

    List<User> getAllUsers();

    void saveUser(List<User> users);

    void saveUser(User user);

}
