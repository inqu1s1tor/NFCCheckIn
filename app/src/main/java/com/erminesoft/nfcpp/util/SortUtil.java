package com.erminesoft.nfcpp.util;

import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.EventsToday;

import java.util.Date;
import java.util.List;

public final class SortUtil {

    public static long sortEventsOnTodayAndReturnTotalWorkingTime(List<Event> allEventsList, List<Card> cardList, List<EventsToday> todayEventsList, boolean takeLastEntry) {

        long entryLong = 0;
        long exitLong = 0;
        long diffInMs = 0;
        EventsToday eventsToday;

        for (int i = 1; i <= allEventsList.size(); i++) {
            if (i % 2 == 0) { // 2
                exitLong = ((long) allEventsList.get(i - 1).getCreationTime() * (long) 1000);
                diffInMs = diffInMs + (exitLong - entryLong);

                eventsToday = new EventsToday();
                eventsToday.setNameCard(getCardNameById(allEventsList.get(i - 1).getIdCard(), cardList));
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
                    eventsToday.setNameCard(getCardNameById(allEventsList.get(i - 1).getIdCard(), cardList));
                    eventsToday.setEntry(DateUtil.dateToFormatString(entryLong, DateUtil.DATE_FORMAT_H_M));
                    eventsToday.setExit(" --:-- ");
                    eventsToday.setSelector(allEventsList.get(i - 1).getIsSent());

                    if (takeLastEntry) {
                        eventsToday.setTotal_hours(DateUtil.getDifferenceTime(curDate.getTime() - entryLong));
                        diffInMs = diffInMs + (curDate.getTime() - entryLong);
                    } else {
                        eventsToday.setTotal_hours("00:00");
                    }
                    todayEventsList.add(eventsToday);
                }
            }
        }

        return diffInMs;
    }

    private static String getCardNameById(String cardId, List<Card> cardList){
        String nameCard = "";
        for (Card rc : cardList){
            if (rc.getIdCard().equals(cardId)){
                nameCard = rc.getNameCard();
            }
        }

        return nameCard;
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
            }
        }
        return diffInMs;
    }
}
