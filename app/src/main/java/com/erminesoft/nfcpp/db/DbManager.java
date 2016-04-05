package com.erminesoft.nfcpp.db;

import android.content.Context;
import android.util.Log;

import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.User;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public final class DbManager extends Observable implements DbBridge {

    private final SharedHelper sharedHelper;
    private final UserHelper userHelper;
    private final EventHelper eventHelper;
    private final CardHelper cardHelper;

    public DbManager(Context context, SharedHelper sharedHelper) {
        this.sharedHelper = sharedHelper;
        userHelper = new UserHelper();
        eventHelper = new EventHelper();
        cardHelper = new CardHelper();
    }

    private void notifyObserversProcedure() {
        setChanged();
        notifyObservers();
    }

    @Override
    public User getMe() {
        String param = "name";
        String value = sharedHelper.getUserName();
        return userHelper.getUserByStringParam(param, value);
    }

    @Override
    public void setMyUser(User myUser) {
        Log.d("setMyUser", "*getUserRoles()=" + myUser.getUserRoles());
        userHelper.saveUser(myUser);
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
    public List<Event> getAllEvents() {
        return eventHelper.getAllEvents();
    }

    @Override
    public List<Event> getUnsentEvents() {
        return eventHelper.getUnsentEvents();
    }

    @Override
    public void saveEvent(List<Event> events) {
        eventHelper.saveEvent(events);
        notifyObserversProcedure();
    }

    @Override
    public void saveEvent(Event event) {
        Log.d("saveEvent", "event.getCreationTime() = " + event.getCreationTime());
        eventHelper.saveEvent(event);
        notifyObserversProcedure();
    }

    @Override
    public List<Event> getEventsByDate(String date) {
        return eventHelper.getEventsByDate(date);
    }

    @Override
    public List<Event> getEventsByMonth(String date) {
        return eventHelper.getEventsByMonth(date);
    }


    @Override
    public List<User> getAllUsers() {
        return userHelper.getAllUsers();
    }

    @Override
    public void saveUser(List<User> users) {
        userHelper.saveUser(users);
        notifyObserversProcedure();
    }

    @Override
    public void saveUser(User user) {
        userHelper.saveUser(user);
        notifyObserversProcedure();
    }

    @Override
    public Event getLastEventByCardId(String idCard) {
        return eventHelper.getLastEventByCardId(idCard);
    }

    @Override
    public List<Event> getEventsByIdPerMonth(String userId, String date) {
        return eventHelper.getMonthEventsByUserId(date, userId);
    }

    @Override
    public List<Event> getEventsByDateAndUserId(String date, String userId) {
        return eventHelper.getEventsByDateAndUserId(date, userId);
    }

    @Override
    public void saveCards(List<Card> cards) {
        cardHelper.saveCard(cards);
        notifyObserversProcedure();
    }

    @Override
    public void saveCards(Card card) {
        cardHelper.saveCard(card);
        notifyObserversProcedure();
    }

    @Override
    public List<Card> getAllCards() {
        return cardHelper.getAllCards();
    }

    @Override
    public Card getCardById(String cardId) {
        return cardHelper.getCardById(cardId);
    }

    @Override
    public boolean containCardById(String cardId) {
        return cardHelper.getCardById(cardId) != null;
    }

    @Override
    public void clearAllData() {
        userHelper.clearUser();
        eventHelper.clearAllEvents();
        cardHelper.clearAllCards();
    }

}
