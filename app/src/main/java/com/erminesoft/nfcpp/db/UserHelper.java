package com.erminesoft.nfcpp.db;

import com.erminesoft.nfcpp.model.User;

import java.util.List;

import io.realm.Realm;

final class UserHelper {

    List<User> getAllUsers(Realm realm) {
        return realm.where(User.class).findAll();
    }

    void saveUser(Realm realm, List<User> users) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(users);
        realm.commitTransaction();
        realm.close();
    }

    void saveUser(Realm realm, User user) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
        realm.close();
    }

    User getUserByStringParam(Realm realm,String param, String value){
        return realm.where(User.class).equalTo(param, value).findFirst();
    }

    void clearUser(Realm realm){
        realm.beginTransaction();
        realm.where(User.class).findAll().clear();
        realm.commitTransaction();
    }
}
