package com.erminesoft.nfcpp.net;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.util.ExtractorToUserUtil;

import java.util.List;

/**
 * Created by evgen on 18.03.2016.
 */
class UsersManager {

    private static final int PAGE_SIZE = 100;

    private final DbBridge dbBridge;

    UsersManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    void getAllUsers(final MainCallBack callback, String searchName) {

        final String myId = dbBridge.getMe().getObjectId();

        String whereClause = "name LIKE '%" + searchName + "%'";
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);
        query.setPageSize(PAGE_SIZE);

        Backendless.Data.of(BackendlessUser.class).find(query, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> users) {
                List<BackendlessUser> backendlessUsers = users.getData();
                if (backendlessUsers.size() != 0) {
                    for (int i = 0; i < backendlessUsers.size(); i++) {
                        if (backendlessUsers.get(i).getObjectId().equals(myId)){
                            backendlessUsers.remove(backendlessUsers.get(i));
                        }
                    }
                    dbBridge.saveUser(ExtractorToUserUtil.convertBackendlssUserToUserModel(backendlessUsers));
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
