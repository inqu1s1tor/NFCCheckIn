package com.erminesoft.nfcpp.core.bridge;

import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.User;

import java.util.List;
import java.util.Observer;

public interface DbBridge {

    User getMe();

    void setMyUser(User myUser);

    void addNewObserver(Observer observer);

    void removeObserver(Observer observer);

    List<Event> getAllEvents();

    List<Event> getUnsentEvents();

    void saveEvent(List<Event> events);

    void saveEvent(Event event);

    List<Event> getEventsByDate(String date);

    List<Event> getEventsByMonth(String date);


    List<User> getAllUsers();

    void saveUser(List<User> users);

    void saveUser(User user);

    Event getLastEventByCardId(String idCard);

    List<Event> getEventsByIdPerMonth(String ownerId, String date);

    List<Event> getEventsByDateAndUserId(String date, String userId);


    void saveCards(List<Card> cards);

    void saveCards(Card card);

    List<Card> getAllCards();

    Card getCardById(String cardId);

    boolean containCardById(String cardId);

    void clearAllData();
}
