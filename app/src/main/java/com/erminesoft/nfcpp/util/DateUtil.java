package com.erminesoft.nfcpp.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateUtil {

    public static final String DATE_FORMAT_Y_M_D = "yyyy-MM-dd";
    public static final String DATE_FORMAT_Y_M_D_H_M = "yyyy-MM-dd HH:mm";

    public static int getStartOfDay(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_Y_M_D);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (int) (calendar.getTimeInMillis()/1000);
    }

    public static int getEndOfDayInMillis(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_Y_M_D);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 60);
        calendar.set(Calendar.SECOND, 60);
        calendar.set(Calendar.MILLISECOND, 0);

        return (int) (calendar.getTimeInMillis()/1000);
    }


    public static String getDifferenceTime(long diffInMs) {
        int hh = (int) (TimeUnit.MILLISECONDS.toHours(diffInMs) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(diffInMs)));
        int mm = (int) (TimeUnit.MILLISECONDS.toMinutes(diffInMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMs)));
        String hoursString = (hh < 10 ? "0"+hh : String.valueOf(hh));
        String minutesString = (mm < 10 ? "0"+mm : String.valueOf(mm));
        String differenceTime = hoursString + ":" + minutesString;
        return differenceTime;
    }

    public static String dateToFormatString(long creationTime) {
        String formatString = new SimpleDateFormat("HH:mm").format(new Date(creationTime));
        return formatString;
    }
}
