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


/**
 * Created by Aleks on 09.03.2016.
 */
public final class NetManagerFacade implements NetBridge {

    private AuthManager authManager;
    private EventManager eventManager;

    public NetManagerFacade (Context context,SharedHelper sharedHelper, DbBridge dbBridge) {

        initBackendLess(context);
        authManager = new AuthManager(sharedHelper, dbBridge);
        eventManager = new EventManager();
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
}
