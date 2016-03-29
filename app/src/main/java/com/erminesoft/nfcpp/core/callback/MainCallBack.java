package com.erminesoft.nfcpp.core.callback;

import android.app.usage.UsageEvents;

import com.erminesoft.nfcpp.model.RealmEvent;

import java.util.List;

/**
 * Created by Aleks on 09.03.2016.
 */
public interface MainCallBack {

    void onSuccess();

    void onError(String error);

    void onSuccessGetEvents(List<RealmEvent> realmEventList);

    void onSuccessGetEvent(RealmEvent realmEvent);

}
