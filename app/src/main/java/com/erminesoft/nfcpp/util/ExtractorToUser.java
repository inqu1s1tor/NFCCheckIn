package com.erminesoft.nfcpp.util;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Aleks on 21.03.2016.
 */
public final class ExtractorToUser {


    public static List<User> copyBackendlssUserToUserModel(List<BackendlessUser> backendlessUsers) {
        List<User> users = new ArrayList<>();

        for(BackendlessUser element : backendlessUsers) {
            User user = new User();
            user.setName(element.getProperty("name").toString());
            user.setObjectId(element.getProperty("objectId").toString());
            user.setFirstName(element.getProperty("firstName").toString());
            user.setLastName(element.getProperty("lastName").toString());
            user.setCreated((Date) element.getProperty("created"));

            users.add(user);
        }
        return users;

    }

}
