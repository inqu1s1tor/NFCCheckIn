package com.erminesoft.nfcpp.core.bridge;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.Event;

public interface NetBridge {

    void loginUser(String login, String password, MainCallBack mainCallBack);

    void autoLoginUser(MainCallBack callback);

    void registryUser(BackendlessUser user, MainCallBack mainCallBack);

    void addNewEvent(Event event, final MainCallBack callback);

    Event addNewEvent(Event event);

    void getAllEventsByUserId(String ownerId, MainCallBack callback);

    void getTodayEventsByUserId(String ownerId, long curTime, final MainCallBack callback);

    void getAllEvents(final MainCallBack mainCallBack);

    void getAllUsers(final MainCallBack callback, String searchName);

    void addNewCard(Card card, final MainCallBack callback);

    void getAllCard(final MainCallBack callBack);

    void userLogout();
}
