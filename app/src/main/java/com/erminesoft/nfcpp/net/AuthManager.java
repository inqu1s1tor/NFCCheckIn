package com.erminesoft.nfcpp.net;

import android.text.TextUtils;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;

/**
 * Created by Aleks on 10.03.2016.
 */
final class AuthManager {

    private SharedHelper sharedHelper;
    private DbBridge dbBridge;

    AuthManager (SharedHelper sharedHelper, DbBridge dbBridge) {
        this.sharedHelper = sharedHelper;
        this.dbBridge = dbBridge;
    }

    void registryUser(BackendlessUser user, final MainCallBack mainCallBack) {

        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser registeredUser) {
                Log.d("registryUser", "user has been registered and now can login");
                String login = registeredUser.getProperty("name").toString();
                String password = registeredUser.getPassword().toString();
                logInUser(login, password, mainCallBack);

            }

            public void handleFault(BackendlessFault fault) {
                mainCallBack.onError(fault.getMessage());
                Log.d("registryUser", "fault = " + fault.getMessage());
            }
        });

    }


    void autoLogin(MainCallBack callback){
        String login = sharedHelper.getUserName();
        String password = sharedHelper.getUserPassword();

        if(TextUtils.isEmpty(login) || TextUtils.isEmpty(password)){
            callback.onError("Empty Login and/or passwd ");
            return;
        }

        logInUser(login, password, callback);
    }

    void logInUser(final String login, final String password, final MainCallBack callback) {

        Backendless.UserService.login(login, password, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser registeredUser) {
                Log.d("logInUser", "login as " + login);

                sharedHelper.setUserName(registeredUser.getProperty("name").toString());
                sharedHelper.setUserPassword(password);

                dbBridge.setMyUser(registeredUser);
                callback.onSuccess();
            }

            public void handleFault(BackendlessFault fault) {
                callback.onError(fault.getMessage());
                Log.d("logInUser", "fault = " + fault.toString());
            }
        });
    }

}
