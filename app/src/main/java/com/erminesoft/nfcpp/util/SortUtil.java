package com.erminesoft.nfcpp.util;

import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.model.EventsToday;

import java.util.Date;
import java.util.List;

public final class SortUtil {

    public static long sortEventsOnTodayAndReturnTotalWorkingTime(List<RealmEvent> allEventsList, List<EventsToday> todayEventsList) {

        long entryLong = 0;
        long exitLong = 0;
        long diffInMs = 0;
        EventsToday eventsToday;

        for (int i = 1; i <= allEventsList.size(); i++) {
            if (i % 2 == 0) { // 2
                exitLong = ((long) allEventsList.get(i - 1).getCreationTime() * (long) 1000);
                diffInMs = diffInMs + (exitLong - entryLong);

                eventsToday = new EventsToday();
                eventsToday.setEntry(DateUtil.dateToFormatString(entryLong, DateUtil.DATE_FORMAT_H_M));
                eventsToday.setExit(DateUtil.dateToFormatString(exitLong, DateUtil.DATE_FORMAT_H_M));
                eventsToday.setTotal_hours(DateUtil.getDifferenceTime(exitLong - entryLong));
                eventsToday.setSelector(allEventsList.get(i - 1).getIsSent());
                todayEventsList.add(eventsToday);

            } else {  //1
                entryLong = ((long) allEventsList.get(i - 1).getCreationTime() * (long) 1000);
                if (i == allEventsList.size()) {
                    Date curDate = new Date(System.currentTimeMillis());

                    eventsToday = new EventsToday();
                    eventsToday.setEntry(DateUtil.dateToFormatString(entryLong, DateUtil.DATE_FORMAT_H_M));
                    eventsToday.setExit(" --:-- ");
                    eventsToday.setTotal_hours(DateUtil.getDifferenceTime(curDate.getTime() - entryLong));
                    eventsToday.setSelector(allEventsList.get(i - 1).getIsSent());
                    todayEventsList.add(eventsToday);
                    diffInMs = diffInMs + (curDate.getTime() - entryLong);
                }
            }
        }

        return diffInMs;
    }


    public static long sortEventsAndReturnTotalWorkingTime(List<Long> allEventsList) {

        long entryLong = 0;
        long exitLong = 0;
        long diffInMs = 0;
        for (int i = 1; i <= allEventsList.size(); i++) {
            if (i % 2 == 0) { // 2
                exitLong = (allEventsList.get(i - 1) * (long) 1000);
                diffInMs = diffInMs + (exitLong - entryLong);
            } else {  //1
                entryLong = (allEventsList.get(i - 1) * (long) 1000);
//                if (i == allEventsList.size()) {
//                    Date curDate = new Date(System.currentTimeMillis());
//                    diffInMs = diffInMs + (curDate.getTime() - entryLong);
//                }
            }
        }
        return diffInMs;
    }
}
