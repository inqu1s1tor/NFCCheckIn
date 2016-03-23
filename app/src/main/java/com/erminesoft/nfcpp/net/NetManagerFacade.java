package com.erminesoft.nfcpp.net;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.Event;


/**
 * Created by Aleks on 09.03.2016.
 */
public final class NetManagerFacade implements NetBridge {

    private final AuthManager authManager;

    private final EventManager eventManager;
    private final BoltsEventManager boltsEventManager;

    private final UsersManager usersManager;

    public NetManagerFacade (Context context,SharedHelper sharedHelper, DbBridge dbBridge) {

        initBackendLess(context);
        authManager = new AuthManager(sharedHelper, dbBridge);

        eventManager = new EventManager(dbBridge);
        boltsEventManager = new BoltsEventManager();

        usersManager = new UsersManager(dbBridge);
    }

    private void initBackendLess(Context context) {
        Resources res = context.getResources();
        Log.d("init", "APP_ID = " + res.getString(R.string.BACKENDLESS_APP_ID));
        Log.d("init", "KEY = " + res.getString(R.string.BACKENDLESS_KEY));
        Backendless.initApp(context, res.getString(R.string.BACKENDLESS_APP_ID), res.getString(R.string.BACKENDLESS_KEY), "v1");
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
    public void addNewEvent(String idCard, MainCallBack callback) {
        eventManager.addNewEvent(idCard, callback);
    }

    @Override
    public Event addNewEventBolt(Event eventToSave) {
        return boltsEventManager.addNewEvent(eventToSave);
    }

    @Override
    public void getAllEvents(String ownerId, MainCallBack callback) {
        eventManager.getAllEvents(ownerId, callback);
    }

    @Override
    public void getTodayEvents(String ownerId, long curTime, MainCallBack callback) {
        eventManager.getTodayEvents(ownerId, curTime, callback);
    }

    @Override
    public void getAllUsers(MainCallBack callback, String searchName) {
        usersManager.getAllUsers(callback, searchName);
    }


}
