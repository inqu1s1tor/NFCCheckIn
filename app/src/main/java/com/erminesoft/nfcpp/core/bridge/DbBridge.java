package com.erminesoft.nfcpp.core.bridge;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.model.Event;

import java.util.List;
import java.util.Observer;

public interface DbBridge {

    BackendlessUser getMyUser();

    void setMyUser(BackendlessUser myUser);

    void addNewObserver(Observer observer);

    void removeObserver(Observer observer);

    List<Event> getEvents();

    void saveEvent(List<Event> events);

    void saveEvent(Event event);

    public List<Event> getEventsByDate(String date);

    List <Users> getAllUsers();

}
