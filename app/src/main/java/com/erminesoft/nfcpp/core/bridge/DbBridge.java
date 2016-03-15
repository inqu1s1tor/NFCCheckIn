package com.erminesoft.nfcpp.core.bridge;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.model.Event;

import java.util.List;
import java.util.Observer;

/**
 * Created by Aleks on 10.03.2016.
 */
public interface DbBridge {

    BackendlessUser getMyUser();

    void setMyUser(BackendlessUser myUser);
    void notifyObservers();

    void addNewObserver(Observer observer);

    void removeObserver(Observer observer);

}
