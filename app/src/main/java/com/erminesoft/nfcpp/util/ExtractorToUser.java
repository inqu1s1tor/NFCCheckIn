package com.erminesoft.nfcpp.util;

import android.util.Log;

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
            user.setName(String.valueOf(element.getProperty("name")));
            user.setObjectId(String.valueOf(element.getProperty("objectId")));
            user.setFirstName(String.valueOf(element.getProperty("firstName")));
            user.setLastName(String.valueOf(element.getProperty("lastName")));
            user.setCreated((Date) element.getProperty("created"));
            user.setEmail(String.valueOf(element.getProperty("name")));

            users.add(user);
        }
        return users;

    }

}
