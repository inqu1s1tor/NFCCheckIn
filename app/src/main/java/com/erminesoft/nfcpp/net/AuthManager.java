package com.erminesoft.nfcpp.net;

import android.text.TextUtils;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.User;

import java.util.Date;
import java.util.List;

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
                String login = registeredUser.getProperty("name").toString();
                String password = registeredUser.getPassword();
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
                sharedHelper.setUserName(registeredUser.getProperty("name").toString());
                sharedHelper.setUserPassword(password);
                getRole(registeredUser, callback);
            }

            public void handleFault(BackendlessFault fault) {
                callback.onError(fault.getMessage());
                Log.d("logInUser", "fault = " + fault.toString());
            }
        });
    }



    private void getRole(final BackendlessUser registeredUser, final MainCallBack callback) {
        Backendless.UserService.getUserRoles(new AsyncCallback<List<String>>() {
            @Override
            public void handleResponse(List<String> userRoles) {
                String userRolesStr = "";
                for (String role : userRoles) {
                    userRolesStr += role + ";";
                }
                User user = new User();
                user.setObjectId(registeredUser.getObjectId());
                user.setName(registeredUser.getProperty("name").toString());
                user.setEmail(registeredUser.getProperty("name").toString());  // TODO
                user.setCreated((Date) registeredUser.getProperty("created"));
                user.setFirstName(registeredUser.getProperty("firstName").toString());
                user.setLastName(registeredUser.getProperty("lastName").toString());
                user.setUserRoles(userRolesStr);
                dbBridge.setMyUser(user);
                callback.onSuccess();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("getRole", "fault = " + fault.toString());
            }
        });
    }

    void userLogout(){
        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                dbBridge.clearAllData();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("userLogout", "fault = " + fault.toString());
            }
        });
    }
}
