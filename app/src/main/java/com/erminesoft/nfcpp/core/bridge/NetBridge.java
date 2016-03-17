package com.erminesoft.nfcpp.core.bridge;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.core.callback.MainCallBack;

/**
 * Created by Aleks on 09.03.2016.
 */
public interface NetBridge {

    void loginUser (String login, String password, MainCallBack mainCallBack);

    void autoLoginUser(MainCallBack callback);

    void registryUser (BackendlessUser user, MainCallBack mainCallBack);

    void addNewEvent(String idCard, final MainCallBack callback);

    void getAllEvents(String ownerId, MainCallBack callback);

    void getTodayEvents(String ownerId, long curTime, final MainCallBack callback);
}
