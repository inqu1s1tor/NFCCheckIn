package com.erminesoft.nfcpp.core.bridge;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.RealmCard;
import com.erminesoft.nfcpp.model.RealmEvent;

public interface NetBridge {

    void loginUser(String login, String password, MainCallBack mainCallBack);

    void autoLoginUser(MainCallBack callback);

    void registryUser(BackendlessUser user, MainCallBack mainCallBack);

    void addNewEvent(RealmEvent realmEvent, final MainCallBack callback);

    RealmEvent addNewEvent(RealmEvent realmEvent);

    void getAllEventsByUserId(String ownerId, MainCallBack callback);

    void getTodayEventsByUserId(String ownerId, long curTime, final MainCallBack callback);

    void getAllEvents(final MainCallBack mainCallBack);

    void getAllUsers(final MainCallBack callback, String searchName);


    void addNewCard(RealmCard realmCard, final MainCallBack callback);

    void getAllCard(final MainCallBack callBack);

    void userLogout();
}
