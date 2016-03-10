package com.erminesoft.nfcpp.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aleks on 09.03.2016.
 */
public final class SharedHelper {

    private static final String NAME = "NFC-Chec";
    private static final String USER_NAME = "userName";
    private static final String User_PASSWORD = "userPassword";

    private final SharedPreferences mSharedPreferences;

    public SharedHelper (Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void setUserName (String userName) {
        mSharedPreferences.edit().putString(USER_NAME, userName);
    }

    public void setUserPassword (String userPassword) {
        mSharedPreferences.edit().putString(User_PASSWORD, userPassword);
    }

    public String getUserName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public String getUserPassword() {
        return mSharedPreferences.getString(User_PASSWORD, "");
    }

    public void sharedHelperClear() {
        mSharedPreferences.edit().clear().apply();
    }
}
