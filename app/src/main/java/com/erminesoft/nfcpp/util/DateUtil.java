package com.erminesoft.nfcpp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class DateUtil {

    public static final String DATE_FORMAT_Y_M_D = "yyyy-MM-dd";
    public static final String DATE_FORMAT_Y_M_D_H_M = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_M_D_Y = "MM.dd.yyyy";
    public static final String DATE_FORMAT_Y_M = "yyyy-MM";
    public static final String DATE_FORMAT_H_M = "HH:mm";
    public static final String DATE_FORMAT_D = "dd";

    public static int getStartOfDay(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_Y_M_D, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    public static int getEndOfDayInMillis(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_Y_M_D, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 60);
        calendar.set(Calendar.SECOND, 60);
        calendar.set(Calendar.MILLISECOND, 0);

        return (int) (calendar.getTimeInMillis() / 1000);
    }


    public static int getStartOfMonth(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_Y_M, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    public static int getEndOfMonthInMillis(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_Y_M, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 60);
        calendar.set(Calendar.SECOND, 60);
        calendar.set(Calendar.MILLISECOND, 0);

        return (int) (calendar.getTimeInMillis() / 1000);
    }


    public static String getDifferenceTime(long diffInMs) {
        int hh = (int) (TimeUnit.MILLISECONDS.toHours(diffInMs) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(diffInMs)));
        int mm = (int) (TimeUnit.MILLISECONDS.toMinutes(diffInMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMs)));
        String hoursString = (hh < 10 ? "0" + hh : String.valueOf(hh));
        String minutesString = (mm < 10 ? "0" + mm : String.valueOf(mm));
        return hoursString + ":" + minutesString;
    }

    public static String dateToFormatString(long creationTime, String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.getDefault()).format(new Date(creationTime));
    }

    public static String getDateMonthSelected(String date, boolean isPrevious) throws ParseException {
        int month = (isPrevious ? -1 : +1);

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_Y_M, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.add(Calendar.MONTH, month);
        return format.format(new Date(calendar.getTimeInMillis()));
    }

}
