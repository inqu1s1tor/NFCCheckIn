package com.erminesoft.nfcpp.util;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ExtractorToUser {

    public static List<User> convertBackendlssUserToUserModel(List<BackendlessUser> backendlessUsers) {
        List<User> users = new ArrayList<>(backendlessUsers.size());

        for (BackendlessUser element : backendlessUsers) {

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
