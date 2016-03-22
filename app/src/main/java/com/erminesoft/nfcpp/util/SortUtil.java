package com.erminesoft.nfcpp.util;

import android.util.Log;

import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.EventsToday;

import java.util.Date;
import java.util.List;

public final class SortUtil {

    public static long sortEventsOnTodayAndReturnTotalWorkingTime(List<Event> allEventsList, List<EventsToday> todayEventsList) {

        long entryLong = 0;
        long exitLong = 0;
        long diffInMs = 0;
        EventsToday eventsToday;

        for (int i = 1; i <= allEventsList.size(); i++) {
            if (i % 2 == 0) { // 2
                exitLong = ((long) allEventsList.get(i - 1).getCreationTime() * (long) 1000);
                diffInMs = diffInMs + (exitLong - entryLong);

                eventsToday = new EventsToday();
                eventsToday.setEntry(DateUtil.dateToFormatString(entryLong));
                eventsToday.setExit(DateUtil.dateToFormatString(exitLong));
                eventsToday.setTotal_hours(DateUtil.getDifferenceTime(exitLong - entryLong));
                eventsToday.setSelector(false);
                todayEventsList.add(eventsToday);

            } else {  //1
                entryLong = ((long) allEventsList.get(i - 1).getCreationTime() * (long) 1000);
                if (i == allEventsList.size()) {
                    Date curDate = new Date(System.currentTimeMillis());

                    eventsToday = new EventsToday();
                    eventsToday.setEntry(DateUtil.dateToFormatString(entryLong));
                    eventsToday.setExit(" --:-- ");
                    eventsToday.setTotal_hours(DateUtil.getDifferenceTime(curDate.getTime() - entryLong));
                    eventsToday.setSelector(false);

                    todayEventsList.add(eventsToday);

                    diffInMs = diffInMs + (curDate.getTime() - entryLong);
                }
            }
        }

        return diffInMs;
    }
}
