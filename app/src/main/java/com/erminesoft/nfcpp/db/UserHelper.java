package com.erminesoft.nfcpp.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.erminesoft.nfcpp.model.User;

import java.util.List;

final class UserHelper {

    List<User> getAllUsers() {
        return new Select().from(User.class).execute();
    }

    void saveUser(List<User> users) {
        ActiveAndroid.beginTransaction();
        try {

            for (User user : users) {
                user.save();
            }

        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    void saveUser(User user) {
        user.save();
    }

    User getUserByStringParam(String param, String value) {
        return new Select().from(User.class).where(param + " = ?", value).executeSingle();
    }

    void clearUser() {
        new Delete().from(User.class).execute();
    }
}
