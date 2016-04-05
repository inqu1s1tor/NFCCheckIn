package com.erminesoft.nfcpp.core.callback;

import com.erminesoft.nfcpp.model.Event;

import java.util.List;

/**
 * Created by Aleks on 09.03.2016.
 */
public interface MainCallBack {

    void onSuccess();

    void onError(String error);

    void onSuccessGetEvents(List<Event> eventList);

    void onSuccessGetEvent(Event event);

}
