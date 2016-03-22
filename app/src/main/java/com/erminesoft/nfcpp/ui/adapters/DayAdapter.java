package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.Day;
import com.erminesoft.nfcpp.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aleks on 15.03.2016.
 */
public class DayAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<Day> objects;

    public DayAdapter(Context context, List<Day> days) {
        objects = days;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Day getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Day day = getItem(position);
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_day_list, parent, false);
            holder.dateTv = (TextView) convertView.findViewById(R.id.dateId);
            holder.allEventsTv = (TextView) convertView.findViewById(R.id.allEventsId);
            holder.totalTimeTv = (TextView) convertView.findViewById(R.id.totalTimeId);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Log.d("adapter", "*day=" + day);
        if (day != null) {
            holder.dateTv.setText(day.getCurrentDate().toString());
            loadTodayData(holder, day.getCheckingList());
        } else {
            holder.totalTimeTv.setText("no data");
            holder.allEventsTv.setText("no data");
        }

        return convertView;
    }

    public void replaceNewData(List<Day> newObjects) {
        objects = newObjects;
        notifyDataSetChanged();
    }


    private void loadTodayData(Holder holder, List<Date> dateList) {
//        Log.d("loadTodayScreen", "eventList = " + eventList.get(0).getCreated());
        Collections.sort(dateList, new Comparator<Date>() {
            @Override
            public int compare(Date lhs, Date rhs) {
                return lhs.compareTo(rhs);
            }
        });

//        Log.d("loadTodayScreen", "eventList2 = " + eventList.get(0).getCreated());
        if (dateList.size() > 0) {
            loadTodayEvents(holder, dateList);
        }
    }

    private void loadTodayEvents(Holder holder, List<Date> dateList) {
        String totalTime = "";

        Date entry = null;
        Date exit = null;
        long diffInMs = 0;
        String strEvents = "";

        for (int i = 1; i <= dateList.size(); i++) {
            if (i % 2 == 0) { // 2
                exit = dateList.get(i - 1);
                diffInMs = diffInMs + (exit.getTime() - entry.getTime());
                strEvents += "  -  " + dateToFormatString(exit) + "\n";
            } else {  //1
                entry = dateList.get(i - 1);
                strEvents += dateToFormatString(entry);
                if (i == dateList.size()) {
                    strEvents += "  -  --:--";
                    Date curDate = new Date(System.currentTimeMillis());
                    diffInMs = diffInMs + (curDate.getTime() - entry.getTime());
                }
            }
        }

        int hh = (int) (TimeUnit.MILLISECONDS.toHours(diffInMs) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(diffInMs)));
        int mm = (int) (TimeUnit.MILLISECONDS.toMinutes(diffInMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMs)));

        totalTime = hh + ":" + mm;
        holder.allEventsTv.setText(strEvents);
        holder.totalTimeTv.setText("worked time:\n" + totalTime);
    }

    private String dateToFormatString(Date date) {
        String formatString = new SimpleDateFormat("HH:mm").format(date);
        return formatString;
    }

    private static final class Holder {
        TextView dateTv;
        TextView allEventsTv;
        TextView totalTimeTv;
    }

}

