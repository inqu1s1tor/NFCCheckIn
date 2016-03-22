package com.erminesoft.nfcpp.net;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.util.ExtractorToUser;

import java.util.List;

/**
 * Created by evgen on 18.03.2016.
 */
public class UsersManager {

    private final DbBridge dbBridge;

    UsersManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    void getAllUsers(final MainCallBack callback, String searchName) {

        String whereClause = "name LIKE '%" + searchName + "%'";
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);

        Backendless.Data.of(BackendlessUser.class).find(query, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> users) {
                List<BackendlessUser> backendlessUsers = users.getData();
                if(backendlessUsers.size()!= 0) {
                    dbBridge.saveUser(ExtractorToUser.copyBackendlssUserToUserModel(backendlessUsers));
                }

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Server reported an error - " + backendlessFault.getMessage());
                callback.onError(backendlessFault.getMessage());
            }
        });

    }
}
