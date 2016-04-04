package com.erminesoft.nfcpp.net;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.BuildConfig;
import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.RealmCard;
import com.erminesoft.nfcpp.model.RealmEvent;


public final class NetManagerFacade implements NetBridge {

    private final AuthManager authManager;

    private final EventManager eventManager;
    private final CardManager cardManager;
    private final BoltsEventManager boltsEventManager;

    private final UsersManager usersManager;

    public NetManagerFacade(Context context, SharedHelper sharedHelper, DbBridge dbBridge) {
        initBackendLess(context);

        authManager = new AuthManager(sharedHelper, dbBridge);
        eventManager = new EventManager(dbBridge);
        cardManager = new CardManager(dbBridge);
        boltsEventManager = new BoltsEventManager(dbBridge);
        usersManager = new UsersManager(dbBridge);
    }

    private void initBackendLess(Context context) {
        Backendless.initApp(context, BuildConfig.BACKENDLESS_APP_ID, BuildConfig.BACKENDLESS_KEY, "v1");
    }

    @Override
    public void loginUser(String login, String password, MainCallBack mainCallBack) {
        authManager.logInUser(login, password, mainCallBack);
    }

    @Override
    public void autoLoginUser(MainCallBack callback) {
        authManager.autoLogin(callback);
    }

    @Override
    public void registryUser(BackendlessUser user, MainCallBack mainCallBack) {
        authManager.registryUser(user, mainCallBack);
    }

    @Override
    public void addNewEvent(RealmEvent realmEvent, MainCallBack callback) {
        eventManager.addNewEvent(realmEvent, callback);
    }

    @Override
    public RealmEvent addNewEvent(RealmEvent realmEvent) {
        return boltsEventManager.addNewEvent(realmEvent);
    }


    @Override
    public void getAllEventsByUserId(String ownerId, MainCallBack callback) {
        eventManager.getAllEventsByUserId(ownerId, callback);
    }

    @Override
    public void getTodayEventsByUserId(String ownerId, long curTime, MainCallBack callback) {
        eventManager.getTodayEventsByUserId(ownerId, curTime, callback);
    }

    @Override
    public void getAllEvents(MainCallBack mainCallBack) {
        eventManager.getAllEvents(mainCallBack);
    }


    @Override
    public void getAllUsers(MainCallBack callback, String searchName) {
        usersManager.getAllUsers(callback, searchName);
    }

    @Override
    public void addNewCard(RealmCard realmCard, MainCallBack callback) {
        cardManager.addNewCard(realmCard, callback);
    }

    @Override
    public void getAllCard(MainCallBack callBack) {
        cardManager.getAllCard(callBack);
    }

    @Override
    public void userLogout() {
        authManager.userLogout();
    }


}
